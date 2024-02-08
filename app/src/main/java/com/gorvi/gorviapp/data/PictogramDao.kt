package com.gorvi.gorviapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PictogramDao {
    @Insert
    suspend fun insert(pictogram: Pictogram)

    @Query("SELECT * FROM pictogram ORDER BY label")
    fun getAll(): Flow<List<Pictogram>>


    @Query("SELECT * FROM pictogram WHERE id = :id")
    fun getPictogramById(id: Int): Flow<Pictogram>

    @Delete
    suspend fun delete(pictogram: Pictogram)
}


