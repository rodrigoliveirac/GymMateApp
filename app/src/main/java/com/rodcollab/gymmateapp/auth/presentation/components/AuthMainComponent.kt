package com.rodcollab.gymmateapp.auth.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rodcollab.gymmateapp.R
import com.rodcollab.gymmateapp.auth.presentation.intent.AuthUiAction

@Composable
fun AuthMainComponent(
    displayToSignUpBtn: Boolean,
    confirmBtnText: Int,
    headlineScreen: Int,
    email: String,
    password: String,
    repeatPassword: String?,
    showPassword: Boolean,
    onAuthUiAction: (AuthUiAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.clip(CircleShape),
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(headlineScreen)
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { onAuthUiAction(AuthUiAction.OnEmailValueChange(it)) },
            label = { Text(text = stringResource(R.string.auth_email_label)) },
            placeholder = { Text(text = stringResource(R.string.auth_email_placeholder)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) }
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { onAuthUiAction(AuthUiAction.OnPasswordValueChange(it)) },
            label = { Text(text = stringResource(R.string.auth_password_label)) },
            placeholder = { Text(text = stringResource(R.string.auth_password_placeholder)) },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { onAuthUiAction(AuthUiAction.OnShowPasswordClick) }) {
                    Icon(
                        painter = painterResource(id = if (showPassword) R.drawable.ic_eye_close else R.drawable.ic_eye_open),
                        contentDescription = null
                    )
                }
            }
        )
        repeatPassword?.let {
            Spacer(modifier = Modifier.size(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = it,
                onValueChange = { onAuthUiAction(AuthUiAction.OnRepeatPasswordValueChange(it)) },
                label = { Text(text = stringResource(R.string.auth_repeat_password_label)) },
                placeholder = { Text(text = stringResource(R.string.auth_password_placeholder)) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onAuthUiAction(AuthUiAction.OnShowPasswordClick) }) {
                        Icon(
                            painter = painterResource(id = if (showPassword) R.drawable.ic_eye_close else R.drawable.ic_eye_open),
                            contentDescription = null
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = { onAuthUiAction(AuthUiAction.OnConfirmClick) }) {
            Text(text = stringResource(confirmBtnText))
        }
        Spacer(modifier = Modifier.size(16.dp))
        TextButton(
            modifier = Modifier.alpha(if (displayToSignUpBtn) 1.0f else 0f),
            onClick = { onAuthUiAction(AuthUiAction.GoToSignUp) }) {
            Text(text = stringResource(R.string.auth_create_account_text_button))
        }
    }
}
