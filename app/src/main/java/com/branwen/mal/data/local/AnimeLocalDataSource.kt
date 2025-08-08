package com.branwen.mal.data.local

import com.branwen.mal.interfaces.AnimeDao
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.models.domain.MyAnimeListItem
import com.branwen.mal.models.entity.AnimeListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local data source for anime data.
 *
 * This class is responsible for interacting with the local database to retrieve and store anime data.
 *
 * @property dao The Data Access Object for anime data.
 */
class AnimeLocalDataSource(private val dao: AnimeDao) {
    /**
     * Get the anime list from the local database as a [Flow] of [MyAnimeListItem]
     */
    fun getAnimeListFlow(): Flow<List<MyAnimeListItem>> =
        dao.getAll()
            .map { entities ->
                entities.map { it.toDomain() }
            }

    /**
     * Save the anime list to the local database
     */
    suspend fun saveAnimeList(list: List<MyAnimeListItem>) {
        dao.clearAll()
        dao.insertAll(
            list = list.map { it.toEntity() }
        )
    }

    suspend fun updateWatchedEpisodes(id: Int, numEpisodesWatched: Int) {
        dao.updateWatchedEpisodes(id, numEpisodesWatched)
    }

    suspend fun getAnimeDetails(animeId: Int): AnimeNode? = null
    suspend fun saveAnimeDetails(node: AnimeNode) = Unit

    /**
     * Convert a [AnimeListEntity] to a [MyAnimeListItem]
     */
    private fun AnimeListEntity.toDomain() = MyAnimeListItem(
        id = id,
        title = title,
        status = status,
        imageUrl = imageUrl,
        startSeason = startSeason,
        startYear = startYear,
        numEpisodesWatched = numEpisodesWatched,
        totalEpisodes = totalEpisodes,
        rating = rating
    )

    /**
     * Convert a [MyAnimeListItem] to a [AnimeListEntity]
     */
    private fun MyAnimeListItem.toEntity() = AnimeListEntity(
        id = id,
        title = title,
        status = status,
        imageUrl = imageUrl,
        startSeason = startSeason,
        startYear = startYear,
        numEpisodesWatched = numEpisodesWatched,
        totalEpisodes = totalEpisodes,
        rating = rating
    )
}