package com.rodcollab.gymmateapp.core.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rodcollab.gymmateapp.core.data.model.Exercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM default_exercises")
    fun getAll() : List<Exercise>
    @Upsert
    fun upsert(exercises: List<Exercise>)
}