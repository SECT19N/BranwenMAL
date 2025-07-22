package com.branwen.mal.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.branwen.mal.models.AnimeListItem
import com.branwen.mal.utils.MalServiceBuilder.provideMalApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class DiscoverUIState(
    val topTenAnime: List<AnimeListItem> = emptyList(),
    val topTenAiringAnime: List<AnimeListItem> = emptyList(),
    val topTenUpcomingAnime: List<AnimeListItem> = emptyList(),
    val topTenMovies: List<AnimeListItem> = emptyList(),
    val tenSuggestedAnime: List<AnimeListItem> = emptyList()
)

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {
    private val _topTenAnime = MutableStateFlow<List<AnimeListItem>>(emptyList())
    private val _topTenAiringAnime = MutableStateFlow<List<AnimeListItem>>(emptyList())
    private val _topTenUpcomingAnime = MutableStateFlow<List<AnimeListItem>>(emptyList())
    private val _topTenMovies = MutableStateFlow<List<AnimeListItem>>(emptyList())
    private val _tenSuggestedAnime = MutableStateFlow<List<AnimeListItem>>(emptyList())

    val topTenAnime: StateFlow<List<AnimeListItem>> = _topTenAnime
    val topTenAiringAnime: StateFlow<List<AnimeListItem>> = _topTenAiringAnime
    val topTenUpcomingAnime: StateFlow<List<AnimeListItem>> = _topTenUpcomingAnime
    val topTenMovies: StateFlow<List<AnimeListItem>> = _topTenMovies
    val tenSuggestedAnime: StateFlow<List<AnimeListItem>> = _tenSuggestedAnime


    private val _discoverUIState = MutableStateFlow(DiscoverUIState())

    val discoverUIState: StateFlow<DiscoverUIState> = _discoverUIState

    init {
        fetchAnimeRanking("all", _topTenAnime)
        fetchAnimeRanking("airing", _topTenAiringAnime)
        fetchAnimeRanking("upcoming", _topTenUpcomingAnime)
        fetchAnimeRanking("movie", _topTenMovies)
        fetchTenSuggestedAnime()
    }

    private fun fetchAnimeRanking(
        rankingType: String,
        animeList: MutableStateFlow<List<AnimeListItem>>
    ) {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val prefs = context.getSharedPreferences("bran_mal_prefs", MODE_PRIVATE)
            val accessToken = prefs.getString("access_token", null)

            if (accessToken != null) {
                try {
                    val apiService = provideMalApiService(accessToken)
                    val response = apiService.getAnimeRanking(rankingType = rankingType)
                    animeList.value = response.data.sortedBy { it.ranking?.rank }
                } catch (e: Exception) {
                    Log.e("TopTen${rankingType.replaceFirstChar { it.uppercase() }} Exception", "${e.message}")
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
                try {
                    val apiService = provideMalApiService(accessToken)
                    val response = apiService.getAnimeSuggestions()
                    _tenSuggestedAnime.value = response.data.sortedBy { it.ranking?.rank }
                } catch (e: Exception) {
                    Log.e("TopTenMovies Exception", "${e.message}")
                }
            }
        }
    }
}