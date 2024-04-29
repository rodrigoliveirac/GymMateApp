package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import kotlinx.coroutines.flow.Flow

interface ExercisesByBP {
    suspend operator fun invoke(onResultOf: (ResultOf<Map<BodyPart,List<ExerciseExternal>>>)-> Unit)
    suspend fun myExercises(bodyPart: String,onResultOf: suspend (ResultOf<List<ExerciseExternal>>)-> Unit)
}