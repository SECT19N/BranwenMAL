package com.branwen.mal.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga_list")
data class MangaListEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val status: String,
    val imageUrl: String,
    val startYear: String,
    val numChaptersRead: Int,
    val totalChapters: Int?,
    val rating: Int
)