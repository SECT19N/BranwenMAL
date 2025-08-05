package com.branwen.mal.models.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdatedAnimeResponse(
    val status: String,
    val score: Int,
    @Json(name = "num_episodes_watched") val numEpisodesWatched: Int,
    @Json(name = "is_rewatching") val isRewatching: Boolean,
    @Json(name = "updated_at") val updatedAt: String,
    val priority: Int,
    @Json(name = "num_times_rewatched") val numTimesRewatched: Int,
    @Json(name = "rewatch_value") val rewatchValue: Int,
    val tags: List<String>,
    val comments: String
)