package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationResponse(
    @Json(name = "id") val id: String,
    @Json(name = "type") val type: NotificationType,
    @Json(name = "title") val title: String,
    @Json(name = "message") val message: String,
    @Json(name = "is_read") val isRead: Boolean,
    @Json(name = "created_at") val createdAt: Long
)

enum class NotificationType {
    @Json(name = "system") SYSTEM,
    @Json(name = "transaction") TRANSACTION,
    @Json(name = "promotions") PROMOTIONS,
    @Json(name = "todo") TODO
}
