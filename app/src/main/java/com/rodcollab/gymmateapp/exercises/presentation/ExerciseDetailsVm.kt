package com.rodcollab.gymmateapp.exercises.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.exerciseIdArgs
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import com.rodcollab.gymmateapp.exercises.presentation.intent.ExerciseDetailsUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseUiState(
    val exerciseExternal: ExerciseExternal? = null,
    val isEditable: Boolean = false
)

@HiltViewModel
class ExerciseDetailsVm @Inject constructor(
    private val domain: ExercisesDomain,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exerciseId: String = checkNotNull(savedStateHandle[exerciseIdArgs])

    private val _uiState = MutableStateFlow(ExerciseUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            val exercise = domain.readExercise(exerciseId)
            it.copy(exerciseExternal = exercise, isEditable = exercise.userExercise ?: false)
        }
    }

    fun toAction(action: ExerciseDetailsUiAction, goTo: (String) -> Unit = { }) {
        viewModelScope.launch {
            when (action) {
                is ExerciseDetailsUiAction.OnEdit -> {
                    goTo("${GymMateScreens.ADD_OR_EDIT_EXERCISE_SCREEN}/EDIT?${GymMateDestinationsArgs.bodyPartArgs}=${_uiState.value.exerciseExternal?.bodyPart}?${exerciseIdArgs}=${_uiState.value.exerciseExternal?.uuid}")
                }

                is ExerciseDetailsUiAction.OnUpdate -> {
                    _uiState.update {
                        it.copy(exerciseExternal = action.exercise)
                    }
                }
            }
        }
    }
}