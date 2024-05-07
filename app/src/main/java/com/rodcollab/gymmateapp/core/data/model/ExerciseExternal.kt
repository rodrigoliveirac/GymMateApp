package com.rodcollab.gymmateapp.core.data.model

import java.io.Serializable

data class ExerciseExternal(
    val uuid: String? = null,
    val name: String? = null,
    val image: String? = null,
    val bodyPart: String? = null,
    val notes: String? = null,
    val userExercise: Boolean? = null
) : Serializable