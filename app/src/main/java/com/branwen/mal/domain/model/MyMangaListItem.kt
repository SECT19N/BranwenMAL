package com.branwen.mal.domain.model

data class MyMangaListItem(
    val id: Int,
    val title: String,
    val status: String,
    val imageUrl: String,
    val startYear: String,
    val numChaptersRead: Int,
    val totalChapters: Int?,
    val rating: Int
)