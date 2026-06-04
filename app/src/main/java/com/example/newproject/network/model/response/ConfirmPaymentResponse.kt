package com.example.newproject.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConfirmPaymentResponse(
    @Json(name = "transaction_id") val transactionId: String,
    @Json(name = "status") val status: String
)
