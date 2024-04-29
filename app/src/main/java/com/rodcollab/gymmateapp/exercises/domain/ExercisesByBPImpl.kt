package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExercisesByBPImpl @Inject constructor(private val exercises: ExercisesRepository) :
    ExercisesByBP {
    override suspend fun invoke(onResultOf: (ResultOf<Map<BodyPart, List<ExerciseExternal>>>) -> Unit) {
        try {
            val bodyPartWithExercises = hashMapOf<BodyPart, List<ExerciseExternal>>()

            exercises.getBodyParts { resultOf ->
                when (resultOf) {
                    is ResultOf.Success -> {
                        resultOf.value.map {
                            exercises.getExerciseByBodyPart(it.name) { result ->
                                when (result) {
                                    is ResultOf.Success -> {
                                        bodyPartWithExercises[it] = result.value

                                    }

                                    is ResultOf.Failure -> {
                                    }

                                    else -> {

                                    }
                                }
                            }
                        }
                    }

                    is ResultOf.Failure -> {
                    }

                    else -> {

                    }
                }
            }
            onResultOf(ResultOf.Success(bodyPartWithExercises))

        } catch (e: Exception) {
            onResultOf(ResultOf.Failure(e.message, e.cause))
        }
    }

    override suspend fun myExercises(
        bodyPart: String,
        onResultOf: suspend (ResultOf<List<ExerciseExternal>>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {

                    val bodyPartWithExercises = mutableListOf<ExerciseExternal>()

                    val callback: (List<ExerciseExternal>) -> Unit = {
                        launch(Dispatchers.IO + Job()) {
                            async {
                                onResultOf(ResultOf.Success(it))
                            }.await()
                        }
                    }
                    exercises.userExercises(bodyPart) { resultOf ->
                        when (resultOf) {
                            is ResultOf.Success -> {
                                callback(resultOf.value)
                            }

                            is ResultOf.Failure -> {

                            }

                            else -> {

                            }
                        }
                    }
                    bodyPartWithExercises
            } catch (e: Exception) {
                onResultOf(ResultOf.Failure(e.message, e.cause))

            }
        }
    }
}