package com.rodcollab.gymmateapp.exercises.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.core.ResultOf
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
    val isLoading: Boolean = false,
    val openDialog: Boolean = false,
    val exerciseExternal: ExerciseExternal? = null,
    val isUserExercise: Boolean = false,
    val idExerciseDeleted: String? = null
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
            it.copy(exerciseExternal = exercise, isUserExercise = exercise.userExercise ?: false)
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

                is ExerciseDetailsUiAction.OnDelete -> {
                    _uiState.update {
                        it.copy(openDialog = !it.openDialog)
                    }
                }

                is ExerciseDetailsUiAction.OnDeleteConfirmation -> {
                    when (action.confirm) {
                        false -> {
                            _uiState.update {
                                it.copy(openDialog = !it.openDialog)
                            }
                        }

                        else -> {

                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                             domain.deleteExercise(id = exerciseId, bodyPart = _uiState.value.exerciseExternal?.bodyPart!!) { result ->
                                    when (result) {
                                        is ResultOf.Success -> {
                                            _uiState.update {
                                                it.copy(openDialog = false, isLoading = false, idExerciseDeleted = exerciseId)
                                            }
                                        }
                                        is ResultOf.Failure -> {

                                        }

                                        else -> {

                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}