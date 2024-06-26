package com.rodcollab.gymmateapp.auth.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rodcollab.gymmateapp.R
import com.rodcollab.gymmateapp.auth.presentation.intent.AuthUiAction
import com.rodcollab.gymmateapp.core.ui.GymMateTextField
import com.rodcollab.gymmateapp.core.ui.PasswordTextField
import com.rodcollab.gymmateapp.core.ui.RepeatPasswordTextField

@Composable
fun AuthMainComponent(
    displayToSignUpBtn: Boolean,
    confirmBtnText: Int,
    headlineScreen: Int,
    email: String,
    password: String,
    repeatPassword: String?,
    showPassword: Boolean,
    onAuthUiAction: (AuthUiAction) -> Unit,
) {
    val isSignUp by remember { mutableStateOf(repeatPassword != null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.displaySmall,
                text = stringResource(headlineScreen)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = if (!isSignUp) R.drawable.greeting_hand else R.drawable.flexed_biceps),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        GymMateTextField(
            text = email,
            label = R.string.auth_email_placeholder,
            icon = Icons.Default.Email
        ) { newEmailValue -> onAuthUiAction(AuthUiAction.OnEmailValueChange(newEmailValue)) }
        Spacer(modifier = Modifier.size(16.dp))

        PasswordTextField(
            text = password,
            showPassword = showPassword,
            onChangeVisibility = { onAuthUiAction(AuthUiAction.OnShowPasswordClick) }
        ) { newPasswordValue -> onAuthUiAction(AuthUiAction.OnPasswordValueChange(newPasswordValue)) }

        if (isSignUp) {
            Spacer(modifier = Modifier.size(16.dp))
            RepeatPasswordTextField(
                text = repeatPassword!!,
                showPassword = showPassword,
                onChangeVisibility = { onAuthUiAction(AuthUiAction.OnShowPasswordClick) }
            ) { newPasswordValue -> onAuthUiAction(AuthUiAction.OnPasswordValueChange(newPasswordValue)) }
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
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = stringResource(R.string.auth_create_account_text_button),
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
