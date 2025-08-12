package com.branwen.mal.di

import android.content.SharedPreferences
import androidx.core.content.edit
import com.branwen.mal.data.remote.api.MalApi
import com.branwen.mal.data.remote.api.MalServiceBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMalApi(
        prefs: SharedPreferences // Depends on SharedPreferences from AppModule
    ): MalApi {
        return MalServiceBuilder.provideMalApiService( // Your existing builder logic
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
}