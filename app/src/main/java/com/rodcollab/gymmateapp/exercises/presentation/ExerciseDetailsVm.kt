package com.rodcollab.gymmateapp.exercises.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.exerciseIdArgs
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ExerciseUiState(
    val exerciseExternal: ExerciseExternal? = null
)

@HiltViewModel
class ExerciseDetailsVm @Inject constructor(
    private val readExercise: ExercisesDomain,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exerciseId: String = checkNotNull(savedStateHandle[exerciseIdArgs])

    private val _uiState = MutableStateFlow(ExerciseUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(exerciseExternal = readExercise.readExercise(exerciseId))
        }
    }

}