package com.qpay.xcash.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddFriendRequest(
    @Json(name = "friend_id")
    val friendId: String
)
