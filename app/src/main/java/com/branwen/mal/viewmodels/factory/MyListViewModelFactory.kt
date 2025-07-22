package com.branwen.mal.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.branwen.mal.data.repo.AnimeListRepository
import com.branwen.mal.viewmodels.MyListViewModel

class MyListViewModelFactory(
    private val repository: AnimeListRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyListViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}