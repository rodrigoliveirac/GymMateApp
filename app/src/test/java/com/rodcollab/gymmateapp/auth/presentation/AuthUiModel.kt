package com.rodcollab.gymmateapp.auth.presentation

import com.rodcollab.gymmateapp.auth.domain.model.AuthDomain
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.auth.presentation.intent.AuthUiAction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FakeSavedStateHandle : HashMap<String, String>() {

    private val storage: HashMap<String, String> = hashMapOf()
    override fun get(key: String): String? {
        return storage[key]
    }

    override fun put(key: String, value: String): String? {
        return storage.put(key,value)
    }

}
class AuthUiModel(dispatcherProvider: CoroutineDispatcher, private val authDomain: AuthDomain, private val fakeSavedStateHandle: FakeSavedStateHandle) : CoroutineScope  {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onAuthUiAction(action: AuthUiAction, toMainScreen: ()-> Unit = {}) {
        launch {
            when(action) {
                is AuthUiAction.OnEmailValueChange -> {
                        updateWith(_uiState.value.copy(email = _uiState.value.email + action.email))
                }
                is AuthUiAction.OnPasswordValueChange -> {
                    updateWith(_uiState.value.copy(password = action.password))
                }
                is AuthUiAction.OnConfirmClick -> {
                    updateWith(_uiState.value.copy(isLoading = true))
                    _uiState.value.apply {
                        fakeSavedStateHandle[SIGN_PATH]?.let { signPath ->
                            authDomain.authenticate(
                                email = email,
                                password = password,
                                repeatPassword = repeatPassword,
                                signPath = SignPath.getSignPath(signPath),
                                onResult = { resultOf ->
                                    updateWith(_uiState.value.copy(isLoading = false))
                                    toMainScreen()
                                }
                            )
                        }
                    }
                }
                is AuthUiAction.OnShowPasswordClick -> {
                    launch {
                        _uiState.update {
                            it.copy(showPassword = !_uiState.value.showPassword)
                        }
                    }
                }
                is AuthUiAction.GoToSignUp -> {

                }
                is AuthUiAction.OnRepeatPasswordValueChange -> {
                    updateWith(_uiState.value.copy(repeatPassword = action.repeatPassword))
                }
            }
        }
    }

    companion object {
        const val SIGN_PATH = "SIGN_PATH"
    }
    private fun updateWith(newState: AuthUiState) {
        _uiState.value = newState
        println("new state $newState")
    }

    override val coroutineContext: CoroutineContext = dispatcherProvider
}