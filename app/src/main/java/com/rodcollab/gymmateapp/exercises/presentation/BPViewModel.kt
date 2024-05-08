package com.rodcollab.gymmateapp.exercises.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.bodyPartArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.ADD_OR_EDIT_EXERCISE_SCREEN
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens.EXERCISE_DETAILS
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.exerciseIdArgs
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import com.rodcollab.gymmateapp.exercises.presentation.screens.DropOrInsert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BPExercisesViewModel @Inject constructor(
    private val exercises: ExercisesDomain,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(BPExercisesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            updateExercises()
        }
    }

    fun onUiActions(action: BPExercisesUiAction, goTo: (String) -> Unit = { }) {
        viewModelScope.launch {
            when (action) {
                is BPExercisesUiAction.OnBodyPart -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                    _uiState.update {
                        it.copy(
                            exercisesByBP = _uiState.value.bpWithExercises[action.bodyPart]!!,
                            isLoading = false
                        )
                    }
                    goTo(GymMateScreens.EXERCISES)
                }

                is BPExercisesUiAction.OnNewExercise -> {
                    goTo("$ADD_OR_EDIT_EXERCISE_SCREEN/ADD?$bodyPartArgs=${_uiState.value.exercisesByBP[0].bodyPart}?$exerciseIdArgs={$exerciseIdArgs}")
                }

                is BPExercisesUiAction.UpdateExercises -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                    when (action.operation) {
                        DropOrInsert.INSERT -> {
                            val newList = withContext(Dispatchers.IO) {
                                _uiState.value.exercisesByBP + action.newExercise!!
                            }
                            _uiState.update {
                                it.copy(exercisesByBP = newList, isLoading = false)
                            }
                        }

                        else -> {

                            val newList = withContext(Dispatchers.IO) {
                                val exercise =
                                    _uiState.value.exercisesByBP.find { it.uuid == action.idExercise }!!
                                val exercises = _uiState.value.exercisesByBP.toMutableList()
                                exercises.remove(exercise)
                                exercises
                            }

                            _uiState.update {
                                it.copy(exercisesByBP = newList, isLoading = false)
                            }
                        }
                    }

                }

                is BPExercisesUiAction.ExerciseDetails -> {
                    goTo("$EXERCISE_DETAILS/${action.exerciseExternal.uuid}")
                }
            }
        }
    }

    private suspend fun updateExercises() {
        withContext(Dispatchers.Main) {
            _uiState.update {
                it.copy(isLoading = true)
            }
        }
        exercises.exercises { result ->
            when (result) {
                is ResultOf.Success -> {
                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(bpWithExercises = result.value, isLoading = false)
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

sealed interface BPExercisesUiAction {
    data class OnBodyPart(val bodyPart: BodyPart) : BPExercisesUiAction

    data object OnNewExercise : BPExercisesUiAction
    data class UpdateExercises(
        val idExercise: String?,
        val newExercise: ExerciseExternal?,
        val operation: DropOrInsert
    ) : BPExercisesUiAction

    data class ExerciseDetails(val exerciseExternal: ExerciseExternal) : BPExercisesUiAction
}

data class BPExercisesUiState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val bpWithExercises: Map<BodyPart, List<ExerciseExternal>> = mapOf(),
    val exercisesByBP: List<ExerciseExternal> = listOf()
) {
    val exercisesTopBarTitle = getTopBarTitle()

    private fun getTopBarTitle(): String {
        var title = ""
        if (exercisesByBP.isNotEmpty()) {
            title = "${exercisesByBP[0].bodyPart} exercises"
        }
        return title
    }
}