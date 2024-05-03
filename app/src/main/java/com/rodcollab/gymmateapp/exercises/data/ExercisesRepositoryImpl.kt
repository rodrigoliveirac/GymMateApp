package com.rodcollab.gymmateapp.exercises.data

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.dao.BodyPartDao
import com.rodcollab.gymmateapp.core.data.dao.ExerciseDao
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.firebase.NoSQLManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ExercisesRepositoryImpl @Inject constructor(
    private val remoteManager: NoSQLManager<ExerciseExternal>,
    private val firebaseAuth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val exerciseDao: ExerciseDao,
    private val bodyPartDao: BodyPartDao,
) : ExercisesRepository {

    override suspend fun getBodyParts(onResult: suspend (ResultOf<List<BodyPart>>) -> Unit) {
        return withContext(Dispatchers.IO) {
            try {
                onResult(ResultOf.Success(bodyPartDao.getAll()))
            } catch (e: Exception) {
                onResult(ResultOf.Failure(e.message, e.cause))
            }
        }
    }

    override suspend fun userExercises(
        bodyPart: String,
        onResult: (ResultOf<List<ExerciseExternal>>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val callback: (List<DocumentSnapshot>?)-> Unit = {
                launch(Dispatchers.IO + Job()) {
                    onResult(ResultOf.Success( async {
                        it?.map {
                            ExerciseExternal(
                                uuid = it.getString("uuid")!!.toString(),
                                name = it.getString("name")!!.toString(),
                                bodyPart = it.getString("bodyPart")!!.toString(),
                                image = it.getString("image") as String?,
                                notes = it.getString("notes")!!.toString(),
                                userExercise = true
                            )
                        }!!
                    }.await()))
                }
            }
            firebaseAuth.currentUser?.uid?.let {
                firestore.collection(USERS_COLLECTION)
                    .document(it)
                    .collection(EXERCISES_COLLECTION)
                    .whereEqualTo("bodyPart", bodyPart).addSnapshotListener { value, error ->
                        callback(value?.documents)

                        error?.let {
                            onResult(ResultOf.Failure(it.message, it.cause))
                        }
                    }
            }
        }
    }

    override suspend fun getExerciseByBodyPart(
        bodyPart: String,
        onResult: (ResultOf<List<ExerciseExternal>>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                onResult(ResultOf.Success(exerciseDao.getByBodyPart(bodyPart).map {
                    ExerciseExternal(
                        uuid = it.id.toString(),
                        userExercise = false,
                        name = it.name,
                        bodyPart = it.bodyPart,
                        notes = it.notes,
                        image = it.image
                    )
                }))
            }catch (e:Exception) {
                onResult(ResultOf.Failure(e.message,e.cause))
            }
        }
    }

    override suspend fun delete(document: String, onResult: (ResultOf<String>) -> Unit) {
        try {
            firebaseAuth.currentUser?.uid?.let { userId ->
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXERCISES_COLLECTION)
                    .document(document)
                    .delete()
                    .addOnSuccessListener {
                        onResult(ResultOf.Success("Exercise successfully deleted!"))
                        Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener { e ->
                        onResult(ResultOf.Failure(e.message, e.cause))
                        Log.w(TAG, "Error deleting document", e)
                    }
            }
        } catch (e: Exception) {
            onResult(ResultOf.Failure(e.message, e.cause))
        }
    }

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
            remoteManager.createOrUpdate(
                USERS_COLLECTION, EXERCISES_COLLECTION, document, model, onResult
            )
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
        remoteManager.deleteFile(document) { result ->
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
        remoteManager.upload(image = image, document = document) { result ->
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
                    remoteManager.createOrUpdate(
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
            remoteManager.upload(image = image, document = uuid) { result ->
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
                        remoteManager.createOrUpdate(
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
            remoteManager.createOrUpdate(
                USERS_COLLECTION, EXERCISES_COLLECTION, uuid, model, onResult
            )
        }
    }

    companion object {
        const val USERS_COLLECTION = "USERS_COLLECTION"
        const val EXERCISES_COLLECTION = "EXERCISES_COLLECTION"
    }

}