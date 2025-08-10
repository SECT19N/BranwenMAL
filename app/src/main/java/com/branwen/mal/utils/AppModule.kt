package com.branwen.mal.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.room.Room
import com.branwen.mal.data.local.AnimeLocalDataSource
import com.branwen.mal.data.local.MangaLocalDataSource
import com.branwen.mal.data.remote.AnimeRemoteDataSource
import com.branwen.mal.data.remote.MangaRemoteDataSource
import com.branwen.mal.data.repo.AnimeRepository
import com.branwen.mal.data.repo.MangaRepository
import com.branwen.mal.interfaces.AnimeDao
import com.branwen.mal.interfaces.MalApi
import com.branwen.mal.interfaces.MangaDao
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
    fun provideSharedPrefs(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(
        "bran_mal_prefs",
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun provideMalApi(
        prefs: SharedPreferences
    ): MalApi {
        return MalServiceBuilder.provideMalApiService(
            accessTokenProvider = { prefs.getString("access_token", "") ?: "" },
            refreshTokenProvider = { prefs.getString("refresh_token", "") ?: "" },
            saveTokens = { tokens ->
                prefs.edit {
                    putString("access_token", tokens.accessToken)
                    putString("refresh_token", tokens.refreshToken)
                    putLong(
                        "expires_at",
                        System.currentTimeMillis() + (tokens.expiresIn * 1000L)
                    )
                }
            }
        )
    }

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
    fun provideAnimeDao(
        db: AppDatabase
    ): AnimeDao = db.animeDao()

    @Provides
    fun provideAnimeRemote(
        malApi: MalApi
    ): AnimeRemoteDataSource = AnimeRemoteDataSource(malApi)

    @Provides
    fun provideAnimeLocal(
        animeDao: AnimeDao
    ): AnimeLocalDataSource = AnimeLocalDataSource(animeDao)

    @Provides
    @Singleton
    fun provideAnimeRepo(
        remote: AnimeRemoteDataSource,
        local: AnimeLocalDataSource
    ): AnimeRepository = AnimeRepository(remote, local)

    @Provides
    fun provideMangaDao(
        db: AppDatabase
    ): MangaDao = db.mangaDao()

    @Provides
    fun provideMangaRemote(
        malApi: MalApi
    ): MangaRemoteDataSource = MangaRemoteDataSource(malApi)

    @Provides
    fun provideMangaLocal(
        mangaDao: MangaDao
    ): MangaLocalDataSource = MangaLocalDataSource(mangaDao)

    @Provides
    @Singleton
    fun provideMangaRepo(
        remote: MangaRemoteDataSource, local: MangaLocalDataSource
    ): MangaRepository = MangaRepository(remote, local)
}