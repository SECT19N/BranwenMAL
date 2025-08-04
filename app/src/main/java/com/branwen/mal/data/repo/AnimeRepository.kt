package com.branwen.mal.data.repo

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.branwen.mal.data.local.AnimeLocalDataSource
import com.branwen.mal.data.remote.AnimeRemoteDataSource
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.models.domain.MyAnimeListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

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

@Entity(tableName = "anime_list")
data class AnimeListEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val status: String,
    val imageUrl: String,
    val startSeason: String,
    val startYear: String,
    val numEpisodesWatched: Int,
    val totalEpisodes: Int?,
    val rating: Int
)

@Dao
interface AnimeDao {
    @Query(
        value = "SELECT * FROM anime_list " +
                "ORDER BY LOWER(TITLE) ASC, " +
                "CASE status " +
                "   WHEN 'watching' THEN 1 " +
                "   WHEN 'completed' THEN 2 " +
                "   WHEN 'on_hold' THEN 3 " +
                "   WHEN 'dropped' THEN 4 " +
                "   ELSE 5 " +
                "END"
    )
    fun getAll(): Flow<List<AnimeListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AnimeListEntity>)

    @Query("DELETE FROM anime_list")
    suspend fun clearAll()
}

@Database(
    entities = [AnimeListEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
}