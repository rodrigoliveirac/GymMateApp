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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class FireManager<T : Any>(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    var userId: String? = firebaseAuth.currentUser?.uid
    fun delete(
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

            desertRef
                .delete()
                .addOnSuccessListener {
                    onCoroutineScope(
                        ResultOf.Success("The file was deleted successfully!"),
                        onResult
                    )
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

            val downloadCallback = toDownloadFile(imagePath, onResult)

            try {
                fileRef.putFile(Uri.parse(image)).addOnSuccessListener {

                    downloadCallback()
                }.addOnFailureListener { exception ->
                    throw exception
                }
            } catch (e: Exception) {
                onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
            }
        }
    }

    private fun CoroutineScope.toDownloadFile(
        imagePath: String, onResult: suspend (ResultOf<String>) -> Unit
    ): () -> Unit {
        val downloadCallback: () -> Unit = {
            launch(Dispatchers.IO + Job()) {
                try {
                    download(
                        firebaseAuth.currentUser?.zza()?.options?.storageBucket!!,
                        imagePath,
                        onResult
                    )
                } catch (e: Exception) {
                    onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
                }
            }
        }
        return downloadCallback
    }

    suspend fun download(
        bucket: String, filePath: String, onResult: suspend (ResultOf<String>) -> Unit
    ) {
        withContext(Dispatchers.IO) {

            val gsReference = storage.getReferenceFromUrl("gs://$bucket/$filePath")

            gsReference.downloadUrl.addOnSuccessListener { uri ->
                onCoroutineScope(
                    ResultOf.Success(
                        Uri.parse(uri.normalizeScheme().toString()).toString()
                    ), onResult
                )
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
        onResult: (ResultOf<T>) -> Unit
    ) {
        val data = GymMateModelSerializer<T>().invoke(model)

        userId?.let { userId ->
            firestore.collection(collection1).document(userId).collection(collection2)
                .document(document).set(data).addOnSuccessListener {
                    onResult(ResultOf.Success(model))
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written or updated!")
                }.addOnFailureListener { e ->
                    onResult(ResultOf.Failure(e.message, e.cause))
                    Log.w(ContentValues.TAG, "Error writing document", e)
                }
        } ?: run {
            throw Exception("User is not found")
        }
    }

    private fun generatePath(document: String): String {
        return "${document}.pdf"
    }

    private fun <R> CoroutineScope.onCoroutineScope(
        result: ResultOf<R>,
        onResult: suspend (ResultOf<R>) -> Unit
    ) {
        launch {
            onResult(result)
        }
    }
}