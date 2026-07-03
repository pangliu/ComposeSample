package com.qpay.xcash.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "account")
    val account: String,
    
    @Json(name = "password")
    val password: String,
    
    @Json(name = "deviceId")
    val deviceId: String
)
