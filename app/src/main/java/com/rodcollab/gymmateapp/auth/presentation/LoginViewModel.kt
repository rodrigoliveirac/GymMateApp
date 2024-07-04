package com.rodcollab.gymmateapp.auth.presentation

import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.rodcollab.gymmateapp.BuildConfig
import com.rodcollab.gymmateapp.auth.domain.model.AuthDomain
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.auth.presentation.intent.AuthUiAction
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.SIGN_PATH
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.passwordArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens
import com.rodcollab.gymmateapp.core.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authDomain: AuthDomain,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val signPath: String = savedStateHandle[SIGN_PATH] ?: SIGN_IN
    private val getEmailArg: String = savedStateHandle["email"] ?: ""
    private val getPasswordArg: String = savedStateHandle["password"] ?: ""

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val googleIdOption: GetGoogleIdOption by lazy {
         GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.CLIENT_ID)
            .setAutoSelectEnabled(true)
            //.setNonce(<nonce string to use when generating a Google ID token>)
            .build()
    }

    init {
        _uiState.update { currentUiState ->
            val signPath = SignPath.getSignPath(signPath)
            val isSignInPath = signPath == SignPath.SignIn()
            val repeatPassword = if (!isSignInPath) initialRepeatPassword else noRepeatPassword
            currentUiState.copy(
                email = getEmailArg,
                password = getPasswordArg,
                repeatPassword = repeatPassword,
                displayRepeatPassword = !isSignInPath,
                displayArrowBack = !isSignInPath,
                signPath = signPath,
                displayToSignUpBtn = isSignInPath,
            )
        }
    }

    fun onAuthUiAction(action: AuthUiAction, navigateToScreen: (String) -> Unit = {}) {
        viewModelScope.launch {
            when (action) {
                is AuthUiAction.OnEmailValueChange -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(email = action.email)
                    }
                }

                is AuthUiAction.OnPasswordValueChange -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(password = action.password)
                    }
                }

                is AuthUiAction.OnConfirmClick -> {
                    _uiState.value.apply {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isLoading = true,
                                message = getResourceId()
                            )
                        }
                        withContext(Dispatchers.IO) {
                            authDomain.authenticate(
                                authType = action.authType,
                                signPath = signPath,
                                onResult = { resultOf ->
                                    when (resultOf) {
                                        is ResultOf.Success -> {
                                            navigateToScreen(getRoute())
                                        }

                                        is ResultOf.Failure -> {
                                            _uiState.update { currentUiState ->
                                                currentUiState.copy(
                                                    displaySnackbar = true,
                                                    isLoading = false,
                                                    message = resultOf.message.toString()
                                                )
                                            }
                                        }
                                        else -> {
                                            print("another")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                is AuthUiAction.OnShowPasswordClick -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(showPassword = !currentUiState.showPassword)
                    }
                }

                is AuthUiAction.OnRepeatPasswordValueChange -> {
                    _uiState.update {
                        it.copy(repeatPassword = action.repeatPassword)
                    }
                }
                is AuthUiAction.GoToSignUp -> {
                    navigateToScreen("${GymMateScreens.AUTH_SCREEN}/${SIGN_UP}")
                }
            }
        }
    }

    fun prepareRequest(launchAnotherActivity: (GetCredentialRequest)-> Unit) {
        viewModelScope.launch {
            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            launchAnotherActivity(request)
        }
    }

    fun hideSnackBar() {
        viewModelScope.launch {
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    displaySnackbar = false,
                )
            }
        }
    }
    private fun AuthUiState.getRoute() = when (signPath) {
        is SignPath.SignIn -> {
            GymMateScreens.ONBOARDING_SCREEN
        }
        else -> {
            "${GymMateScreens.AUTH_SCREEN}/$SIGN_IN?${GymMateDestinationsArgs.emailArgs}=$email?$passwordArgs=$password"
        }
    }

    private fun AuthUiState.getResourceId() = when (signPath) {
        is SignPath.SignIn -> {
            loadingDataMsg
        }

        else -> {
            loadingCreatingNewDataMsg
        }
    }

    companion object {
        private const val SIGN_IN = "SIGN_IN"
        private const val SIGN_UP = "SIGN_UP"
        private const val initialRepeatPassword = ""
        private const val loadingDataMsg = "Loading data account"
        private const val loadingCreatingNewDataMsg = "Loading new data account"
        private val noRepeatPassword = null
    }
}