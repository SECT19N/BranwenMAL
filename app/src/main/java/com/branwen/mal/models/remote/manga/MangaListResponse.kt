package com.branwen.mal.models.remote.manga

import com.branwen.mal.models.Paging
import com.branwen.mal.models.PictureSource
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaListResponse(
    val data: List<MangaListData>,
    val paging: Paging?
)

data class MangaListData(
    val node: MangaNode,
    val list_status: MangaListStatus
)

data class MangaNode(
    val id: Int,
    val title: String,
    val main_picture: PictureSource,
    val num_volumnes: Int,
    val num_chapters: Int,
    val start_date: String,
    val media_type: String
)

data class MangaListStatus(
    val status: String,
    val is_rereading: Boolean,
    val num_volumes_read: Int,
    val num_chapters_read: Int,
    val score: Int,
    val updated_at: String
)