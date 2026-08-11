package com.qpay.xcash.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FindFriendRequest(
    @Json(name = "country_code")
    val countryCode: String,

    @Json(name = "phone_number")
    val phoneNumber: String
)
