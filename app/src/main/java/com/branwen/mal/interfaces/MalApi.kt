package com.branwen.mal.interfaces

import com.branwen.mal.models.AnimeListResponse
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.models.ListStatus
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MalApi {
    @GET("v2/anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Path("anime_id") animeId: Int,
        @Query("fields") fields: String = "id,title,main_picture,alternative_titles,start_date,end_date," +
                "synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at," +
                "media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source," +
                "average_episode_duration,rating,pictures,background,related_anime,related_manga," +
                "recommendations,studios,statistics"
    ): AnimeNode

    @GET("v2/users/@me/animelist")
    suspend fun getUserAnimeList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("status") status: String? = null,
        @Query("sort") sort: String = "anime_title",
        @Query("fields") fields: String = "list_status,num_episodes,start_season"
    ): AnimeListResponse

    @PATCH("v2/anime/{anime_id}/my_list_status")
    suspend fun patchAnimeListStatus(
        @Path("anime_id") animeId: Int,
        @Query("status") status: String,
        @Query("score") score: Int,
        @Query("num_watched_episodes") numWatchedEpisodes: Int,
    ): ListStatus

    @GET("v2/anime/ranking")
    suspend fun getAnimeRanking(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("ranking_type") rankingType: String = "all", // all, airing, upcoming, tv, ova, movie, special, bypopularity, favorite
        @Query("fields") fields: String? = null // optional, we don't need it for our purpose.
    ): AnimeListResponse // TODO - Make sure to separate classes unless it won't cause issues.

    @GET("v2/anime/suggestions")
    suspend fun getAnimeSuggestions(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("fields") fields: String? = null // optional, we don't need it for our purpose.
    ): AnimeListResponse
}