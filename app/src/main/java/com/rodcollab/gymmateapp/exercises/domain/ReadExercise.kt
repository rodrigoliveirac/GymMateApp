package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal

interface ReadExercise {
    operator fun invoke(exerciseId: String): ExerciseExternal
}