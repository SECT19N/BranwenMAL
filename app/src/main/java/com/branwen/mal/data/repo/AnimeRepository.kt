package com.branwen.mal.data.repo

import android.content.SharedPreferences
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.branwen.mal.models.AnimeListItem
import com.branwen.mal.models.AnimeNode
import com.branwen.mal.models.Picture
import com.branwen.mal.utils.MalServiceBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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
                "ORDER BY CASE status " +
                "WHEN 'watching' THEN 1 " +
                "WHEN 'completed' THEN 2 " +
                "WHEN 'on_hold' THEN 3 " +
                "WHEN 'dropped' THEN 4 " +
                "ELSE 5 END"
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