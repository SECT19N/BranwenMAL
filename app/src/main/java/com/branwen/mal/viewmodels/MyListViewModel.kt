package com.branwen.mal.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branwen.mal.data.repo.AnimeListRepository
import com.branwen.mal.models.AnimeListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MyListViewModel(
    private val repository: AnimeListRepository
) : ViewModel() {
    private val _animeList = MutableStateFlow<List<AnimeListItem>>(emptyList())
    val animeList: StateFlow<List<AnimeListItem>> = _animeList


    private val _selectedStatus = MutableStateFlow("all")
    val selectedStatus: StateFlow<String> = _selectedStatus

    val filteredAnimeList: StateFlow<List<AnimeListItem>> = combine(
        _animeList, _selectedStatus
    ) { fullList, status ->
        if (status == "all") {
            fullList
        } else {
            fullList.filter { it.listStatus?.status == status }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        fetchAnimeList()
    }

    fun refreshAnimeList() {
        fetchAnimeList(forceRefresh = true)
    }

    private fun fetchAnimeList(forceRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                _animeList.value = repository.getAnimeList()
            }.onFailure {
                Log.e("MyListViewModel", "Error fetching anime list", it)
            }
        }
    }

    fun onStatusSelected(status: String) {
        _selectedStatus.value = status
    }
}