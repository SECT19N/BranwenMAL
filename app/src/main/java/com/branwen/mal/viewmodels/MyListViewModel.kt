package com.branwen.mal.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branwen.mal.data.repo.AnimeRepository
import com.branwen.mal.data.repo.MyAnimeListItem
import com.branwen.mal.models.AnimeListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class MyListUiState(
    val animeList: List<AnimeListItem> = emptyList(),
    val selectedStatus: String = "all",
    val filteredAnimeList: List<AnimeListItem> = emptyList()
)

@HiltViewModel
class MyListViewModel @Inject constructor(
    private val repository: AnimeRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyListUiState())
    val uiState: StateFlow<MyListUiState> = _uiState.asStateFlow()

    private val _animeList = MutableStateFlow<List<MyAnimeListItem>>(emptyList())
    val animeList: StateFlow<List<MyAnimeListItem>> = _animeList

    private val _selectedStatus = MutableStateFlow("all")
    val selectedStatus: StateFlow<String> = _selectedStatus

    val filteredAnimeList: StateFlow<List<MyAnimeListItem>> = combine(
        _animeList, _selectedStatus
    ) { fullList, status ->
        if (status == "all") {
            fullList
        } else {
            fullList.filter { it.status == status }
        }
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList()
    )

    private val _listSwitchChecked = mutableStateOf(true)
    val listSwitchChecked = _listSwitchChecked

    init {
        fetchAnimeList()
    }

    fun refreshAnimeList() {
        fetchAnimeList(forceRefresh = true)
    }
//
//    private fun fetchAnimeList(forceRefresh: Boolean = false) {
//        viewModelScope.launch(Dispatchers.IO) {
//            runCatching {
//                _animeList.value = repository.getAnimeList()
//            }.onFailure {
//                Log.e("MyListViewModel", "Error fetching anime list", it)
//            }
//        }
//    }

    private fun fetchAnimeList(forceRefresh: Boolean = false) = launchCatching {
        _animeList.value = repository.getAnimeList()
    }

    fun onStatusSelected(status: String) {
        _selectedStatus.value = status
    }
}

fun ViewModel.launchCatching(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
    runCatching {
        block()
    }.onFailure {
        Timber.e(it, "Error in launchCatching")
    }
}