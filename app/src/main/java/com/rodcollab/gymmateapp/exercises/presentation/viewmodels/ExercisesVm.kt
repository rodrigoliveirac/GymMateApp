package com.rodcollab.gymmateapp.exercises.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.bodyPartArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateScreens
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import com.rodcollab.gymmateapp.exercises.presentation.intent.ExercisesUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExercisesUiState(
    val isLoading: Boolean = false,
    val exercises: List<ExerciseExternal> = listOf(),
    val bodyPart: String = ""
) {
    val exercisesTopBarTitle = getTopBarTitle()
    private fun getTopBarTitle(): String {
        var title = ""
        if (bodyPart.isNotEmpty()) {
            title = "$bodyPart exercises"
        }
        return title
    }
}

@HiltViewModel
class ExercisesVm @Inject constructor(
    private val domain: ExercisesDomain,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState = _uiState.asStateFlow()

    private val bodyPart: String = checkNotNull(savedStateHandle[bodyPartArgs])

    init {
        _uiState.update {
            it.copy(bodyPart = bodyPart)
        }
    }

    fun onUiActions(action: ExercisesUiAction, goTo: (String) -> Unit = {}) {
        viewModelScope.launch {
            when (action) {
                is ExercisesUiAction.OnNewExercise -> {
                    goTo("${GymMateScreens.ADD_OR_EDIT_EXERCISE_SCREEN}/ADD?${bodyPartArgs}=${bodyPart}?${GymMateDestinationsArgs.exerciseIdArgs}={${GymMateDestinationsArgs.exerciseIdArgs}}")
                }

                is ExercisesUiAction.UpdateExercises -> {
                    updateExercises()
                }

                is ExercisesUiAction.ExerciseDetails -> {
                    goTo("${GymMateScreens.EXERCISE_DETAILS}/${action.exerciseId}")
                }
            }
        }
    }

    private suspend fun updateExercises() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        _uiState.update {
            it.copy(exercises = domain.exercisesByBP(bodyPart), isLoading = false)
        }
    }
}