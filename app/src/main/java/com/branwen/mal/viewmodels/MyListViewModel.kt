package com.branwen.mal.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branwen.mal.data.repo.AnimeRepository
import com.branwen.mal.models.domain.MyAnimeListItem
import com.branwen.mal.models.domain.MyMangaListItem
import com.branwen.mal.utils.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the My List screen.
 *
 * This ViewModel is responsible for fetching and managing the user's anime list,
 * handling loading and refreshing states, and filtering the list based on status.
 *
 * @property repository The [AnimeRepository] used to fetch anime data.
 */
@HiltViewModel
class MyListViewModel @Inject constructor(
    private val repository: AnimeRepository,
) : ViewModel() {
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _selectedStatus = MutableStateFlow("all")
    val selectedStatus: StateFlow<String> = _selectedStatus

    private val _animeList = MutableStateFlow<List<MyAnimeListItem>>(emptyList())
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

    private val _mangaList = MutableStateFlow<List<MyMangaListItem>>(emptyList())
    val filteredMangaList: StateFlow<List<MyMangaListItem>> = combine(
        _mangaList, _selectedStatus
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
        observeLocal()
        fetchAnimeList(isInitial = true)
    }

    private fun observeLocal() {
        launchCatching(
            block = {
                repository.getAnimeListFlow().collect { list ->
                    _animeList.value = list
                }
            }
        )
    }

    private fun fetchAnimeList(isInitial: Boolean = false) {
        launchCatching(
            block = {
                if (isInitial) _loading.value = true
                repository.fetchAndCacheAnimeList()
            },
            post = {
                if (isInitial) _loading.value = false
                else _isRefreshing.value = false
            }
        )
    }

    fun onPullToRefresh() {
        _isRefreshing.value = true
        fetchAnimeList(isInitial = false)
    }

    fun onStatusSelected(status: String) {
        _selectedStatus.value = status
    }

    fun onProgressIncrement(animeItem: MyAnimeListItem) {
        launchCatching(
            block = {
                repository.incrementAnimeListStatus(animeItem)
            }
        )
    }
}