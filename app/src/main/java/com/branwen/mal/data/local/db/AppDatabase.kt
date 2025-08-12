package com.branwen.mal.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.branwen.mal.data.local.dao.AnimeDao
import com.branwen.mal.data.local.dao.MangaDao
import com.branwen.mal.data.local.model.AnimeListEntity
import com.branwen.mal.data.local.model.MangaListEntity

/**
 * Room database for the application.
 *
 * This class defines the database configuration and serves as the main access point
 * to the persisted data. It lists all the entities that are part of the database
 * and provides abstract methods to get Data Access Objects (DAOs) for each entity.
 *
 * @property animeDao Provides access to the anime data stored in the database.
 */
@Database(
    entities = [AnimeListEntity::class, MangaListEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    abstract fun mangaDao(): MangaDao
}