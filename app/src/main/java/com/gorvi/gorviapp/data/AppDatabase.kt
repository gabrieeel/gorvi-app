package com.gorvi.gorviapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Pictogram::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pictogramDao(): PictogramDao
}


