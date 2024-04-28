package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.Exercise

interface ExercisesByBP {
    suspend operator fun invoke(bodyPart: String, onResult: (ResultOf<Exercise>) -> Unit)
}