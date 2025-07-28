package com.branwen.mal.utils

import android.content.Context
import android.content.SharedPreferences
import com.branwen.mal.data.repo.AnimeRepository
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
    fun provideAnimeRepo(prefs: SharedPreferences): AnimeRepository = AnimeRepository(prefs)
}