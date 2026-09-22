package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfoResponse(
    @Json(name = "user_id") val userId: String,
    @Json(name = "user_name") val userName: String,
    @Json(name = "nick_name") val nickName: String,
    @Json(name = "user_phone") val userPhone: String,
    @Json(name = "user_email") val userEmail: String,
    @Json(name = "cash_balance") val cashBalance: Double,
    @Json(name = "token_balance") val tokenBalance: Double,
    @Json(name = "default_bank_name") val defaultBankName: String = "",
    @Json(name = "default_card_number") val defaultCardNumber: String = "",
    @Json(name = "country_code") val countryCode: String = ""
) {
    companion object {
        fun empty() = UserInfoResponse("", "", "", "", "", 0.0, 0.0, "", "", "")
    }
}
