package com.rodcollab.gymmateapp.firebase

import com.rodcollab.gymmateapp.core.ResultOf

interface NoSQLManager<T: Any> {
    var userId: String?
    suspend fun create(
        collection1: String,
        collection2: String,
        document: String,
        model: T,
        onResult: (ResultOf<T>) -> Unit
    )

    suspend fun upload(image: String, document: String, onResult: suspend (ResultOf<String>) -> Unit)

    suspend fun download(bucket: String, filePath: String, onResult: suspend (ResultOf<String>) -> Unit)
}