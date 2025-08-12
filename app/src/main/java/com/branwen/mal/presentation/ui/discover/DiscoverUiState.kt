package com.branwen.mal.presentation.ui.discover

import com.branwen.mal.data.remote.dto.anime.AnimeListItem

data class DiscoverUiState(
    var topTenAnime: List<AnimeListItem> = emptyList(),
    var topTenAiringAnime: List<AnimeListItem> = emptyList(),
    var topTenUpcomingAnime: List<AnimeListItem> = emptyList(),
    var topTenMovies: List<AnimeListItem> = emptyList(),
    var tenSuggestedAnime: List<AnimeListItem> = emptyList()
)