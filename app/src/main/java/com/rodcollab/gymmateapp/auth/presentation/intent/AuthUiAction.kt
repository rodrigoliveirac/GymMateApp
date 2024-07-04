package com.rodcollab.gymmateapp.auth.presentation.intent

import com.rodcollab.gymmateapp.auth.domain.usecase.AuthType

sealed interface AuthUiAction {
    data class OnEmailValueChange(val email: String) : AuthUiAction
    data class OnPasswordValueChange(val password: String) : AuthUiAction
    data class OnRepeatPasswordValueChange(val repeatPassword: String) : AuthUiAction
    data class OnConfirmClick(val authType: AuthType) : AuthUiAction
    data object OnShowPasswordClick : AuthUiAction
    data object GoToSignUp : AuthUiAction
}