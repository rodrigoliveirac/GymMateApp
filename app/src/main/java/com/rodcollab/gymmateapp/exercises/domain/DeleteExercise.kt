package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf

interface DeleteExercise {
    operator fun invoke(id:String,onResult: (ResultOf<String>) -> Unit)
}