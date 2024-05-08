package com.rodcollab.gymmateapp.exercises.domain.model

import com.rodcollab.gymmateapp.exercises.domain.AddOrEditExercise
import com.rodcollab.gymmateapp.exercises.domain.ExercisesByBP
import com.rodcollab.gymmateapp.exercises.domain.ReadExercise

data class ExercisesDomain(
    val readExercise: ReadExercise,
    val exercises: ExercisesByBP,
    val addOrEditExercise: AddOrEditExercise
)
