package com.rodcollab.gymmateapp.exercises.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.Exercise
import com.rodcollab.gymmateapp.exercises.domain.ExercisesByBP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BPExercisesViewModel @Inject constructor(private val exercises: ExercisesByBP) : ViewModel() {

    private val _uiState = MutableStateFlow(BPExercisesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            exercises { resultOf ->
                when(resultOf) {
                    is ResultOf.Success -> {
                        _uiState.update {
                            it.copy(bpWithExercises = resultOf.value,isLoading = false)
                        }
                    }
                    is ResultOf.Failure -> {
                        _uiState.update {
                            it.copy(message = resultOf.message)
                        }
                    }
                    else -> {}
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

data class BPExercisesUiState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val bpWithExercises: Map<BodyPart,Exercise> = mapOf()
)