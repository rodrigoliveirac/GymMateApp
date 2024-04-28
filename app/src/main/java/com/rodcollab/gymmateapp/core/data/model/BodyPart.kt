package com.rodcollab.gymmateapp.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("bodyPart")
data class BodyPart(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo("bodyPart_name") val name: String
)