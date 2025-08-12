package com.branwen.mal.data.remote.api

import com.branwen.mal.data.remote.dto.TokenResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object MalServiceBuilder {
    fun provideAuthService(): MalAuthService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://myanimelist.net/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(MalAuthService::class.java)
    }

    fun provideMalApiService(
        accessTokenProvider: () -> String,
        refreshTokenProvider: () -> String,
        saveTokens: (TokenResponse) -> Unit, // store updated tokens
    ): MalApi {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val authRetrofit = Retrofit.Builder()
            .baseUrl("https://myanimelist.net/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val authApi = authRetrofit.create(MalAuthService::class.java)

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${accessTokenProvider()}")
                    .build()
                chain.proceed(request)
            }
            .authenticator { _, response ->
                if (responseCount(response) >= 2) return@authenticator null

                val refreshToken = refreshTokenProvider()

                val tokenResponse = authApi.refreshAccessToken(
                    refreshToken = refreshToken
                ).execute()

                if (tokenResponse.isSuccessful) {
                    val body = tokenResponse.body() ?: return@authenticator null
                    saveTokens(body)

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${body.accessToken}")
                        .build()
                } else {
                    null
                }
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.myanimelist.net/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

        return retrofit.create(MalApi::class.java)
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var r = response.priorResponse
        while (r != null) {
            count++
            r = r.priorResponse
        }
        return count
    }
}