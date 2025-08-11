package com.branwen.mal.data.remote

import android.content.SharedPreferences
import android.util.Log
import com.branwen.mal.interfaces.MalApi
import com.branwen.mal.models.domain.MyMangaListItem
import com.branwen.mal.models.remote.manga.MangaListData

/**
 * A data source for fetching manga-related data from the MyAnimeList (MAL) API.
 *
 * This class is responsible for making API calls to MAL, retrieving manga information,
 * and transforming the API response into domain-specific models. It uses [SharedPreferences]
 * to access the user's access token, which is required for authenticated API requests.
 *
 * The class defines a specific order for manga statuses (`statusOrder`) which is used
 * to sort the manga list retrieved from the API.
 *
 * @property sharedPreferences An instance of [SharedPreferences] used to retrieve the access token.
 */
class MangaRemoteDataSource(
    private val malApi: MalApi,
) {
    private val statusOrder = listOf("reading", "completed", "on_hold", "dropped", "plan_to_read").withIndex()
        .associate { it.value to it.index }

    /**
     * Retrieves the user's manga list from the MyAnimeList API.
     *
     * This function fetches the manga list in chunks (pages) due to API limitations.
     * It continues to fetch pages until an empty page is returned, indicating the end of the list.
     * The retrieved manga items are then sorted based on their status (reading, completed, etc.)
     * and converted to a list of domain model objects (`MyMangaListItem`).
     *
     * @return A list of `MyMangaListItem` representing the user's manga list, sorted by status.
     *         Returns an empty list if the access token is not found or if an error occurs during the API call.
     */
    suspend fun getMangaList(): List<MyMangaListItem> {
        try {
            val fullList = mutableListOf<MangaListData>()
            var offset = 0
            val limit = 100

            while (true) {
                val response = malApi.getUserMangaList(limit = limit, offset = offset)
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
        } catch (e: Exception) {
            Log.e("MangaRemoteDataSource", "Failed to get manga list ${e.message}")
        }

        return emptyList()
    }

    /**
     * Converts a list of [MangaListData] objects to a list of [MyMangaListItem] domain models.
     * This extension function iterates over the list and calls the [toDomain] function on each item.
     *
     * @return A list of [MyMangaListItem] objects.
     */
    private fun List<MangaListData>.toDomain(): List<MyMangaListItem> {
        return map { it.toDomain() }
    }

    /**
     * Converts a [MangaListData] object from the remote API to a [MyMangaListItem] domain object.
     *
     * This function maps the fields from the API response to the corresponding fields in the domain model.
     * It handles cases where some fields might be null by providing default values.
     *
     * @return A [MyMangaListItem] object representing the manga with relevant information.
     */
    private fun MangaListData.toDomain(): MyMangaListItem {
        return MyMangaListItem(
            id = node.id,
            title = node.title,
            status = listStatus?.status ?: "plan_to_read",
            imageUrl = node.mainPicture.medium,
            startYear = node.startDate?.take(4) ?: "N/A",
            numChaptersRead = listStatus?.numChaptersRead ?: 0,
            totalChapters = node.numberOfChapters,
            rating = listStatus?.score ?: 0
        )
    }
}