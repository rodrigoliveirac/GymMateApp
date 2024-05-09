package com.rodcollab.gymmateapp.exercises.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import com.rodcollab.gymmateapp.exercises.presentation.intent.BodyPartsUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
data class BodyPartsUiState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val bodyParts: List<BodyPart> = listOf()
)

@HiltViewModel
class BodyPartsVm @Inject constructor(
    private val exercises: ExercisesDomain,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BodyPartsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            updateBodyParts()
        }
    }

    fun onUiActions(action: BodyPartsUiAction, goTo: (String) -> Unit = { }) {
        viewModelScope.launch {
            when (action) {
                is BodyPartsUiAction.OnBodyPart -> {
                    goTo("${GymMateScreens.EXERCISES}/${action.bodyPart.name}")
                }
            }
        }
    }

    private suspend fun updateBodyParts() {
        withContext(Dispatchers.Main) {
            _uiState.update {
                it.copy(isLoading = true)
            }
        }
        exercises.bodyParts { result ->
            when (result) {
                is ResultOf.Success -> {
                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(bodyParts = result.value, isLoading = false)
                        }
                    }
                }

                is ResultOf.Failure -> {
                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(message = result.message, isLoading = false)
                        }
                    }
                }

                else -> {

                }
            }
        }
    }

    fun hideSnackBar() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(message = null)
            }
        }
    }
}