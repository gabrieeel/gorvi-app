package com.gorvi.gorviapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gorvi.gorviapp.data.PictogramRepository

class PictogramViewModelFactory(private val repository: PictogramRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PictogramViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PictogramViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

