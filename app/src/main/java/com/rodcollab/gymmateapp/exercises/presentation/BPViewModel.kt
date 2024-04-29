package com.rodcollab.gymmateapp.exercises.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.Exercise
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BPExercisesViewModel @Inject constructor(private val exercises: ExercisesDomain) : ViewModel() {

    private val _uiState = MutableStateFlow(BPExercisesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            exercises.exercises { resultOf ->
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

    fun onUiActions(action: BPExercisesUiAction, goTo: () -> Unit) {
        viewModelScope.launch {
            when(action) {
                is BPExercisesUiAction.OnBodyPart -> {
                    _uiState.update {
                        it.copy(exercisesByBP = _uiState.value.bpWithExercises[action.bodyPart]!!)
                    }
                    goTo()
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

sealed interface BPExercisesUiAction {
    data class OnBodyPart(val bodyPart: BodyPart) : BPExercisesUiAction
}

data class BPExercisesUiState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val bpWithExercises: Map<BodyPart,List<Exercise>> = mapOf(),
    val exercisesByBP: List<Exercise> = listOf()
) {
    val exercisesTopBarTitle = getTopBarTitle()

    private fun getTopBarTitle() : String {
        var title = ""
        if(exercisesByBP.isNotEmpty()) {
            title = "${exercisesByBP[0].bodyPart} exercises"
        }
        return title
    }
}