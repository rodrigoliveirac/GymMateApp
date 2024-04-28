package com.rodcollab.gymmateapp.auth.presentation.intent

sealed interface AuthUiAction {
    data class OnEmailValueChange(val email: String) : AuthUiAction
    data class OnPasswordValueChange(val password: String) : AuthUiAction
    data class OnRepeatPasswordValueChange(val repeatPassword: String) : AuthUiAction
    data object OnConfirmClick : AuthUiAction
    data object OnShowPasswordClick : AuthUiAction
    data object GoToSignUp : AuthUiAction
}