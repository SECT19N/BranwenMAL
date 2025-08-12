package com.branwen.mal.models.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdatedMangaResponse(
    val status: String,
    @param:Json(name = "is_rereading") val isRereading: Boolean,
    @param:Json(name = "num_volumes_read") val numVolumesRead: Int,
    @param:Json(name = "num_chapters_read") val numChaptersRead: Int,
    val score: Int,
    @param:Json(name = "updated_at") val updatedAt: String,
    val priority: Int,
    @param:Json(name = "num_times_reread") val numTimesReread: Int,
    @param:Json(name = "reread_value") val rereadValue: Int,
    val tags: List<String>,
    val comments: String
)