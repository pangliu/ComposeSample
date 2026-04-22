package com.example.newproject.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
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
