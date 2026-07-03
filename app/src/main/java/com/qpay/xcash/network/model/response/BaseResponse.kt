package com.qpay.xcash.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// BaseResponse 帶有泛型 <T>，Moshi KSP 實體化時無法為泛型自動生成 Adapter
// 所以必須移除 @JsonClass(generateAdapter = true)，交給 Moshi 執行期間解析
data class BaseResponse<T>(
    @Json(name = "code")
    val code: Int,
    
    @Json(name = "error_msg")
    val errorMsg: String,
    
    @Json(name = "result")
    val result: T?
) {
    fun isSuccess(): Boolean {
        return code == 200
    }
}
