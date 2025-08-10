package com.branwen.mal.data.local

import com.branwen.mal.interfaces.MangaDao
import com.branwen.mal.models.domain.MyMangaListItem
import com.branwen.mal.models.entity.MangaListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MangaLocalDataSource(
    private val dao: MangaDao
) {
    fun getMangaListFlow(): Flow<List<MyMangaListItem>> =
        dao.getAll()
            .map { entities ->
                entities.map { it.toDomain() }
            }

    suspend fun saveMangaList(list: List<MyMangaListItem>) {
        dao.clearAll()
        dao.insertAll(
            list = list.map { it.toEntity() }
        )
    }

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