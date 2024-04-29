package com.rodcollab.gymmateapp.exercises.domain.model

import com.rodcollab.gymmateapp.exercises.domain.AddOrEditExercise
import com.rodcollab.gymmateapp.exercises.domain.ExercisesByBP

data class ExercisesDomain(
    val exercises: ExercisesByBP,
    val addExercise: AddOrEditExercise
)
