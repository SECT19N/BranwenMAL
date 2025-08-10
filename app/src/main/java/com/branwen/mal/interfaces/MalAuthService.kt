package com.branwen.mal.interfaces

import com.branwen.mal.models.TokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MalAuthService {
    @FormUrlEncoded
    @POST("/v1/oauth2/token")
    suspend fun exchangeToken(
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("client_id") clientId: String,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
    ): TokenResponse

    @FormUrlEncoded
    @POST("v1/oauth2/token")
    fun refreshAccessToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): Call<TokenResponse>
}