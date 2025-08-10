package com.branwen.mal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.branwen.mal.interfaces.MalApi
import com.branwen.mal.models.uistate.DiscoverUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val malApi: MalApi
) : ViewModel() {
    private val _discoverUIState = MutableStateFlow(DiscoverUiState())
    val discoverUIState: StateFlow<DiscoverUiState> = _discoverUIState

    init {
        fetchAnimeRanking("all")
        fetchAnimeRanking("airing")
        fetchAnimeRanking("upcoming")
        fetchAnimeRanking("movie")
        fetchTenSuggestedAnime()
    }

    private fun fetchAnimeRanking(rankingType: String) {
        viewModelScope.launch {
            runCatching {
                val response = malApi.getAnimeRanking(rankingType = rankingType)
                val sortedList = response.data.sortedBy { it.ranking?.rank }

                _discoverUIState.value = when (rankingType) {
                    "all" -> _discoverUIState.value.copy(topTenAnime = sortedList)
                    "airing" -> _discoverUIState.value.copy(topTenAiringAnime = sortedList)
                    "upcoming" -> _discoverUIState.value.copy(topTenUpcomingAnime = sortedList)
                    "movie" -> _discoverUIState.value.copy(topTenMovies = sortedList)
                    else -> _discoverUIState.value
                }
            }.onFailure {
                Timber.e(it, "Failed to fetch top anime for type: $rankingType")
            }
        }
    }

    private fun fetchTenSuggestedAnime() {
        viewModelScope.launch {
            runCatching {
                val response = malApi.getAnimeSuggestions()
                _discoverUIState.value.tenSuggestedAnime = response.data.sortedBy { it.ranking?.rank }
            }.onFailure {
                Timber.e(it, "TopTen Suggested")
            }
        }
    }
}