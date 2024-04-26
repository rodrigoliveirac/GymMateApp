package com.rodcollab.gymmateapp.auth.presentation.intent

sealed interface AuthUiAction {
    data class OnEmailValueChange(val email: String) : AuthUiAction
    data class OnPasswordValueChange(val password: String) : AuthUiAction
    data object OnConfirmClick : AuthUiAction
    data object OnShowPasswordClick : AuthUiAction
}