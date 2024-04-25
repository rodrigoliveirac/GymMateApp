package com.rodcollab.gymmateapp.auth.domain.usecase

import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.core.ResultOf

class UserSignInImpl(
    private val authRepository: AuthRepository,
    private val emailAndPasswordValidator: EmailAndPasswordValidator
) : UserSignIn {
    override suspend fun invoke(email: String, password: String, onResult: (ResultOf<User>) -> Unit) {
        emailAndPasswordValidator(email,password) { resultOf ->
            when(resultOf) {
                is ResultOf.Failure -> {
                    onResult(resultOf)
                }
                is ResultOf.Success -> {
                    authRepository.signInWithEmailAndPassword(email, password, onResult)
                }
                else -> {
                    ResultOf.Loading
                }
            }
        }
    }
}