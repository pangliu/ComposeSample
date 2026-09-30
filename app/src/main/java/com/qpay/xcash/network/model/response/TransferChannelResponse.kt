package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransferChannelResponse(
    @Json(name = "e_wallet") val eWallet: List<TransferChannelItem> = emptyList(),
    @Json(name = "bank") val bank: List<TransferChannelItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TransferChannelItem(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "image_url") val imageUrl: String = ""
)
