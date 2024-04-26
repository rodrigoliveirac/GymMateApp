package com.rodcollab.gymmateapp.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.auth.domain.model.AuthDomain
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.auth.presentation.intent.LoginUiAction
import com.rodcollab.gymmateapp.core.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authDomain: AuthDomain) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onLoginUiAction(action: LoginUiAction, toMainScreen: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Default) {
            when(action) {
                is LoginUiAction.OnEmailValueChange -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(email = action.email)
                    }
                }
                is LoginUiAction.OnPasswordValueChange -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(email = action.password)
                    }
                }
                is LoginUiAction.OnLoginClick -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(resultOf = ResultOf.Idle)
                    }
                    _uiState.value.apply {
                        authDomain.authenticate(
                            email = email,
                            password = password,
                            signPath = SignPath.SIGN_IN,
                            onResult = { resultOf ->
                                _uiState.update { currentUiState ->
                                    currentUiState.copy(resultOf = resultOf)
                                }
                                toMainScreen()
                            }
                        )
                    }
                }
                is LoginUiAction.OnShowPasswordClick -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(showPassword = !currentUiState.showPassword)
                    }
                }
            }
        }
    }
}