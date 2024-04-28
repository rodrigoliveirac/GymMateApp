package com.rodcollab.gymmateapp.auth.presentation

import com.rodcollab.gymmateapp.auth.domain.model.User
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.core.ResultOf

data class AuthUiState(
    val message: String = "",
    val displayRepeatPassword: Boolean = false,
    val displayArrowBack:Boolean = false,
    val displayToSignUpBtn: Boolean = true,
    val signPath: SignPath = SignPath.SignIn(),
    val email: String = "",
    val password: String = "",
    val repeatPassword: String? = null,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val displaySnackbar: Boolean = false,
    val resultOf: ResultOf<User> = ResultOf.Idle
)