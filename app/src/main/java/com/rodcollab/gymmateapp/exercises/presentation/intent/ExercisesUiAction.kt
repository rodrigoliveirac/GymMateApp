package com.rodcollab.gymmateapp.exercises.presentation.intent

sealed interface ExercisesUiAction {
    data object OnNewExercise : ExercisesUiAction
    data object UpdateExercises : ExercisesUiAction

    data class ExerciseDetails(val exerciseId: String) : ExercisesUiAction
}