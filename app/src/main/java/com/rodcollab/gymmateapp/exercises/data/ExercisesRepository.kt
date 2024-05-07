package com.rodcollab.gymmateapp.exercises.data

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal

interface ExercisesRepository {
    suspend fun getBodyParts(onResult: suspend (ResultOf<List<BodyPart>>) -> Unit)
    suspend fun getExerciseByBodyPart(
        bodyPart: String, onResult: suspend (ResultOf<List<ExerciseExternal>>) -> Unit
    )

    suspend fun addOrEditExercise(
        document: String?,
        bodyPart: String,
        name: String,
        img: String?,
        notes: String,
        onResult: (ResultOf<ExerciseExternal>) -> Unit
    )

    suspend fun delete(document: String, onResult: (ResultOf<String>) -> Unit)
}