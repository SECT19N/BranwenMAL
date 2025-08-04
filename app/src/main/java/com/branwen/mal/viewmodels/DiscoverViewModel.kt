package com.branwen.mal.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.branwen.mal.models.uistate.DiscoverUiState
import com.branwen.mal.utils.MalServiceBuilder.provideMalApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {
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
            val context = getApplication<Application>().applicationContext
            val prefs = context.getSharedPreferences("bran_mal_prefs", MODE_PRIVATE)
            val accessToken = prefs.getString("access_token", null)

            if (accessToken != null) {
                runCatching {
                    val apiService = provideMalApiService(accessToken)
                    val response = apiService.getAnimeRanking(rankingType = rankingType)
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
    }

    private fun fetchTenSuggestedAnime() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val prefs = context.getSharedPreferences("bran_mal_prefs", MODE_PRIVATE)
            val accessToken = prefs.getString("access_token", null)

            if (accessToken != null) {
                runCatching {
                    val apiService = provideMalApiService(accessToken)
                    val response = apiService.getAnimeSuggestions()
                    _discoverUIState.value.tenSuggestedAnime = response.data.sortedBy { it.ranking?.rank }
                }.onFailure {
                    Timber.e(it, "TopTen Suggested")
                }
            }
        }
    }
}