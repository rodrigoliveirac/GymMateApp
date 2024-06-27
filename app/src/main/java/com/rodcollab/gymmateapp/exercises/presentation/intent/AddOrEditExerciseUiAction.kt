package com.rodcollab.gymmateapp.exercises.presentation.intent

sealed interface AddOrEditExerciseUiAction {
    data class OnNameChange(val name: String) : AddOrEditExerciseUiAction
    data class OnImgBitmapChange(val data: Any?, val uploadFile: (String) -> Unit) :
        AddOrEditExerciseUiAction

    data class OnNotesChange(val notes: String) : AddOrEditExerciseUiAction
    data object OnDelete : AddOrEditExerciseUiAction
    data object OnConfirm : AddOrEditExerciseUiAction
}