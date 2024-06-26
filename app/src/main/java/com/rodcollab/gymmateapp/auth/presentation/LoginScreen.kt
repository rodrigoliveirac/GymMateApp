package com.rodcollab.gymmateapp.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodcollab.gymmateapp.R
import com.rodcollab.gymmateapp.auth.presentation.components.AuthMainComponent
import com.rodcollab.gymmateapp.core.ui.BasicLoading
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    goTo: (String?) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                         if(uiState.displayArrowBack) {
                             IconButton(onClick = { goTo(null) }) {
                                 Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                             }
                         }
                },
                title = {
                    Text(
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineSmall,
                        text = stringResource(id = uiState.signPath.topBarTitle)
                    )
                })
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(paddingValues)) {

            uiState.apply {
                AuthMainComponent(
                    displayToSignUpBtn = uiState.displayToSignUpBtn,
                    confirmBtnText = uiState.signPath.confirmBtnText,
                    headlineScreen = uiState.signPath.headlineScreen,
                    email = email,
                    password = password,
                    repeatPassword = repeatPassword,
                    showPassword = showPassword,
                ) { uiAction -> viewModel.onAuthUiAction(uiAction) { route -> goTo(route) }  }
            }
        }
        if(uiState.isLoading) {
            BasicLoading(title = uiState.message)
        }
        if(uiState.displaySnackbar) {
            LaunchedEffect(uiState.message) {
                snackbarHostState.showSnackbar(uiState.message)
                delay(500)
                viewModel.hideSnackBar()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreenPreview() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier.shadow(elevation = 6.dp),title = { Text(text = "Sign In") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
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
                    contentDescription = null)
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineMedium,
                    text = "Welcome to GymMate!")
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = { },
                    label = { Text(text = "Email")},
                    placeholder = { Text(text = "user@gmail.com")},
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null)}
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = { },
                    label = { Text(text = "Password")},
                    placeholder = { Text(text = "somepassword") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = { IconButton(onClick = { /*TODO*/ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_eye_open), contentDescription = null)
                    }}
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = { /*TODO*/ }) {
                    Text(text = "LOGIN")
                }
                Spacer(modifier = Modifier.size(16.dp))
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Create account")
                }
            }
        }
    }
}