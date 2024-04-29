package com.rodcollab.gymmateapp.core.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rodcollab.gymmateapp.core.data.model.ExerciseLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM default_exercises")
    suspend fun getAll() : List<ExerciseLocal>
    @Upsert
    suspend fun upsert(exercises: List<ExerciseLocal>)

    @Query("SELECT * FROM default_exercises WHERE exercise_bodyPart =:bodyPart")
     suspend fun getByBodyPart(bodyPart: String) : List<ExerciseLocal>
}