package com.rodcollab.gymmateapp.auth.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.core.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor( private val firebaseAuth: FirebaseAuth) :
    AuthRepository {

    override suspend fun signInWithEmailAndPassword(email: String, password: String, onResult: (ResultOf<User>) -> Unit) {
        withContext(Dispatchers.IO) {
            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.apply {
                        onResult(ResultOf.Success(User(uuid = uid, username = displayName, email = this.email, password = password)))
                    }
                    Log.i(FIREBASE_LOGIN, "User logged successfully")
                }
                .addOnFailureListener { e ->
                    onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
                    Log.i(FIREBASE_LOGIN, "User logged unsuccessfully")
                }
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (ResultOf<User>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.apply {
                        onResult(ResultOf.Success(User(uuid = uid, username = displayName, email = this.email, password = password)))
                    }
                    Log.i(FIREBASE_SIGNUP, "Registered successfully")
                }
                .addOnFailureListener { e ->
                    onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
                    Log.i(FIREBASE_SIGNUP, "Registered unsuccessfully")
                }
        }
    }

    override suspend fun sendPasswordResetEmail(email: String, onResult: (ResultOf<String>) -> Unit) {
        withContext(Dispatchers.IO) {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    onResult(ResultOf.Success("It's all good. Now check your email"))
                    Log.i(FIREBASE_RESET_PASSWORD, "Password reset successfully")
                }
                .addOnFailureListener { e ->
                    onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
                    Log.i(FIREBASE_RESET_PASSWORD, "Password reset unsuccessfully")
                }

        }
    }

    companion object {
        private const val FIREBASE_LOGIN = "FIREBASE_LOGIN"
        private const val FIREBASE_SIGNUP = "FIREBASE_SIGNUP"
        private const val FIREBASE_RESET_PASSWORD = "FIREBASE_RESET_PASSWORD"
    }

}