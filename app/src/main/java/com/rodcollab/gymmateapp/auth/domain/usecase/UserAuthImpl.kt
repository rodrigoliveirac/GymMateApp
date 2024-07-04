package com.rodcollab.gymmateapp.auth.domain.usecase

import com.rodcollab.gymmateapp.auth.data.AuthRepository
import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.core.ResultOf
class UserAuthImpl(
    private val authRepository: AuthRepository,
    private val emailAndPasswordValidator: EmailAndPasswordValidator
) : UserAuth {
    override suspend fun invoke(
        authType: AuthType,
        signPath: SignPath,
        onResult: (ResultOf<User>) -> Unit
    ) {
        when(authType) {
            is AuthType.EmailAndPassword -> {
                authType.apply {
                    emailAndPasswordValidator(email, password, repeatPassword) { resultOf ->
                        when (resultOf) {
                            is ResultOf.Failure -> {
                                onResult(resultOf)
                            }

                            is ResultOf.Success -> {
                                when (signPath) {
                                    is SignPath.SignIn -> {
                                        authRepository.signInWithEmailAndPassword(email, password, onResult)
                                    }

                                    is SignPath.SignUp -> {
                                        authRepository.createUserWithEmailAndPassword(
                                            email,
                                            password,
                                            onResult
                                        )
                                    }
                                }
                            }

                            else -> {
                                ResultOf.Idle
                            }
                        }
                    }
                }
            }
            is AuthType.GoogleSignIn -> {
                authRepository.signInGoogle(authType.tokenId) { resultOf -> onResult(resultOf) }
            }
        }
    }
}