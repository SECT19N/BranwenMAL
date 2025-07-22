package com.branwen.mal.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.branwen.mal.viewmodels.AnimeDetailsViewModel

class AnimeDetailsViewModelFactory(
    private val application: Application,
    private val animeId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnimeDetailsViewModel(application, animeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}