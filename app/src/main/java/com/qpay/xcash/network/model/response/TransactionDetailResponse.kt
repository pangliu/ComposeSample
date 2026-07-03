package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionDetailResponse(
    @Json(name = "order_id") val orderId: String,
    @Json(name = "status") val status: OrderStatus,
    @Json(name = "currency") val currency: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "fee") val fee: Double,
    @Json(name = "pay_to_name") val payToName: String,
    @Json(name = "pay_to_account") val payToAccount: String,
    @Json(name = "pay_to_bank") val payToBank: String,
    @Json(name = "pay_from_name") val payFromName: String,
    @Json(name = "pay_from_account") val payFromAccount: String,
    @Json(name = "pay_from_bank") val payFromBank: String,
    @Json(name = "reference_no") val referenceNo: String,
    @Json(name = "date") val date: String
) {
    val total: Double get() = amount + fee
}
