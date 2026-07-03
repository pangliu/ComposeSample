package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditCardResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "card_type") val cardType: String,
    @Json(name = "card_name") val cardName: String,
    @Json(name = "card_number") val cardNumber: String,
    @Json(name = "bank_name") val bankName: String,
    @Json(name = "is_primary") val isPrimary: Boolean = false,
    @Json(name = "nick_name") val nickName: String = ""
)
