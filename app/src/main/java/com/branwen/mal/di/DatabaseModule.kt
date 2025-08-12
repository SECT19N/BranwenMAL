package com.branwen.mal.di

import android.content.Context
import androidx.room.Room
import com.branwen.mal.data.local.dao.AnimeDao
import com.branwen.mal.data.local.dao.MangaDao
import com.branwen.mal.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "anime_db"
    ).fallbackToDestructiveMigration(false).build()

    @Provides
    // DAOs are usually not singletons if the DB is a singleton,
    // as Hilt can provide them efficiently.
    fun provideAnimeDao(db: AppDatabase): AnimeDao = db.animeDao()

    @Provides
    fun provideMangaDao(db: AppDatabase): MangaDao = db.mangaDao()
}