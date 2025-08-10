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
    @param:Json(name = "list_status") val listStatus: ListStatus?,
    val ranking: Ranking?,
    @param:Json(name = "num_recommendations") val numRecommendations: Int? = null,
    @param:Json(name = "relation_type_formatted") val relationTypeFormatted: String?,
)

data class AnimeNode(
    val id: Int,
    val title: String,
    @param:Json(name = "main_picture") val mainPicture: PictureSource,
    @param:Json(name = "alternative_titles") val alternativeTitles: AlternativeTitles? = null,
    @param:Json(name = "start_date") val startDate: String? = null,
    @param:Json(name = "end_date") val endDate: String? = null,
    val synopsis: String? = null,
    val mean: Double? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    @param:Json(name = "num_list_users") val numListUsers: Int? = null,
    @param:Json(name = "num_scoring_users") val numScoringUsers: Int? = null,
    val nsfw: String? = null,
    @param:Json(name = "created_at") val createdAt: String? = null,
    @param:Json(name = "updated_at") val updatedAt: String? = null,
    @param:Json(name = "media_type") val mediaType: String? = null,
    val status: String? = null,
    val genres: List<Genre>? = null,
    @param:Json(name = "my_list_status") val myListStatus: ListStatus? = null,
    @param:Json(name = "num_episodes") val numEpisodes: Int? = 0, // Causes a division by zero exception!
    @param:Json(name = "start_season") val startSeason: StartSeason = StartSeason(0, "N/A"),
    val broadcast: Broadcast? = null,
    val source: String? = null,
    @param:Json(name = "average_episode_duration") val averageEpisodeDuration: Int? = null,  // in seconds
    val rating: String? = null,
    val pictures: List<PictureSource>? = null,
    val background: String? = null,
    @param:Json(name = "related_anime") val relatedAnime: List<AnimeListItem>? = null, // TODO - add related manga
    val recommendations: List<AnimeListItem>? = null,
    val studios: List<Studio>? = null,
    val statistics: Statistics? = null
)

data class Statistics(
    val status: Status? = null,
    @param:Json(name = "num_list_users") val numListUsers: Int? = null
)

data class Status(
    val watching: String? = null,
    val completed: String? = null,
    @param:Json(name = "on_hold") val onHold: String? = null,
    val dropped: String? = null,
    @param:Json(name = "plan_to_watch") val planToWatch: String? = null
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
    @param:Json(name = "day_of_the_week") val dayOfTheWeek: String,
    @param:Json(name = "start_time") val startTime: String
)

data class StartSeason(
    val year: Int = 0,
    val season: String = "N/A"
)

data class ListStatus(
    val status: String,
    val score: Int,
    @param:Json(name = "num_episodes_watched") val numEpisodesWatched: Int = 0,
    @param:Json(name = "is_rewatching") val isRewatching: Boolean = false,
    @param:Json(name = "updated_at") val updatedAt: String
)

data class Ranking(
    val rank: Int
)