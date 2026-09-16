package com.qpay.xcash.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CashInPaymentRequest(
    @Json(name = "user_id") val userId: String,
    @Json(name = "card_type") val cardType: String,
    @Json(name = "card_name") val cardName: String,
    @Json(name = "card_number") val cardNumber: String,
    @Json(name = "bank_name") val bankName: String
)
