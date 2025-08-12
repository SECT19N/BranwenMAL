package com.branwen.mal.di

import com.branwen.mal.data.local.AnimeLocalDataSource
import com.branwen.mal.data.local.MangaLocalDataSource
import com.branwen.mal.data.local.dao.AnimeDao
import com.branwen.mal.data.local.dao.MangaDao
import com.branwen.mal.data.remote.AnimeRemoteDataSource
import com.branwen.mal.data.remote.MangaRemoteDataSource
import com.branwen.mal.data.remote.api.MalApi
import com.branwen.mal.data.repository.AnimeRepository
import com.branwen.mal.data.repository.MangaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
// DataModule can include NetworkModule and DatabaseModule if they are specific to data operations
// Or AppModule can include all three if Network/DB are used more broadly.
// For now, let's assume AppModule includes them all for simplicity unless specified.
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideAnimeRemote(malApi: MalApi): AnimeRemoteDataSource = AnimeRemoteDataSource(malApi)

    @Provides
    fun provideMangaRemote(malApi: MalApi): MangaRemoteDataSource = MangaRemoteDataSource(malApi)

    @Provides
    fun provideAnimeLocal(animeDao: AnimeDao): AnimeLocalDataSource = AnimeLocalDataSource(animeDao)

    @Provides
    fun provideMangaLocal(mangaDao: MangaDao): MangaLocalDataSource = MangaLocalDataSource(mangaDao)

    @Provides
    @Singleton
    fun provideAnimeRepo(
        remote: AnimeRemoteDataSource,
        local: AnimeLocalDataSource
    ): AnimeRepository = AnimeRepository(remote, local) // Assuming AnimeRepository is the concrete class

    @Provides
    @Singleton
    fun provideMangaRepo(
        remote: MangaRemoteDataSource,
        local: MangaLocalDataSource
    ): MangaRepository = MangaRepository(remote, local) // Assuming MangaRepository is the concrete class
}
    