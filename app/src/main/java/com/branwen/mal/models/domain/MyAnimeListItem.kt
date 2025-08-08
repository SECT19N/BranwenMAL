package com.branwen.mal.models.domain

data class MyAnimeListItem(
    val id: Int,
    val title: String,
    val status: String,
    val imageUrl: String,
    val startSeason: String,
    val startYear: String,
    val numEpisodesWatched: Int,
    val totalEpisodes: Int?,
    val rating: Int
)