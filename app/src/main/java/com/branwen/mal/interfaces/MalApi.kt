package com.branwen.mal.interfaces

import com.branwen.mal.models.AnimeListResponse
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.models.ListStatus
import com.branwen.mal.models.remote.UpdatedAnimeResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface defining the API endpoints for MyAnimeList (MAL).
 * This interface is used by Retrofit to make network requests to the MAL API.
 */
interface MalApi {
    /**
     * Retrieves detailed information about a specific anime.
     *
     * This function makes a GET request to the `/v2/anime/{anime_id}` endpoint of the MyAnimeList API.
     * It allows fetching a comprehensive set of details for a given anime ID.
     *
     * @param animeId The ID of the anime to retrieve details for.
     * @param fields A comma-separated string specifying which fields to include in the response.
     *               Defaults to a comprehensive list of fields including:
     *               `id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics`.
     * @return An [AnimeNode] object containing the detailed information for the requested anime.
     */
    @GET("v2/anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Path("anime_id") animeId: Int,
        @Query("fields") fields: String = "id,title,main_picture,alternative_titles,start_date,end_date," +
                "synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at," +
                "media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source," +
                "average_episode_duration,rating,pictures,background,related_anime,related_manga," +
                "recommendations,studios,statistics"
    ): AnimeNode

    /**
     * Retrieves the anime list for the authenticated user.
     *
     * This function makes a GET request to the "v2/users/@me/animelist" endpoint
     * of the MyAnimeList API. It allows fetching a paginated list of anime
     * present in the user's list, with options to filter by status, sort the results,
     * and specify which fields to return for each anime.
     *
     * @param limit The maximum number of items to return in a single response. Defaults to 100.
     * @param offset The number of items to skip from the beginning of the list. Used for pagination. Defaults to 0.
     * @param status Filters the list by anime status. Possible values include "watching", "completed", "on_hold", "dropped", "plan_to_watch". If null, no status filter is applied. Defaults to null.
     * @param sort Specifies the sorting criteria for the returned list. Defaults to "anime_title".
     *             Other possible values include "list_score", "list_updated_at", "anime_start_date", "anime_id".
     * @param fields A comma-separated string of fields to include in the response for each anime.
     *               Defaults to "list_status,num_episodes,start_season".
     *               Refer to the MyAnimeList API documentation for a full list of available fields.
     * @return [AnimeListResponse] An object containing the list of anime and pagination information.
     */
    @GET("v2/users/@me/animelist")
    suspend fun getUserAnimeList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("status") status: String? = null,
        @Query("sort") sort: String = "anime_title",
        @Query("fields") fields: String = "list_status,num_episodes,start_season"
    ): AnimeListResponse

    /**
     * Updates the status of an anime in the user's list.
     *
     * @param animeId The ID of the anime to update.
     * @param status The new status of the anime (e.g., "watching", "completed", "on_hold", "dropped", "plan_to_watch").
     * @param score The new score for the anime (0-10).
     * @param numWatchedEpisodes The number of episodes watched.
     * @return The updated [ListStatus] object.
     */
    @PATCH("v2/anime/{anime_id}/my_list_status")
    @FormUrlEncoded
    suspend fun patchAnimeListStatus(
        @Path("anime_id") animeId: Int,
        @Field("status") status: String,
        @Field("score") score: Int,
        @Field("num_watched_episodes") numWatchedEpisodes: Int,
    ): UpdatedAnimeResponse

    /**
     * Retrieves a ranked list of anime based on the specified criteria.
     *
     * @param limit The maximum number of anime to return in the response. Defaults to 10.
     * @param offset The starting point for the list of anime. Defaults to 0.
     * @param rankingType The type of ranking to use. Possible values are:
     *  - "all": All anime.
     *  - "airing": Currently airing anime.
     *  - "upcoming": Upcoming anime.
     *  - "tv": TV series.
     *  - "ova": Original Video Animations.
     *  - "movie": Movies.
     *  - "special": Special episodes.
     *  - "bypopularity": Anime ranked by popularity.
     *  - "favorite": Anime ranked by user favorites.
     *  Defaults to "all".
     * @param fields An optional comma-separated list of fields to include in the response for each anime.
     *  If null or empty, a default set of fields will be returned.
     * @return An [AnimeListResponse] object containing the ranked list of anime.
     */
    @GET("v2/anime/ranking")
    suspend fun getAnimeRanking(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("ranking_type") rankingType: String = "all", // all, airing, upcoming, tv, ova, movie, special, bypopularity, favorite
        @Query("fields") fields: String? = null // optional, we don't need it for our purpose.
    ): AnimeListResponse // TODO - Make sure to separate classes unless it won't cause issues.

    /**
     * Retrieves anime suggestions for the authenticated user.
     *
     * This function makes a GET request to the "v2/anime/suggestions" endpoint
     * of the MyAnimeList API. It returns a list of anime that the user might
     * be interested in, based on their viewing history and preferences.
     *
     * @param limit The maximum number of suggestions to return. Defaults to 10.
     * @param offset The number of items to skip from the beginning of the suggestion list. Used for pagination. Defaults to 0.
     * @param fields An optional comma-separated string of fields to include in the response for each anime.
     *               If null or empty, a default set of fields will be returned by the API.
     *               For the purpose of this application, this field is optional and not typically needed.
     * @return [AnimeListResponse] An object containing the list of anime suggestions and pagination information.
     */
    @GET("v2/anime/suggestions")
    suspend fun getAnimeSuggestions(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("fields") fields: String? = null // optional, we don't need it for our purpose.
    ): AnimeListResponse
}