package com.rodcollab.gymmateapp.exercises.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.bodyPartArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.exerciseIdArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens.ADD_OR_EDIT_EXERCISE_SCREEN
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.core.data.model.ExerciseLocal
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
            exercises.exercises { result ->
                when(result) {
                    is ResultOf.Success -> {
                        _uiState.update {
                            it.copy(bpWithExercises = result.value,isLoading = false)
                        }
                    }
                    is ResultOf.Failure -> {
                        _uiState.update {
                            it.copy(message = result.message,isLoading = false)
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun onUiActions(action: BPExercisesUiAction, goTo: (String) -> Unit) {
        viewModelScope.launch {
            when(action) {
                is BPExercisesUiAction.OnBodyPart -> {
                    async {
                        exercises.exercises.myExercises(action.bodyPart.name) { result ->
                            when(result) {
                                is ResultOf.Success -> {
                                        _uiState.update {
                                            it.copy(exercisesByBP = result.value + _uiState.value.bpWithExercises[action.bodyPart]!!)
                                        }
                                }
                                is ResultOf.Failure -> {
                                    _uiState.update {
                                        it.copy(message = result.message,isLoading = false)
                                    }
                                }
                                else -> {

                                }
                            }
                        }
                    }.await()

                    goTo(GymMateScreens.EXERCISES)

                }
                is BPExercisesUiAction.OnNewExercise -> {
                    goTo("$ADD_OR_EDIT_EXERCISE_SCREEN/ADD?$bodyPartArgs=${_uiState.value.exercisesByBP[0].bodyPart}?${GymMateDestinationsArgs.nameExerciseArgs}=${GymMateDestinationsArgs.nameExerciseArgs}?${GymMateDestinationsArgs.imgUrlExerciseArgs}=${GymMateDestinationsArgs.imgUrlExerciseArgs}?${GymMateDestinationsArgs.notesExerciseArgs}=${GymMateDestinationsArgs.notesExerciseArgs}")
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

}

data class BPExercisesUiState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val bpWithExercises: Map<BodyPart,List<ExerciseExternal>> = mapOf(),
    val exercisesByBP: List<ExerciseExternal> = listOf()
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