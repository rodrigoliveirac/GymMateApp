package com.rodcollab.gymmateapp.exercises.presentation.intent

import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal

sealed interface ExerciseDetailsUiAction {
    data object OnEdit : ExerciseDetailsUiAction
    data class OnUpdate(val exercise: ExerciseExternal): ExerciseDetailsUiAction

    data object OnDelete : ExerciseDetailsUiAction

    data class OnDeleteConfirmation(val confirm: Boolean) : ExerciseDetailsUiAction
}