package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal

interface ExercisesByBP {
    suspend operator fun invoke(onResultOf: suspend (ResultOf<Map<BodyPart,List<ExerciseExternal>>>)-> Unit)
}