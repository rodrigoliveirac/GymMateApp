package com.rodcollab.gymmateapp.exercises.data

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.Exercise

interface ExercisesRepository {
    suspend fun getBodyParts(onResult: suspend (ResultOf<List<BodyPart>>) -> Unit)
    suspend fun getExerciseByBodyPart(
        bodyPart: String,
        onResult: suspend (ResultOf<List<Exercise>>) -> Unit
    )
}