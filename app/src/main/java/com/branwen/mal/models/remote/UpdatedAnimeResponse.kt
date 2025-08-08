package com.branwen.mal.models.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdatedAnimeResponse(
    val status: String,
    val score: Int,
    @param:Json(name = "num_episodes_watched") val numEpisodesWatched: Int,
    @param:Json(name = "is_rewatching") val isRewatching: Boolean,
    @param:Json(name = "updated_at") val updatedAt: String,
    val priority: Int,
    @param:Json(name = "num_times_rewatched") val numTimesRewatched: Int,
    @param:Json(name = "rewatch_value") val rewatchValue: Int,
    val tags: List<String>,
    val comments: String
)