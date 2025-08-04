package com.branwen.mal.data.remote

import android.content.SharedPreferences
import com.branwen.mal.models.AnimeListItem
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.models.Picture
import com.branwen.mal.models.domain.MyAnimeListItem
import com.branwen.mal.utils.MalServiceBuilder

/**
 * Fetches anime data from the MyAnimeList (MAL) API.
 *
 * This class is responsible for making network requests to the MAL API
 * to retrieve user-specific anime lists and details for individual anime series.
 * It uses [SharedPreferences] to access the user's access token for authentication.
 *
 * @property sharedPreferences An instance of [SharedPreferences] used to retrieve the MAL access token.
 */
class AnimeRemoteDataSource(
    private val sharedPreferences: SharedPreferences
) {
    private val statusOrder = listOf("watching", "completed", "on_hold", "dropped", "plan_to_watch").withIndex()
        .associate { it.value to it.index }

    /**
     * Get the anime list from the MAL API as a [List] of [MyAnimeListItem]
     */
    suspend fun getAnimeList(): List<MyAnimeListItem> {
        val token = sharedPreferences.getString("access_token", null) ?: return emptyList()
        val service = MalServiceBuilder.provideMalApiService(token)

        val fullList = mutableListOf<AnimeListItem>()
        var offset = 0
        val limit = 100

        while (true) {
            val response = service.getUserAnimeList(limit = limit, offset = offset)
            val items = response.data
            if (items.isEmpty()) break

            fullList += items

            // If fewer than 100 items were returned, this is the last page.
            if (items.size < limit) break

            offset += limit
        }

        return fullList
            .sortedBy { statusOrder[it.listStatus?.status] ?: Int.MAX_VALUE }
            .toDomain()
    }

    suspend fun getAnimeDetails(animeId: Int): AnimeNode {
        val token = sharedPreferences.getString("access_token", null)
            ?: return AnimeNode(
                id = animeId,
                title = "N/A",
                mainPicture = Picture("N/A", "N/A")
            )

        return MalServiceBuilder.provideMalApiService(token).getAnimeDetails(animeId)
    }

    /**
     * Convert a [List] of [AnimeListItem] to a list of [MyAnimeListItem]
     */
    private fun List<AnimeListItem>.toDomain(): List<MyAnimeListItem> {
        return map { it.toDomain() }
    }

    /**
     * Convert a [AnimeListItem] to a [MyAnimeListItem]
     */
    private fun AnimeListItem.toDomain(): MyAnimeListItem {
        return MyAnimeListItem(
            id = node.id,
            title = node.title,
            status = listStatus?.status ?: "plan_to_watch",
            imageUrl = node.mainPicture.medium,
            startSeason = node.startSeason.season.replaceFirstChar { it.uppercaseChar() },
            startYear = node.startSeason.year.toString(),
            numEpisodesWatched = listStatus?.numEpisodesWatched ?: 0,
            totalEpisodes = node.numEpisodes,
            rating = listStatus?.score ?: 0
        )
    }
}