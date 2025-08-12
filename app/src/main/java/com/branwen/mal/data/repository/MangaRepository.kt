package com.branwen.mal.data.repository

import com.branwen.mal.data.local.MangaLocalDataSource
import com.branwen.mal.data.remote.MangaRemoteDataSource
import com.branwen.mal.domain.model.MyMangaListItem
import com.branwen.mal.domain.repository.IMangaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val remote: MangaRemoteDataSource,
    private val local: MangaLocalDataSource
) : IMangaRepository {
    /**
     * Retrieves a flow of the user's manga list.
     *
     * This function first checks if there's a cached list in the local data source.
     * If the local list is empty or doesn't exist, it attempts to fetch the list
     * from the remote data source and then saves it to the local data source.
     * Regardless of whether a remote fetch occurred, it then emits all updates
     * from the local data source's manga list flow.
     *
     * If fetching from the remote source fails, an error is logged, and the function
     * proceeds to emit from the (potentially empty) local data source.
     *
     * @return A [Flow] of [List] of [MyMangaListItem] representing the user's manga list.
     */
    override suspend fun getMangaListCache(): Flow<List<MyMangaListItem>> = flow {
        val localFlow = local.getMangaListFlow().firstOrNull()

        if (localFlow.isNullOrEmpty()) {
            try {
                val remoteList = remote.getMangaList()
                local.saveMangaList(remoteList)
            } catch (e: Exception) {
                Timber.e("Failed to fetch manga list ${e.message}")
            }
        }

        emitAll(local.getMangaListFlow())
    }

    /**
     * Fetches the manga list from the remote data source and caches it in the local data source.
     *
     * This function attempts to retrieve the manga list from the remote source.
     * If the retrieval is successful and the list is not empty, it saves the list
     * to the local data source.
     * If an error occurs during the fetch operation, an error message is logged.
     */
    override suspend fun fetchAndCacheMangaList() {
        try {
            val list = remote.getMangaList()

            if (list.isEmpty()) return

            local.saveMangaList(list)
        } catch (e: Exception) {
            Timber.tag("MangaRepository").e("Failed to fetch manga list ${e.message}")
        }
    }
}