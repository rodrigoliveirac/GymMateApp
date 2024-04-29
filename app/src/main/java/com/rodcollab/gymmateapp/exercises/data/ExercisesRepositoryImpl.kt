package com.rodcollab.gymmateapp.exercises.data

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.dao.BodyPartDao
import com.rodcollab.gymmateapp.core.data.dao.ExerciseDao
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ExercisesRepositoryImpl @Inject constructor(
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

                val uuid = document ?: run {
                    UUID.randomUUID().toString()
                }

                val storageRef = storage.reference
                val fileRef =
                    storageRef.child(uuid).child("${uuid}.pdf")

                var imgUrl: String? = null

                val updateImgUrl: (String?) -> Unit = { imgUrl = it }

                val onResultFileSaved: (ResultOf<String>?) -> Unit = {
                    it?.let {
                        launch(Dispatchers.IO + Job()) {
                            firebaseAuth.currentUser?.zza()?.options?.storageBucket?.let {
                                downloadFile(it, uuid) { res ->
                                    when (res) {
                                        is ResultOf.Success -> {
                                            updateImgUrl(res.value)
                                        }

                                        is ResultOf.Failure -> {
                                            updateImgUrl(null)
                                        }

                                        else -> {

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                img?.let { image ->
                    async {
                        fileRef.putFile(Uri.parse(image))
                            .addOnSuccessListener {
                                onResultFileSaved(ResultOf.Success(image))
                            }.addOnFailureListener { exception ->
                                throw exception
                            }
                    }.await()
                }

                val data = hashMapOf<String, Any?>(
                    "uuid" to uuid,
                    "name" to name,
                    "bodyPart" to bodyPart,
                    "image" to img,
                    "notes" to notes
                )
                firebaseAuth.currentUser?.uid?.let { userId ->
                    firestore
                        .collection(USERS_COLLECTION)
                        .document(userId)
                        .collection(EXERCISES_COLLECTION)
                        .document(uuid)
                        .set(data)
                        .addOnSuccessListener {
                            onResult(
                                ResultOf.Success(
                                    ExerciseExternal(
                                        uuid = uuid,
                                        name = name,
                                        image = img,
                                        bodyPart = bodyPart,
                                        notes = notes,
                                        userExercise = true,
                                    )
                                )
                            )
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error writing document", e)
                        }
                } ?: run {
                    throw Exception("User is not found")
                }

            } catch (e: Exception) {
                onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
            }
        }
    }

    private suspend fun downloadFile(
        storageBucket: String,
        child: String,
        onResult: (ResultOf<String>) -> Unit
    ) {
        withContext(Dispatchers.IO) {

            val ref =
                storage.getReferenceFromUrl("gs://$storageBucket/$child/$child.pdf")

            ref.downloadUrl.addOnSuccessListener {
                onResult(
                    ResultOf.Success(
                        Uri.parse(it.normalizeScheme().toString()).toString()
                    )
                )
            }
                .addOnFailureListener {
                    onResult(ResultOf.Failure(message = it.message, throwable = it.cause))
                }


        }
    }

    companion object {
        const val USERS_COLLECTION = "USERS_COLLECTION"
        const val EXERCISES_COLLECTION = "EXERCISES_COLLECTION"
    }

}