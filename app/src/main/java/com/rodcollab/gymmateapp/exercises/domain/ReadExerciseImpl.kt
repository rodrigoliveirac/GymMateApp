package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository

class ReadExerciseImpl(private val exercisesRepository:ExercisesRepository) : ReadExercise {

    override fun invoke(exerciseId: String): ExerciseExternal = exercisesRepository.getExerciseById(exerciseId)

}