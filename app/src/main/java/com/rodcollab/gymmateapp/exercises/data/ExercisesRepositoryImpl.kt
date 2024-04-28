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

    override suspend fun getBodyParts(): List<BodyPart> {
        return withContext(Dispatchers.IO) {
            bodyPartDao.getAll()
        }
    }

    override suspend fun getExerciseByBodyPart(bodyPart: String, onResult: (ResultOf<Exercise>)-> Unit): List<Exercise> {
         return withContext(Dispatchers.IO) {
            exerciseDao.getByBodyPart(bodyPart)
        }
    }
}