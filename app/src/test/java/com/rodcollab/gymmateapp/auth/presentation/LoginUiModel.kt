package com.rodcollab.gymmateapp.auth.presentation

import com.rodcollab.gymmateapp.auth.domain.model.AuthDomain
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.auth.presentation.intent.LoginUiAction
import com.rodcollab.gymmateapp.core.ResultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginUiModel(dispatcherProvider: CoroutineDispatcher, private val authDomain: AuthDomain) : CoroutineScope  {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onLoginUiAction(action: LoginUiAction, toMainScreen: ()-> Unit = {}) {
        launch {
            when(action) {
                is LoginUiAction.OnEmailValueChange -> {
                        updateWith(_uiState.value.copy(email = _uiState.value.email + action.email))
                }
                is LoginUiAction.OnPasswordValueChange -> {
                    updateWith(_uiState.value.copy(password = action.password))
                }
                is LoginUiAction.OnLoginClick -> {
                    launch {
                        updateWith(_uiState.value.copy(resultOf = ResultOf.Idle))
                        _uiState.value.apply {
                            authDomain.authenticate(
                                email = email,
                                password = password,
                                signPath = SignPath.SIGN_IN,
                                onResult = { resultOf ->
                                    updateWith(_uiState.value.copy(resultOf = resultOf))
                                    toMainScreen()
                                }
                            )
                        }
                    }
                }
                is LoginUiAction.OnShowPasswordClick -> {
                    launch {
                        _uiState.update {
                            it.copy(showPassword = !_uiState.value.showPassword)
                        }
                    }
                }
            }
        }
    }

    private fun updateWith(newState: LoginUiState) {
        _uiState.value = newState
        println("new state $newState")
    }

    override val coroutineContext: CoroutineContext = dispatcherProvider
}