package com.rodcollab.gymmateapp.auth.presentation

import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.core.ResultOf

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val resultOf: ResultOf<User>? = null
)
