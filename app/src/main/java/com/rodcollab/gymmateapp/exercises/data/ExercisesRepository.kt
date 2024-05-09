package com.rodcollab.gymmateapp.exercises.data

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal

interface ExercisesRepository {
    suspend fun getBodyParts(onResult: suspend (ResultOf<List<BodyPart>>) -> Unit)
    suspend fun updateBPToExercises(
        onResult: suspend () -> Unit
    )

    suspend fun bodyPartToExercisesCache(): Map<String,List<ExerciseExternal>>

    suspend fun addOrEditExercise(
        document: String?,
        bodyPart: String,
        name: String,
        img: String?,
        notes: String,
        onResult: suspend (ResultOf<ExerciseExternal>) -> Unit
    )

    fun delete(document: String, bodyPart: String, onResult: (ResultOf<String>) -> Unit)

    fun getExerciseById(uuid: String): ExerciseExternal

    suspend fun getExerciseByBPCache(bodyPartId: String): List<ExerciseExternal>
}