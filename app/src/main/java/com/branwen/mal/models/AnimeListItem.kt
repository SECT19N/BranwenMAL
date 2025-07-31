package com.branwen.mal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeListResponse(
    val data: List<AnimeListItem>,
    val paging: Paging?
)

data class AnimeListItem(
    val node: AnimeNode,
    @Json(name = "list_status") val listStatus: ListStatus?,
    val ranking: Ranking?,
    @Json(name = "num_recommendations") val numRecommendations: Int? = null
)

data class AnimeNode(
    val id: Int,
    val title: String,
    @Json(name = "main_picture") val mainPicture: Picture,
    @Json(name = "alternative_titles") val alternativeTitles: AlternativeTitles? = null,
    @Json(name = "start_date") val startDate: String? = null,
    @Json(name = "end_date") val endDate: String? = null,
    val synopsis: String? = null,
    val mean: Double? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    @Json(name = "num_list_users") val numListUsers: Int? = null,
    @Json(name = "num_scoring_users") val numScoringUsers: Int? = null,
    val nsfw: String? = null,
    @Json(name = "created_at") val createdAt: String? = null,
    @Json(name = "updated_at") val updatedAt: String? = null,
    @Json(name = "media_type") val mediaType: String? = null,
    val status: String? = null,
    val genres: List<Genre>? = null,
    @Json(name = "my_list_status") val myListStatus: ListStatus? = null,
    @Json(name = "num_episodes") val numEpisodes: Int? = 0, // Causes a division by zero exception!
    @Json(name = "start_season") val startSeason: StartSeason = StartSeason(0, "N/A"),
    val broadcast: Broadcast? = null,
    val source: String? = null,
    @Json(name = "average_episode_duration") val averageEpisodeDuration: Int? = null,  // in seconds
    val rating: String? = null,
    val pictures: List<Picture>? = null,
    val background: String? = null,
    @Json(name = "related_anime") val relatedAnime: List<AnimeListItem>? = null, // TODO - add related manga
    val recommendations: List<AnimeListItem>? = null,
    val studios: List<Studio>? = null,
    val statistics: Statistics? = null
)

data class Statistics(
    val status: Status? = null,
    @Json(name = "num_list_users") val numListUsers: Int? = null
)

data class Status(
    val watching: String? = null,
    val completed: String? = null,
    @Json(name = "on_hold") val onHold: String? = null,
    val dropped: String? = null,
    @Json(name = "plan_to_watch") val planToWatch: String? = null
)

fun Status.toIntMap(): Map<String, Int> = mapOf(
    "Watching" to (watching?.toIntOrNull() ?: 0),
    "Completed" to (completed?.toIntOrNull() ?: 0),
    "On Hold" to (onHold?.toIntOrNull() ?: 0),
    "Dropped" to (dropped?.toIntOrNull() ?: 0),
    "Plan to Watch" to (planToWatch?.toIntOrNull() ?: 0)
)

data class Studio(
    val id: Int,
    val name: String
)

data class Broadcast(
    @Json(name = "day_of_the_week") val dayOfTheWeek: String,
    @Json(name = "start_time") val startTime: String
)

data class Genre(
    val id: Int,
    val name: String
)

data class AlternativeTitles(
    val synonyms: List<String>? = null,
    val en: String? = null,
    val ja: String? = null
)

data class Picture(
    val medium: String,
    val large: String
)

data class StartSeason(
    val year: Int = 0,
    val season: String = "N/A"
)

data class ListStatus(
    val status: String,
    val score: Int,
    @Json(name = "num_episodes_watched") val numEpisodesWatched: Int = 0,
    @Json(name = "is_rewatching") val isRewatching: Boolean = false,
    @Json(name = "updated_at") val updatedAt: String
)

data class Ranking(
    val rank: Int
)

data class Paging(
    val next: String?,
)