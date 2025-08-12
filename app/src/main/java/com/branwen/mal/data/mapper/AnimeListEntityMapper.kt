package com.branwen.mal.data.mapper

import com.branwen.mal.data.local.model.AnimeListEntity
import com.branwen.mal.domain.model.MyAnimeListItem

object AnimeListEntityMapper {
    /**
     * Convert a [AnimeListEntity] to a [MyAnimeListItem]
     */
    public fun AnimeListEntity.toDomain() = MyAnimeListItem(
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