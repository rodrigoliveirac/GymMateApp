package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.Exercise

interface ExercisesByBP {
    suspend operator fun invoke(onResult: (ResultOf<Map<BodyPart,List<Exercise>>>) -> Unit)
}