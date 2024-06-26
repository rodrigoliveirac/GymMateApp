package com.rodcollab.gymmateapp.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rodcollab.gymmateapp.R

@Composable
fun GymMateTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: Int,
    icon: ImageVector,
    showPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    hasSensitiveData: Boolean = false,
    onChangeVisibility:()-> Unit = { },
    callback: (String) -> Unit,
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var isFocused by remember { mutableStateOf(false) }

    Text(
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth(),
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleSmall,
        text = stringResource(label)
    )
    Spacer(modifier = Modifier.size(4.dp))
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .border(
                width = 1.dp,
                if (isFocused) MaterialTheme.colorScheme.onPrimary else Color.Transparent,
                RoundedCornerShape(8.dp)
            ),
        value = text,
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        onValueChange = { newValue -> callback(newValue) },
        placeholder = { Text(text = stringResource(label)) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            if(hasSensitiveData) {
                IconButton(onClick = onChangeVisibility ) {
                    Icon(
                        painter = painterResource(id = if (showPassword) R.drawable.ic_eye_close else R.drawable.ic_eye_open),
                        contentDescription = null
                    )
                }
            }
        }
    )
}