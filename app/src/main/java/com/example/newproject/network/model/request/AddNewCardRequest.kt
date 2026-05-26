package com.example.newproject.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddNewCardRequest(
    @Json(name = "card_number") val cardNumber: String,
    @Json(name = "cardholder_name") val cardholderName: String,
    @Json(name = "expiry_date") val expiryDate: String,
    @Json(name = "cvv") val cvv: String,
    @Json(name = "billing_zip") val billingZip: String
)
