package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateLogResponse(
    @Json(name = "date") val date: String,
    @Json(name = "title") val title: String,
    @Json(name = "message") val message: String
)
