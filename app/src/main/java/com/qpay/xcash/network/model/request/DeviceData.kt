package com.qpay.xcash.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeviceData(
    @Json(name = "deviceId") val deviceId: String,
    @Json(name = "deviceName") val deviceName: String,
    @Json(name = "appVersion") val appVersion: String,
    @Json(name = "locale") val locale: String
)
