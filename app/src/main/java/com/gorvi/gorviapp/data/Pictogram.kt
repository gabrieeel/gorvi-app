package com.gorvi.gorviapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictogram")
data class Pictogram(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val label: String
)
