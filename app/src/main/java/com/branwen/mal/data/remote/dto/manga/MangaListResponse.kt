package com.branwen.mal.data.remote.dto.manga

import com.branwen.mal.data.remote.dto.AlternativeTitles
import com.branwen.mal.data.remote.dto.Genre
import com.branwen.mal.data.remote.dto.Paging
import com.branwen.mal.data.remote.dto.PictureSource
import com.branwen.mal.data.remote.dto.anime.AnimeListItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaListResponse(
    val data: List<MangaListData>,
    val paging: Paging?
)

data class MangaListData(
    val node: MangaNode,
    @param:Json(name = "list_status") val listStatus: MangaListStatus?,
    @param:Json(name = "relation_type_formatted") val relationTypeFormatted: String?,
    @param:Json(name = "num_recommendations") val numRecommendations: Int? = null
)

data class MangaNode(
    val id: Int,
    val title: String,
    @param:Json(name = "main_picture") val mainPicture: PictureSource,
    @param:Json(name = "alternative_titles") val alternativeTitles: AlternativeTitles?,
    @param:Json(name = "num_volumes") val numberOfVolumes: Int? = 0,
    @param:Json(name = "num_chapters") val numberOfChapters: Int? = 0,
    @param:Json(name = "start_date") val startDate: String?,
    @param:Json(name = "created_at") val createdAt: String?,
    @param:Json(name = "updated_at") val updatedAt: String?,
    @param:Json(name = "media_type") val mediaType: String?,
    val status: String?,
    val synopsis: String?,
    val mean: Float?,
    val rank: Int?,
    val popularity: Int?,
    @param:Json(name = "num_list_users") val numberOfListUsers: Int?,
    @param:Json(name = "num_scoring_users") val numberOfScoringUsers: Int?,
    val nsfw: String?,
    val genres: List<Genre>?,
    @param:Json(name = "my_list_status") val myListStatus: MangaListStatus?,
    val authors: List<Author>?,
    val pictures: List<PictureSource>?,
    val background: String?,
    @param:Json(name = "related_anime") val relatedAnime: List<AnimeListItem>?,
    @param:Json(name = "related_manga") val relatedManga: List<MangaListData>?,
    val serialization: List<Serialization>?
)

data class Serialization(
    val id: Int,
    val name: String
)

data class Author(
    val node: AuthorNode,
    val role: String
)

data class AuthorNode(
    val id: Int,
    @param:Json(name = "first_name") val firstName: String,
    @param:Json(name = "last_name") val lastName: String,
)

data class MangaListStatus(
    val status: String,
    @param:Json(name = "is_rereading") val isRereading: Boolean,
    @param:Json(name = "num_volumes_read") val numVolumesRead: Int,
    @param:Json(name = "num_chapters_read") val numChaptersRead: Int,
    val score: Int,
    @param:Json(name = "updated_at") val updatedAt: String
)