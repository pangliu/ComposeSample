package com.qpay.xcash.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransferRequest(
    @Json(name = "account_number") val accountNumber: String,
    @Json(name = "amount") val amount: String
)
