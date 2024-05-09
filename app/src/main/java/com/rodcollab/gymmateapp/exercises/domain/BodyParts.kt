package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart

interface BodyParts {
    suspend operator fun invoke(onResultOf: suspend (ResultOf<List<BodyPart>>)-> Unit)

}