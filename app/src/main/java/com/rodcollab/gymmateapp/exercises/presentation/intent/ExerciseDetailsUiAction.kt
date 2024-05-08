package com.rodcollab.gymmateapp.exercises.presentation.intent

sealed interface ExerciseDetailsUiAction {
    data object OnEdit : ExerciseDetailsUiAction
}