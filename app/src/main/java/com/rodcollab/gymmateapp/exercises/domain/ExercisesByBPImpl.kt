package com.rodcollab.gymmateapp.exercises.domain

import android.util.Log
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExercisesByBPImpl @Inject constructor(private val exercises: ExercisesRepository) :
    ExercisesByBP {
    override suspend fun invoke(onResultOf: suspend (ResultOf<Map<BodyPart, List<ExerciseExternal>>>) -> Unit) {
        withContext(Dispatchers.IO){
            try {
                val bodyPartWithExercises = hashMapOf<BodyPart, List<ExerciseExternal>>()
                exercises.getBodyParts { resultOf ->
                    when (resultOf) {
                        is ResultOf.Success -> {
                            withContext(Dispatchers.IO) {
                                resultOf.value.map {
                                    exercises.getExerciseByBodyPart(it.name) { result ->
                                        when (result) {
                                            is ResultOf.Success -> {
                                                bodyPartWithExercises[it] = result.value
                                                Log.d("fromRepository", result.value.toString())
                                            }
                                            is ResultOf.Failure -> {
                                                onResultOf(ResultOf.Failure(result.message, result.throwable))
                                            }
                                            else -> {
                                            }
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
    }

}