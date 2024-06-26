package com.rodcollab.gymmateapp.core.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import com.rodcollab.gymmateapp.R

@Composable
fun PasswordTextField(text: String, showPassword: Boolean, onChangeVisibility: ()-> Unit, onPasswordChange: (String) -> Unit) {
    GymMateTextField(
        text = text,
        label = R.string.auth_password_label,
        showPassword = showPassword,
        hasSensitiveData = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        icon = Icons.Default.Lock,
        onChangeVisibility = onChangeVisibility
    ) { newPasswordValue -> onPasswordChange(newPasswordValue) }
}

@Composable
fun RepeatPasswordTextField(text: String, showPassword: Boolean, onChangeVisibility: ()-> Unit, onPasswordChange: (String) -> Unit) {
    GymMateTextField(
        text = text,
        label = R.string.auth_repeat_password_label,
        showPassword = showPassword,
        hasSensitiveData = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        icon = Icons.Default.Lock,
        onChangeVisibility = onChangeVisibility
    ) { newPasswordValue -> onPasswordChange(newPasswordValue) }
}