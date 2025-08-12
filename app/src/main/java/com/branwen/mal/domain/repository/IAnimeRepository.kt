package com.branwen.mal.domain.repository

import com.branwen.mal.domain.model.MyAnimeListItem
import kotlinx.coroutines.flow.Flow

interface IAnimeRepository {
    suspend fun getAnimeListCache(): Flow<List<MyAnimeListItem>>

    suspend fun fetchAndCacheAnimeList(): Unit
}