package com.rodcollab.gymmateapp.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("default_exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo("exercise_name") val name: String,
    @ColumnInfo("exercise_image") val image: String,
    @ColumnInfo("exercise_bodyPart") val bodyPart: String,
    @ColumnInfo("exercise_notes") val notes: String
)