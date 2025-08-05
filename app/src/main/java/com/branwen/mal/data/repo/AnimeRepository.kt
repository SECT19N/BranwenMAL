package com.branwen.mal.data.repo

import com.branwen.mal.data.local.AnimeLocalDataSource
import com.branwen.mal.data.remote.AnimeRemoteDataSource
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.models.domain.MyAnimeListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * Repository for handling anime data, acting as a single source of truth.
 *
 * This class abstracts the data sources (remote and local) and provides a clean API
 * for accessing and managing anime information. It handles fetching data from the remote
 * source, caching it locally, and retrieving it from the local cache when appropriate.
 *
 * @param remote The remote data source for fetching anime data from an external API.
 * @param local The local data source for storing and retrieving cached anime data.
 */
class AnimeRepository(
    private val remote: AnimeRemoteDataSource,
    private val local: AnimeLocalDataSource
) {
    fun getAnimeListFlow(): Flow<List<MyAnimeListItem>> = flow {
        val localFlow = local.getAnimeListFlow().firstOrNull()
        emitAll(local.getAnimeListFlow()) // always observe local

        if (localFlow.isNullOrEmpty()) {
            val remoteList = remote.getAnimeList()
            local.saveAnimeList(remoteList)
        }
    }

    suspend fun fetchAndCacheAnimeList() {
        val list = remote.getAnimeList()
        local.saveAnimeList(list)
    }

    /**
     * Increments the watched status of an anime item both remotely and locally.
     *
     * This function first attempts to update the anime's status on the remote server.
     * If the remote update is successful, it then updates the watched episodes count
     * in the local database. If the remote update fails, an error is logged.
     *
     * @param animeItem The [MyAnimeListItem] whose status needs to be incremented.
     */
    suspend fun incrementAnimeListStatus(animeItem: MyAnimeListItem) {
        runCatching {
            remote.incrementAnimeListStatus(animeItem)
        }.onSuccess { remoteResponse ->
            local.updateWatchedEpisodes(animeItem.id, remoteResponse.numEpisodesWatched)
        }.onFailure {
            Timber.e(it, "Failed to update remote")
        }
    }

    suspend fun getAnimeDetails(animeId: Int): AnimeNode {
        return runCatching {
            remote.getAnimeDetails(animeId)
        }.onSuccess {
            local.saveAnimeDetails(it)
        }.getOrElse {
            local.getAnimeDetails(animeId) ?: error("No details cached")
        }
    }
}