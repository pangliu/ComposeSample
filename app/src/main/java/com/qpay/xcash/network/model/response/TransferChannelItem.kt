package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransferChannelItem(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "image_url") val imageUrl: String = "",
    @Json(name = "channel") val channel: TransferChannelType
)

enum class TransferChannelType {
    @Json(name = "e_wallet") E_WALLET,
    @Json(name = "bank") BANK
}
