package com.rodcollab.gymmateapp.exercises.presentation.intent

import com.rodcollab.gymmateapp.core.data.model.BodyPart

sealed interface BodyPartsUiAction {
    data class OnBodyPart(val bodyPart: BodyPart) : BodyPartsUiAction

}