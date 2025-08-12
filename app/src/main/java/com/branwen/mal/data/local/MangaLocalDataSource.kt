package com.branwen.mal.data.local

import com.branwen.mal.interfaces.MangaDao
import com.branwen.mal.models.domain.MyMangaListItem
import com.branwen.mal.models.entity.MangaListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MangaLocalDataSource(
    private val dao: MangaDao
) {
    /**
     * Retrieves a [Flow] of [MyMangaListItem]s from the local data source.
     *
     * This function observes the local database for changes to the manga list and emits
     * a new list of [MyMangaListItem]s whenever the data is updated.
     *
     * @return A [Flow] that emits a list of [MyMangaListItem]s.
     */
    fun getMangaListFlow(): Flow<List<MyMangaListItem>> =
        dao.getAll()
            .map { entities ->
                entities.map { it.toDomain() }
            }

    /**
     * Saves a list of [MyMangaListItem]s to the local data source.
     *
     * This function first clears all existing manga entries from the database
     * and then inserts the new list. Each [MyMangaListItem] is converted to
     * its corresponding [MangaListEntity] before insertion.
     *
     * @param list The list of [MyMangaListItem]s to be saved.
     */
    suspend fun saveMangaList(list: List<MyMangaListItem>) {
        dao.clearAll()
        dao.insertAll(
            list = list.map { it.toEntity() }
        )
    }

    /**
     * Converts a [MangaListEntity] to a [MyMangaListItem].
     *
     * This extension function maps the properties of a local database entity
     * to the corresponding domain model object.
     *
     * @return The [MyMangaListItem] representation of the [MangaListEntity].
     */
    private fun MangaListEntity.toDomain() = MyMangaListItem(
        id = id,
        title = title,
        status = status,
        imageUrl = imageUrl,
        startYear = startYear,
        numChaptersRead = numChaptersRead,
        totalChapters = totalChapters,
        rating = rating
    )

    /**
     * Converts a [MyMangaListItem] to a [MangaListEntity].
     *
     * This function is used to map a domain model object to a database entity object
     * for persistence.
     *
     * @return The corresponding [MangaListEntity].
     */
    private fun MyMangaListItem.toEntity() = MangaListEntity(
        id = id,
        title = title,
        status = status,
        imageUrl = imageUrl,
        startYear = startYear,
        numChaptersRead = numChaptersRead,
        totalChapters = totalChapters,
        rating = rating
    )
}