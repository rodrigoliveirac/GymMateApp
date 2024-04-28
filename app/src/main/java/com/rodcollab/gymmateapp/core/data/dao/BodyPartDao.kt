package com.rodcollab.gymmateapp.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.rodcollab.gymmateapp.core.data.model.BodyPart

@Dao
interface BodyPartDao {
    @Insert
    fun insert(bodyPart: BodyPart)
    @Query("SELECT * FROM bodyPart")
    fun getAll() : List<BodyPart>
    @Upsert
    fun upsert(bodyParts: List<BodyPart>)
}