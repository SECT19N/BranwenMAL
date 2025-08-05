package com.branwen.mal.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.branwen.mal.models.entity.AnimeListEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the anime_list table.
 *
 * This interface defines the database interactions for AnimeListEntity.
 * It provides methods to retrieve, insert, and delete anime list data.
 */
@Dao
interface AnimeDao {
    @Query(
        value = "SELECT * FROM anime_list " +
                "ORDER BY " +
                "CASE status " +
                "   WHEN 'watching' THEN 1 " +
                "   WHEN 'completed' THEN 2 " +
                "   WHEN 'on_hold' THEN 3 " +
                "   WHEN 'dropped' THEN 4 " +
                "   ELSE 5 " +
                "END, LOWER(TITLE) ASC"
    )
    fun getAll(): Flow<List<AnimeListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AnimeListEntity>)

    @Query("DELETE FROM anime_list")
    suspend fun clearAll()

    @Query("UPDATE anime_list SET numEpisodesWatched = :newWatched WHERE id = :animeId")
    suspend fun updateWatchedEpisodes(animeId: Int, newWatched: Int)
}