package com.rodcollab.gymmateapp.core.data.model

data class ExerciseExternal(
    val uuid: String,
    val name: String,
    val image: String?,
    val bodyPart: String,
    val notes: String,
    val userExercise: Boolean
)