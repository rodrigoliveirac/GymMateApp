package com.rodcollab.gymmateapp.exercises.data

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.Exercise

interface ExercisesRepository {
    suspend fun getBodyParts(): List<BodyPart>
    suspend fun getExerciseByBodyPart(bodyPart: String, onResult: (ResultOf<Exercise>)-> Unit) : List<Exercise>
}