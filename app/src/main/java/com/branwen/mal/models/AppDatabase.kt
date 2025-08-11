package com.branwen.mal.models

import androidx.room.Database
import androidx.room.RoomDatabase
import com.branwen.mal.interfaces.AnimeDao
import com.branwen.mal.interfaces.MangaDao
import com.branwen.mal.models.entity.AnimeListEntity
import com.branwen.mal.models.entity.MangaListEntity

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