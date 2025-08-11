package com.branwen.mal.models

import com.squareup.moshi.Json

data class TokenResponse(
    @param:Json(name = "expires_in") val expiresIn: Long,
    @param:Json(name = "token_type") val tokenType: String,
    @param:Json(name = "access_token") val accessToken: String,
    @param:Json(name = "refresh_token") val refreshToken: String
)