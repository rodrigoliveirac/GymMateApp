package com.rodcollab.gymmateapp.auth.presentation.intent

sealed interface LoginUiAction {
    data class OnEmailValueChange(val email: String) : LoginUiAction
    data class OnPasswordValueChange(val password: String) : LoginUiAction
    data object OnLoginClick : LoginUiAction
    data object OnShowPasswordClick : LoginUiAction
}