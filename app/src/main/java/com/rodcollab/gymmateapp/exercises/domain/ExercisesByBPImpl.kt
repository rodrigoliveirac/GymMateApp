package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.Exercise
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import javax.inject.Inject

class ExercisesByBPImpl @Inject constructor(private val exercises: ExercisesRepository) :
    ExercisesByBP {
    override suspend fun invoke(onResult: (ResultOf<Map<BodyPart, List<Exercise>>>) -> Unit) {
        try {
            val bodyPartWithExercises = hashMapOf<BodyPart, List<Exercise>>()
            exercises.getBodyParts { resultOf ->
                when (resultOf) {
                    is ResultOf.Success -> {
                        resultOf.value.map { bp ->
                            exercises.getExerciseByBodyPart(bp.name) { resultOf ->
                                when (resultOf) {
                                    is ResultOf.Success -> {
                                        bodyPartWithExercises[bp] = resultOf.value
                                    }

                                    is ResultOf.Failure -> {
                                        onResult(
                                            ResultOf.Failure(
                                                resultOf.message,
                                                resultOf.throwable
                                            )
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        }
                        onResult(ResultOf.Success(bodyPartWithExercises))
                    }

                    is ResultOf.Failure -> {
                        onResult(ResultOf.Failure(resultOf.message, resultOf.throwable))
                    }

                    else -> {

                    }
                }
            }
        } catch (e: Exception) {
            onResult(ResultOf.Failure(e.message, e))
        }

    }
}