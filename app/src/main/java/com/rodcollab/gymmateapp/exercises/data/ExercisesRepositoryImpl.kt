package com.rodcollab.gymmateapp.exercises.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rodcollab.gymmateapp.core.Collections.EXERCISES_COLLECTION
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.dao.BodyPartDao
import com.rodcollab.gymmateapp.core.data.dao.ExerciseDao
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.firebase.FireManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ExercisesRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firebaseStore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage,
    private val exerciseDao: ExerciseDao,
    private val bodyPartDao: BodyPartDao,
) : ExercisesRepository,
    FireManager<ExerciseExternal>(firebaseAuth, firebaseStore, firebaseStorage) {

    override suspend fun getBodyParts(onResult: suspend (ResultOf<List<BodyPart>>) -> Unit) {
        return withContext(Dispatchers.IO) {
            try {
                onResult(ResultOf.Success(bodyPartDao.getAll()))
            } catch (e: Exception) {
                onResult(ResultOf.Failure(e.message, e.cause))
            }
        }
    }

    override suspend fun getExerciseByBodyPart(
        bodyPart: String, onResult: suspend (ResultOf<List<ExerciseExternal>>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val exercises = mutableListOf<ExerciseExternal>()

                getLocalExercises(bodyPart) { result -> finally(result, exercises) }

                getRemoteExercises(bodyPart) { result ->
                    withContext(Dispatchers.Unconfined) {
                        finally(result, exercises)
                    }
                }
                onResult(ResultOf.Success(exercises))
            } catch (e: Exception) {
                onResult(ResultOf.Failure(e.message, e.cause))
            }
        }
    }

    private fun finally(
        result: ResultOf<List<ExerciseExternal>>,
        exercises: MutableList<ExerciseExternal>
    ) {
        when (result) {
            is ResultOf.Success -> {
                exercises.addAll(result.value)
            }

            is ResultOf.Failure -> {
                ResultOf.Failure(result.message, result.throwable)
            }

            else -> {}
        }
    }

    private suspend fun getRemoteExercises(
        bodyPart: String,
        onResult: suspend (ResultOf<List<ExerciseExternal>>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            filterCollection(
                filterPair = Pair(
                    first = "bodyPart",
                    second = bodyPart
                ), EXERCISES_COLLECTION,
                onResult
            )
        }
    }

    private suspend fun getLocalExercises(
        bodyPart: String,
        onResult: (ResultOf<List<ExerciseExternal>>) -> Unit
    ) {
        try {
            val list = mutableListOf<ExerciseExternal>()
            exerciseDao.getByBodyPart(bodyPart).map {
                list.add(
                    ExerciseExternal(
                        uuid = it.id.toString(),
                        userExercise = false,
                        name = it.name,
                        bodyPart = it.bodyPart,
                        notes = it.notes,
                        image = it.image
                    )
                )
            }
            onResult(ResultOf.Success(list))
        } catch (e: Exception) {
            onResult(ResultOf.Failure(e.message, e.cause))
        }
    }


    override suspend fun delete(document: String, onResult: (ResultOf<String>) -> Unit) =
        delete(EXERCISES_COLLECTION, document, onResult)

    override suspend fun addOrEditExercise(
        document: String?,
        bodyPart: String,
        name: String,
        img: String?,
        notes: String,
        onResult: (ResultOf<ExerciseExternal>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                document?.let { doc ->
                    editExercise(doc, img, name, bodyPart, notes, onResult)
                } ?: run {
                    createExercise(img, name, bodyPart, notes, onResult)
                }
            } catch (e: Exception) {
                onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
            }
        }
    }

    private suspend fun editExercise(
        document: String,
        img: String?,
        name: String,
        bodyPart: String,
        notes: String,
        onResult: (ResultOf<ExerciseExternal>) -> Unit
    ) {

        img?.let { image ->
            if (image.contains("file")) {
                uploadFile(image, document, name, bodyPart, notes, onResult)
            } else {
                deleteFile(document, image, name, bodyPart, notes, onResult)
            }
        } ?: run {
            val model = ExerciseExternal(
                uuid = document,
                name = name,
                image = img,
                bodyPart = bodyPart,
                notes = notes,
                userExercise = true
            )
            createOrUpdate(USERS_COLLECTION, EXERCISES_COLLECTION, document, model, onResult)
        }
    }

    private suspend fun deleteFile(
        document: String,
        image: String,
        name: String,
        bodyPart: String,
        notes: String,
        onResult: (ResultOf<ExerciseExternal>) -> Unit
    ) {
        deleteFile(document) { result ->
            when (result) {
                is ResultOf.Success -> {
                    uploadFile(image, document, name, bodyPart, notes, onResult)
                }

                is ResultOf.Failure -> {
                    onResult(result)
                }

                else -> {
                    throw Exception("Unknown Error")
                }
            }
        }
    }

    private suspend fun uploadFile(
        image: String,
        document: String,
        name: String,
        bodyPart: String,
        notes: String,
        onResult: (ResultOf<ExerciseExternal>) -> Unit
    ) {
        upload(image = image, document = document) { result ->
            when (result) {
                is ResultOf.Success -> {
                    val model = ExerciseExternal(
                        uuid = document,
                        name = name,
                        image = result.value,
                        bodyPart = bodyPart,
                        notes = notes,
                        userExercise = true
                    )
                    createOrUpdate(
                        USERS_COLLECTION, EXERCISES_COLLECTION, document, model, onResult
                    )
                }

                is ResultOf.Failure -> {
                    onResult(result)
                }

                else -> {
                    throw Exception("Unknown exception")
                }
            }
        }
    }

    private suspend fun createExercise(
        img: String?,
        name: String,
        bodyPart: String,
        notes: String,
        onResult: (ResultOf<ExerciseExternal>) -> Unit
    ) {
        val uuid = UUID.randomUUID().toString()

        img?.let { image ->
            upload(image, uuid) { result ->
                when (result) {
                    is ResultOf.Success -> {
                        val model = ExerciseExternal(
                            uuid = uuid,
                            name = name,
                            image = result.value,
                            bodyPart = bodyPart,
                            notes = notes,
                            userExercise = true
                        )
                        createOrUpdate(
                            USERS_COLLECTION, EXERCISES_COLLECTION, uuid, model, onResult
                        )
                    }

                    is ResultOf.Failure -> {
                        onResult(result)
                    }

                    else -> {
                        throw Exception("Unknown exception")
                    }
                }
            }
        } ?: run {
            val model = ExerciseExternal(
                uuid = uuid,
                name = name,
                image = img,
                bodyPart = bodyPart,
                notes = notes,
                userExercise = true
            )
            createOrUpdate(USERS_COLLECTION, EXERCISES_COLLECTION, uuid, model, onResult)
        }
    }

    companion object {
        const val USERS_COLLECTION = "USERS_COLLECTION"
    }

}