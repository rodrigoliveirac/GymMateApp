package com.rodcollab.gymmateapp.auth

import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.core.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class FakeAuthRepository : AuthRepository {

    val users = mutableListOf(User(uuid = 0.toString(),email = "shadow@gmail.com", password = "1112", username = "shadow"))

    override suspend fun signInWithEmailAndPassword(email: String, password: String, onResult: (ResultOf<User>) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val user = users.find {
                    it.email == email && it.password == password
                }
                user?.apply {
                    onResult(ResultOf.Success(User(uuid = uuid, username = username, email = this.email, password = this.password)))
                } ?: run {
                    val userNotFoundException = NullPointerException("User not found")
                    onResult(ResultOf.Failure(message = userNotFoundException.message, throwable = userNotFoundException.cause))
                }

            } catch (e:Exception) {
                onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
            }
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (ResultOf<User>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val user = users.find { it.email == email }

                user?.let {
                    val exception = IllegalArgumentException("User already Exist")
                    onResult(ResultOf.Failure(exception.message,exception.cause))
                } ?: run {
                    val uuid = (users.last().uuid?.toInt()?.plus(1)).toString()
                    val newUser = User(
                        uuid  = uuid,
                        username = email.split("@")[0],
                        email = email,
                        password = password
                    )
                    users.add(newUser)
                    onResult(ResultOf.Success(newUser))
                }
            } catch (e: Exception) {
                onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
            }
        }
    }

    override suspend fun sendPasswordResetEmail(email: String, onResult: (ResultOf<String>) -> Unit) {
        withContext(Dispatchers.IO) {
//            firebaseAuth.sendPasswordResetEmail(email)
//                .addOnSuccessListener {
//                    onResult(ResultOf.Success("It's all good. Now check your email"))
//                    Log.i(FIREBASE_RESET_PASSWORD, "Password reset successfully")
//                }
//                .addOnFailureListener { e ->
//                    onResult(ResultOf.Failure(message = e.message, throwable = e.cause))
//                    Log.i(FIREBASE_RESET_PASSWORD, "Password reset unsuccessfully")
//                }

        }
    }

}
