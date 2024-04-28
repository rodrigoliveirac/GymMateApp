package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.Exercise
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import javax.inject.Inject

class ExercisesByBPImpl @Inject constructor(private val exercises: ExercisesRepository) : ExercisesByBP {
    override suspend fun invoke(bodyPart: String, onResult: (ResultOf<Exercise>) -> Unit) {
        exercises.getExerciseByBodyPart(bodyPart, onResult)
    }
}