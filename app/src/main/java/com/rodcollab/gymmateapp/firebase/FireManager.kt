package com.rodcollab.gymmateapp.firebase

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rodcollab.gymmateapp.core.Collections
import com.rodcollab.gymmateapp.core.ResultOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class FireManager<T : Any>(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    var userId: String? = firebaseAuth.currentUser?.uid
    fun deleteDocument(
        collection: String,
        document: String,
        onResult: (ResultOf<String>) -> Unit
    ) {
        try {
            userId?.let {
                firestore
                    .collection("USERS_COLLECTION")
                    .document(it)
                    .collection(Collections.EXERCISES_COLLECTION)
                    .document(document)
                    .delete()
                    .addOnSuccessListener {
                        onResult(ResultOf.Success("Exercise successfully deleted!"))
                        Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!")
                    }.addOnFailureListener { e ->
                        onResult(ResultOf.Failure(e.message, e.cause))
                        Log.w(ContentValues.TAG, "Error deleting document", e)
                    }
            } ?: run {
                throw Exception("User id not found")
            }
        } catch (e: Exception) {
            onResult(ResultOf.Failure(e.message, e.cause))
        }

    }

    suspend fun filterCollection(
        filterPair: Pair<String, Any>,
        collection: String,
        onResult: suspend (ResultOf<List<T>>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val list = mutableListOf<T>()
            val callback: (ResultOf<List<T>>) -> Unit = {
                launch(Dispatchers.Default + Job()) {
                    onResult(it)
                }
            }
            userId?.let { userId ->
                firestore
                    .collection("USERS_COLLECTION")
                    .document(userId)
                    .collection(collection)
                    .whereEqualTo(filterPair.first, filterPair.second)
                    .get()
                    .addOnCompleteListener {
                        val data = GymMateModelSerializer<T>().read(collection)
                        it.result.documents.map { doc ->
                            list.add(doc.toObject(data::class.java)!!)
                        }
                        Log.d("EXERCISE_REMOTE", data.toString())

                        callback(ResultOf.Success(list))
                        it.exception?.let { e ->
                            callback(ResultOf.Failure(e.message, e.cause))
                        }
                    }
            }
        }
    }


    suspend fun deleteFile(
        document: String,
        onResult: suspend (ResultOf<String>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val storageRef = storage.reference
            val desertRef = storageRef.child(generatePath(document))

            val callback : ()-> Unit = {
                onCoroutineScope(
                    ResultOf.Success("The file was deleted successfully!"),
                    onResult
                )
            }
            desertRef
                .delete()
                .addOnSuccessListener {
                    callback()
                }.addOnFailureListener {
                    onCoroutineScope(ResultOf.Failure(it.message, it.cause), onResult)
                }
        }
    }

    suspend fun upload(
        image: String, document: String, onResult: suspend (ResultOf<String>) -> Unit
    ) {
        withContext(Dispatchers.IO) {

            val storageRef = storage.reference
            val imagePath = generatePath(document)
            val fileRef = storageRef.child(document).child(imagePath)

            val downloadCallback: (String) -> Unit = {ref ->
                launch(Dispatchers.IO + Job()) {
                    try {
                        download(
                            ref,
                            onResult
                        )
                    } catch (e: Exception) {
                        onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
                    }
                }
            }

            try {
                fileRef.putFile(Uri.parse(image)).addOnSuccessListener {
                    downloadCallback(it.metadata?.reference.toString())
                }.addOnFailureListener { exception ->
                    throw exception
                }
            } catch (e: Exception) {
                onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
            }
        }
    }

    private suspend fun download(
        ref: String, onResult: suspend (ResultOf<String>) -> Unit
    ) {
        withContext(Dispatchers.IO) {

            val gsReference = storage.getReferenceFromUrl(ref)

            val callback: (Uri)-> Unit = {
                onCoroutineScope(
                    ResultOf.Success(
                        Uri.parse(it.normalizeScheme().toString()).toString()
                    ), onResult
                )
            }
            gsReference.downloadUrl.addOnSuccessListener { uri ->
                callback(uri)
            }.addOnFailureListener { e ->
                onCoroutineScope(
                    ResultOf.Failure(message = e.message, throwable = e.cause),
                    onResult
                )
            }
        }
    }


    suspend fun createOrUpdate(
        collection1: String,
        collection2: String,
        document: String,
        model: T,
        onResult: suspend (ResultOf<T>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val data = async { GymMateModelSerializer<T>().invoke(model) }.await()

            val callback: (ResultOf<T>) -> Unit = {
                launch(Dispatchers.IO +Job()) {
                    onResult(it)
                }
            }

            userId?.let { userId ->
                firestore.collection(collection1).document(userId).collection(collection2)
                    .document(document).set(data).addOnSuccessListener {
                        callback(ResultOf.Success(model))
                        Log.d(ContentValues.TAG, "DocumentSnapshot successfully written or updated!")
                    }.addOnFailureListener { e ->
                        callback(ResultOf.Failure(e.message, e.cause))
                        Log.w(ContentValues.TAG, "Error writing document", e)
                    }
            } ?: run {
                throw Exception("User is not found")
            }
        }
    }

    private fun generatePath(document: String): String {
        return "${document}.pdf"
    }

    private fun <R> CoroutineScope.onCoroutineScope(
        result: ResultOf<R>,
        onResult: suspend (ResultOf<R>) -> Unit
    ) {
        launch(Dispatchers.IO + Job()) {
            onResult(result)
        }
    }
}