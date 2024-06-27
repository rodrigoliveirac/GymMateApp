package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal

interface AddOrEditExercise {
    suspend operator fun invoke(
        document: String?,
        bodyPart: String,
        name: String,
        img: String?,
        notes: String,
        onResult: suspend (ResultOf<ExerciseExternal>) -> Unit
    )
}