package com.rodcollab.gymmateapp.core.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rodcollab.gymmateapp.core.data.model.Exercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM default_exercises")
    suspend fun getAll() : List<Exercise>
    @Upsert
    suspend fun upsert(exercises: List<Exercise>)

    @Query("SELECT * FROM default_exercises WHERE exercise_bodyPart =:bodyPart")
    suspend fun getByBodyPart(bodyPart: String) : List<Exercise>
}