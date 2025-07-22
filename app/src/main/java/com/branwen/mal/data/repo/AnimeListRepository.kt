package com.branwen.mal.data.repo

import android.content.SharedPreferences
import com.branwen.mal.models.AnimeListItem
import com.branwen.mal.utils.MalServiceBuilder

class AnimeListRepository(
    private val sharedPreferences: SharedPreferences
) {
    private val statusOrder = listOf(
        "watching",
        "completed",
        "on_hold",
        "dropped",
        "plan_to_watch"
    ).withIndex().associate { it.value to it.index }

    suspend fun getAnimeList(): List<AnimeListItem> {
        val accessToken = sharedPreferences.getString("access_token", null)
            ?: return emptyList()

        val apiService = MalServiceBuilder.provideMalApiService(accessToken)
        val response =
            apiService.getUserAnimeList(limit = 1000, offset = 0) //TODO - add logic to load 100 at a time later on

        return response.data.sortedBy {
            statusOrder[it.listStatus?.status] ?: Int.MAX_VALUE
        }
    }
}