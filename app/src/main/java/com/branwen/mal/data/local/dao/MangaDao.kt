package com.branwen.mal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.branwen.mal.data.local.model.MangaListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {
    @Query(
        value = "SELECT * FROM manga_list " +
                "ORDER BY " +
                "CASE status " +
                "   WHEN 'reading' THEN 1 " +
                "   WHEN 'completed' THEN 2 " +
                "   WHEN 'on_hold' THEN 3 " +
                "   WHEN 'dropped' THEN 4 " +
                "ELSE 5 " +
                "END, LOWER(TITLE) ASC"
    )
    fun getAll(): Flow<List<MangaListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<MangaListEntity>)

    @Query("DELETE FROM manga_list")
    suspend fun clearAll()

    @Query("UPDATE manga_list SET numChaptersRead = :newRead WHERE id = :mangaId")
    suspend fun updateReadChapters(mangaId: Int, newRead: Int)
}