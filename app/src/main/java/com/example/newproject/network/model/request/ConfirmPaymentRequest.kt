package com.example.newproject.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConfirmPaymentRequest(
    @Json(name = "recipient_account") val recipientAccount: String,
    @Json(name = "recipient_name") val recipientName: String,
    @Json(name = "amount") val amount: String
)
