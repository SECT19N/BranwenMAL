package com.branwen.mal.domain.repository

import com.branwen.mal.domain.model.MyMangaListItem
import kotlinx.coroutines.flow.Flow

interface IMangaRepository {
    suspend fun getMangaListCache(): Flow<List<MyMangaListItem>>

    suspend fun fetchAndCacheMangaList(): Unit
}