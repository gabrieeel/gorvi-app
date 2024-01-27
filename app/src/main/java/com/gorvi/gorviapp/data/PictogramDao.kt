package com.gorvi.gorviapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PictogramDao {
    @Insert
    suspend fun insert(pictogram: Pictogram)

    @Query("SELECT * FROM pictogram")
    fun getAll(): LiveData<List<Pictogram>>
}


