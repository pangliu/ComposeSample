package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "nick_name") val nickName: String,
    @Json(name = "contact_type") val contactType: ContactType,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "is_favorite") val isFavorite: Boolean,
    @Json(name = "tag_label") val tagLabel: String? = null,
    @Json(name = "is_recent") val isRecent: Boolean = false,
    @Json(name = "phone_number") val phoneNumber: String? = null,
    @Json(name = "memo") val memo: String? = null
)

enum class ContactType {
    @Json(name = "facebook") FACEBOOK,
    @Json(name = "phone_num") PHONE_NUM
}
