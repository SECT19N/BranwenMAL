package com.branwen.mal.data.repo

import com.branwen.mal.data.local.MangaLocalDataSource
import com.branwen.mal.data.remote.MangaRemoteDataSource
import com.branwen.mal.models.domain.MyMangaListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class MangaRepository(
    private val remote: MangaRemoteDataSource,
    private val local: MangaLocalDataSource
) {
    fun getMangaListFlow(): Flow<List<MyMangaListItem>> = flow {
        val localFlow = local.getMangaListFlow().firstOrNull()
        emitAll(local.getMangaListFlow())

        if (localFlow.isNullOrEmpty()) {
            val remoteList = remote.getMangaList()
            local.saveMangaList(remoteList)
        }
    }

    suspend fun fetchAndCacheMangaList() {
        val list = remote.getMangaList()
        local.saveMangaList(list)
    }
}