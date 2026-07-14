package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "nick_name") val nickName: String,
    @Json(name = "contact_type") val contactType: ContactType
)

enum class ContactType {
    @Json(name = "facebook") FACEBOOK,
    @Json(name = "phone_num") PHONE_NUM
}
