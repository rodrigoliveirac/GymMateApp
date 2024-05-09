package com.rodcollab.gymmateapp.exercises.domain

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.exercises.data.ExercisesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BodyPartsDomainImpl @Inject constructor(private val exercises: ExercisesRepository) : BodyParts {

    override suspend fun invoke(onResultOf: suspend (ResultOf<List<BodyPart>>) -> Unit) {
        withContext(Dispatchers.IO){
            try {
                if(exercises.bodyPartToExercisesCache().isEmpty()) {
                    updateCache(onResultOf)
                } else {
                    exercises.getBodyParts(onResultOf)
                }
            } catch (e: Exception) {
                onResultOf(ResultOf.Failure(e.message, e.cause))
            }
        }
    }

    private suspend fun updateCache(onResultOf: suspend (ResultOf<List<BodyPart>>) -> Unit) {
        exercises.getBodyParts { resultOf ->
            when (resultOf) {
                is ResultOf.Success -> {
                    withContext(Dispatchers.IO) {
                        exercises.updateBPToExercises {
                            onResultOf(resultOf)
                        }
                    }
                }

                is ResultOf.Failure -> {
                }

                else -> {
                }
            }
        }
    }

}