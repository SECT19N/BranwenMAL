package com.branwen.mal.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.branwen.mal.data.local.AnimeLocalDataSource
import com.branwen.mal.data.remote.AnimeRemoteDataSource
import com.branwen.mal.data.repo.AnimeRepository
import com.branwen.mal.interfaces.AnimeDao
import com.branwen.mal.models.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("bran_mal_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "anime_db")
            .fallbackToDestructiveMigration(false).build()

    @Provides
    fun provideAnimeDao(db: AppDatabase): AnimeDao = db.animeDao()

    @Provides
    fun provideRemote(sharedPrefs: SharedPreferences): AnimeRemoteDataSource =
        AnimeRemoteDataSource(sharedPrefs)

    @Provides
    fun provideLocal(animeDao: AnimeDao): AnimeLocalDataSource = AnimeLocalDataSource(animeDao)

    @Provides
    @Singleton
    fun provideRepo(
        remote: AnimeRemoteDataSource,
        local: AnimeLocalDataSource
    ): AnimeRepository = AnimeRepository(remote, local)
}