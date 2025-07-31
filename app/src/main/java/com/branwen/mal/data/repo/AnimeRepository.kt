package com.branwen.mal.data.repo

import android.content.SharedPreferences
import com.branwen.mal.models.AnimeListItem
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.models.Picture
import com.branwen.mal.utils.MalServiceBuilder

class AnimeRepository(
    private val remote: AnimeRemoteDataSource, private val local: AnimeLocalDataSource
) {
    suspend fun getAnimeList(): List<MyAnimeListItem> {
        return runCatching {
            remote.getAnimeList()
        }.onSuccess {
            local.saveAnimeList(it)
        }.getOrElse {
            local.getAnimeList()
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

class AnimeRemoteDataSource(
    private val sharedPreferences: SharedPreferences
) {
    private val statusOrder = listOf("watching", "completed", "on_hold", "dropped", "plan_to_watch").withIndex()
        .associate { it.value to it.index }

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

    private fun List<AnimeListItem>.toDomain(): List<MyAnimeListItem> {
        return map { it.toDomain() }
    }

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

class AnimeLocalDataSource { //placeholder
    suspend fun getAnimeList(): List<MyAnimeListItem> = emptyList()
    suspend fun saveAnimeList(list: List<MyAnimeListItem>) = Unit

    suspend fun getAnimeDetails(animeId: Int): AnimeNode? = null
    suspend fun saveAnimeDetails(node: AnimeNode) = Unit
}

data class MyAnimeListItem(
    val id: Int,
    val title: String,
    val status: String,
    val imageUrl: String,
    val startSeason: String,
    val startYear: String,
    val numEpisodesWatched: Int,
    val totalEpisodes: Int?,
    val rating: Int
)

data class Anime(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val membersCount: Int?,
    val rank: Int?,
    val popularityRank: Int?,
    val mediaType: String?,
    val year: Int?,
    val status: String?,
    val episodes: Int?,
    val episodeDurationMinutes: Int?,
    val genres: List<Genre>,
    val synopsis: String?,
    val statusStats: Map<String, Int>
)

data class Genre(val id: Int, val name: String)