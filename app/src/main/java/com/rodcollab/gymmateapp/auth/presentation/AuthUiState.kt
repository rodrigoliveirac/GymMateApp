package com.rodcollab.gymmateapp.auth.presentation

import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.core.ResultOf

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String? = null,
    val showPassword: Boolean = false,
    val resultOf: ResultOf<User>? = null,
    val signPath: SignPath? = null
)
