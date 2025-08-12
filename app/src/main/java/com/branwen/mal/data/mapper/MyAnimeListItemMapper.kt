package com.branwen.mal.data.mapper

import com.branwen.mal.data.local.model.AnimeListEntity
import com.branwen.mal.domain.model.MyAnimeListItem

object MyAnimeListItemMapper {
    /**
     * Convert a [MyAnimeListItem] to a [AnimeListEntity]
     */
    public fun MyAnimeListItem.toEntity() = AnimeListEntity(
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