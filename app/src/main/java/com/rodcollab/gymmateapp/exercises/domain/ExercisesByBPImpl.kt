package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.Exercise
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import javax.inject.Inject

class ExercisesByBPImpl @Inject constructor(private val exercises: ExercisesRepository) : ExercisesByBP {
    override suspend fun invoke(bodyPart: String, onResult: (ResultOf<Map<BodyPart,Exercise>>) -> Unit) {
        try {
            val bodyPartWithExercises = hashMapOf<BodyPart,Exercise>()
            exercises.getBodyParts().map { bp ->
                exercises.getExerciseByBodyPart(bp.name) { resultOf ->
                    when(resultOf) {
                        is ResultOf.Success -> {
                            val exercise = Exercise(
                                id = resultOf.value.id,
                                name = resultOf.value.name,
                                image = resultOf.value.image,
                                bodyPart = resultOf.value.bodyPart,
                                notes = resultOf.value.notes
                            )
                            bodyPartWithExercises[bp] = exercise
                        }
                        is ResultOf.Failure -> {
                            onResult(ResultOf.Failure(resultOf.message,resultOf.throwable))
                        }
                        else -> {

                        }
                    }
                }
            }
            onResult(ResultOf.Success(bodyPartWithExercises))
        } catch (e:Exception) {
            onResult(ResultOf.Failure(e.message,e))
        }
    }
}