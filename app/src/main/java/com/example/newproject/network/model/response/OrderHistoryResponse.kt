package com.example.newproject.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderHistoryResponse(
    @Json(name = "order_id") val orderId: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "type") val type: OrderType,
    @Json(name = "payment_name") val paymentName: String,
    @Json(name = "account") val account: String,
    @Json(name = "target_account") val targetAccount: String,
    @Json(name = "status") val status: OrderStatus,
    @Json(name = "expired_at") val expiredAt: Long
)

enum class OrderType {
    @Json(name = "incoming") INCOMING,
    @Json(name = "outgoing") OUTGOING
}

enum class OrderStatus {
    @Json(name = "success") SUCCESS,
    @Json(name = "failed") FAILED
}
