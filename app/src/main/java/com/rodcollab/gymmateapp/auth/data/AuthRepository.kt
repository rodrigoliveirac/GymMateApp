package com.rodcollab.gymmateapp.auth.data

import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.core.ResultOf

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password:String, onResult: (ResultOf<User>) -> Unit)
    suspend fun createUserWithEmailAndPassword(email: String, password: String, onResult: (ResultOf<User>) -> Unit)
    suspend fun sendPasswordResetEmail(email: String, onResult: (ResultOf<String>) -> Unit)
}