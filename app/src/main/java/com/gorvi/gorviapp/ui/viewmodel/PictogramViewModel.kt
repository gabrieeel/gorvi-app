package com.gorvi.gorviapp.ui.viewmodel

import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorvi.gorviapp.data.Pictogram
import com.gorvi.gorviapp.data.PictogramRepository
import kotlinx.coroutines.launch

class PictogramViewModel(private val repository: PictogramRepository) : ViewModel() {

    val allPictograms: Flow<List<Pictogram>> = repository.getAllPictograms()

    fun insert(pictogram: Pictogram) = viewModelScope.launch {
        repository.insert(pictogram)
    }

    // Additional ViewModel logic as needed
}

