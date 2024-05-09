package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository

interface ExercisesByBP {
    suspend operator fun invoke(bodyPart: String) : List<ExerciseExternal>
}

class ExercisesByBPImpl(private val exercisesRepository: ExercisesRepository) : ExercisesByBP {
    override suspend fun invoke(bodyPart: String): List<ExerciseExternal> = exercisesRepository.getExerciseByBPCache(bodyPart)

}