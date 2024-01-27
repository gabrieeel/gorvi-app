package com.gorvi.gorviapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorvi.gorviapp.data.Pictogram
import com.gorvi.gorviapp.data.PictogramRepository
import kotlinx.coroutines.launch

class PictogramViewModel(private val repository: PictogramRepository) : ViewModel() {

    val allPictograms: LiveData<List<Pictogram>> = repository.getAllPictograms()

    fun insert(pictogram: Pictogram) = viewModelScope.launch {
        repository.insert(pictogram)
    }

    // Additional ViewModel logic as needed
}

