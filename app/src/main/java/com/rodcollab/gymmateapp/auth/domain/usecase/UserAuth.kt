package com.rodcollab.gymmateapp.auth.domain.usecase

import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.core.ResultOf

interface UserAuth {
    suspend operator fun invoke(authType: AuthType, signPath: SignPath, onResult: (ResultOf<User>) -> Unit)
}

sealed interface AuthType {
    data class EmailAndPassword(val email:String, val password: String, val repeatPassword: String?) : AuthType
    data class GoogleSignIn(val tokenId: String?) : AuthType
}