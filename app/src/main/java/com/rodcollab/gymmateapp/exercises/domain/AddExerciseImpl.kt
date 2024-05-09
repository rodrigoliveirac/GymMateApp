package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddExerciseImpl(
    private val exercisesRepository: ExercisesRepository
) : AddOrEditExercise {
    override suspend fun invoke(
        document: String?,
        bodyPart: String,
        name: String,
        img: String?,
        notes: String,
        onResult: suspend (ResultOf<ExerciseExternal>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            exercisesRepository.addOrEditExercise(
                document,
                bodyPart,
                name,
                img,
                notes,
                onResult
            )
        }
    }
}