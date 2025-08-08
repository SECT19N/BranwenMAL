package com.branwen.mal.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_list")
data class AnimeListEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val status: String,
    val imageUrl: String,
    val startSeason: String,
    val startYear: String,
    val numEpisodesWatched: Int,
    val totalEpisodes: Int?,
    val rating: Int
)