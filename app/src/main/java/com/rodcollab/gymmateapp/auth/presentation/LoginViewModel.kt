package com.rodcollab.gymmateapp.auth.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.auth.domain.model.AuthDomain
import com.rodcollab.gymmateapp.auth.domain.usecase.enums.SignPath
import com.rodcollab.gymmateapp.auth.presentation.intent.AuthUiAction
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
class LoginViewModel @Inject constructor(
    private val authDomain: AuthDomain,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onAuthUiAction(action: AuthUiAction, toMainScreen: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Default) {
            when(action) {
                is AuthUiAction.OnEmailValueChange -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(email = action.email)
                    }
                }
                is AuthUiAction.OnPasswordValueChange -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(email = action.password)
                    }
                }
                is AuthUiAction.OnConfirmClick -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(resultOf = ResultOf.Idle)
                    }
                    _uiState.value.apply {
                        savedStateHandle.get<String>(SIGN_PATH)?.let { signPath ->
                            authDomain.authenticate(
                                email = email,
                                password = password,
                                signPath = SignPath.valueOf(signPath),
                                onResult = { resultOf ->
                                    _uiState.update { currentUiState ->
                                        currentUiState.copy(resultOf = resultOf)
                                    }
                                    toMainScreen()
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
            }
        }
    }
    companion object {
        private val SIGN_PATH = "SIGN_PATH"
    }
}