package com.rodcollab.gymmateapp.exercises.data

import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.dao.BodyPartDao
import com.rodcollab.gymmateapp.core.data.dao.ExerciseDao
import com.rodcollab.gymmateapp.core.data.model.BodyPart
import com.rodcollab.gymmateapp.core.data.model.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExercisesRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val bodyPartDao: BodyPartDao
) : ExercisesRepository {

    override suspend fun getBodyParts(onResult: suspend (ResultOf<List<BodyPart>>) -> Unit) {
        return withContext(Dispatchers.IO) {
            try {
                onResult(ResultOf.Success(bodyPartDao.getAll()))
            }catch (e:Exception) {
                onResult(ResultOf.Failure(e.message,e.cause))
            }
        }
    }

    override suspend fun getExerciseByBodyPart(bodyPart: String, onResult: suspend (ResultOf<List<Exercise>>) -> Unit) {
         return withContext(Dispatchers.IO) {
            try {
                onResult(ResultOf.Success(exerciseDao.getByBodyPart(bodyPart)))
            } catch (e:Exception) {
                onResult(ResultOf.Failure(e.message,e.cause))
            }
        }
    }
}