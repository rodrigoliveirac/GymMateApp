package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository

class DeleteExerciseImpl(private val exercisesRepository: ExercisesRepository) : DeleteExercise {
    override fun invoke(
        id: String,
        bodyPart: String,
        onResult: (ResultOf<String>) -> Unit
    ) {
        exercisesRepository.delete(document = id, bodyPart = bodyPart, onResult = onResult)
    }
}