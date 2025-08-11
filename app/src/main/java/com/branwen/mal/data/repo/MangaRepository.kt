package com.branwen.mal.data.repo

import com.branwen.mal.data.local.MangaLocalDataSource
import com.branwen.mal.data.remote.MangaRemoteDataSource
import com.branwen.mal.models.domain.MyMangaListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class MangaRepository(
    private val remote: MangaRemoteDataSource,
    private val local: MangaLocalDataSource
) {
    fun getMangaListFlow(): Flow<List<MyMangaListItem>> = flow {
        val localFlow = local.getMangaListFlow().firstOrNull()

        if (localFlow.isNullOrEmpty()) {
            try {
                val remoteList = remote.getMangaList()
                local.saveMangaList(remoteList)
            } catch (e: Exception) {
                Timber.tag("MangaRepository").e("Failed to fetch manga list ${e.message}")
            }
        }

        emitAll(local.getMangaListFlow())
    }

    suspend fun fetchAndCacheMangaList() {
        try {
            val list = remote.getMangaList()
            local.saveMangaList(list)
        } catch (e: Exception) {
            Timber.tag("MangaRepository").e("Failed to fetch manga list ${e.message}")
        }
    }
}