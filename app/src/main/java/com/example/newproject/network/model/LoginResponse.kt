package com.example.newproject.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "token")
    val accessToken: String,
    
    @Json(name = "refresh_token")
    val refreshToken: String
)
