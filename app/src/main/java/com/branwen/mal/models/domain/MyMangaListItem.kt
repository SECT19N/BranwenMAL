package com.branwen.mal.models.domain

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
