package com.rodcollab.gymmateapp.firebase

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rodcollab.gymmateapp.core.ResultOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoSQLManagerImpl<T : Any>(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : NoSQLManager<T> {

    override var userId: String? = firebaseAuth.currentUser?.uid

    override suspend fun upload(
        image: String,
        document: String,
        onResult: suspend (ResultOf<String>) -> Unit
    ) {
        withContext(Dispatchers.IO) {

            val storageRef = storage.reference
            val imagePath = generatePath(document)
            val fileRef = storageRef.child(document).child(imagePath)

            val downloadCallback = toDownloadFile(imagePath, onResult)

            try {
                fileRef.putFile(Uri.parse(image))
                    .addOnSuccessListener {
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
        imagePath: String,
        onResult: suspend (ResultOf<String>) -> Unit
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

    override suspend fun download(
        bucket: String,
        filePath: String,
        onResult: suspend (ResultOf<String>) -> Unit
    ) {
        withContext(Dispatchers.IO) {

            val gsReference = storage.getReferenceFromUrl("gs://$bucket/$filePath")

            val callback: (ResultOf<String>) -> Unit = { result ->
                launch { onResult(result) }
            }

            gsReference.downloadUrl.addOnSuccessListener { uri ->
                callback(ResultOf.Success(Uri.parse(uri.normalizeScheme().toString()).toString()))
            }.addOnFailureListener { e ->
                callback(ResultOf.Failure(message = e.message, throwable = e.cause))
            }

        }
    }

    override suspend fun create(
        collection1: String,
        collection2: String,
        document: String,
        model: T,
        onResult: (ResultOf<T>) -> Unit
    ) {
        val data = GymMateModelSerializer<T>().invoke(model)
        userId?.let { userId ->
            firestore
                .collection(collection1)
                .document(userId)
                .collection(collection2)
                .document(document)
                .set(data)
                .addOnSuccessListener {
                    onResult(ResultOf.Success(model))
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error writing document", e)
                }
        } ?: run {
            throw Exception("User is not found")
        }
    }

    private fun generatePath(document: String): String {
        return "${document}.pdf"
    }
}