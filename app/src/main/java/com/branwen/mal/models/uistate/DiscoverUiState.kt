package com.branwen.mal.models.uistate

import com.branwen.mal.models.AnimeListItem

data class DiscoverUiState(
    var topTenAnime: List<AnimeListItem> = emptyList(),
    var topTenAiringAnime: List<AnimeListItem> = emptyList(),
    var topTenUpcomingAnime: List<AnimeListItem> = emptyList(),
    var topTenMovies: List<AnimeListItem> = emptyList(),
    var tenSuggestedAnime: List<AnimeListItem> = emptyList()
)