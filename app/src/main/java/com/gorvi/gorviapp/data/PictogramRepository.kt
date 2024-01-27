package com.gorvi.gorviapp.data

import androidx.lifecycle.LiveData

class PictogramRepository(private val pictogramDao: PictogramDao) {

    // Function to insert a new pictogram
    suspend fun insert(pictogram: Pictogram) {
        pictogramDao.insert(pictogram)
    }

    // Function to retrieve all pictograms
    fun getAllPictograms(): LiveData<List<Pictogram>> {
        return pictogramDao.getAll()
    }

    // Additional methods as needed
}

