package com.gorvi.gorviapp

import android.app.Application
import androidx.room.Room
import com.gorvi.gorviapp.data.AppDatabase
import com.gorvi.gorviapp.data.PictogramRepository

class GorviApp : Application() {

    lateinit var database: AppDatabase
    lateinit var repository: PictogramRepository

    override fun onCreate() {
        super.onCreate()

        // Initialize the Room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "gorvi-database"
        ).fallbackToDestructiveMigration() // Consider using this for development
            .build()

        // Initialize the repository
        repository = PictogramRepository(database.pictogramDao())
    }
}
