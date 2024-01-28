package com.gorvi.gorviapp.data

import kotlinx.coroutines.flow.Flow


class PictogramRepository(private val pictogramDao: PictogramDao) {

    // Return Flow from the DAO
    fun getAllPictograms(): Flow<List<Pictogram>> {
        return pictogramDao.getAll()
    }

    suspend fun insert(pictogram: Pictogram) {
        pictogramDao.insert(pictogram)
    }
}


