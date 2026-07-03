package com.qpay.xcash.network.api

import com.qpay.xcash.model.Post
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

import com.qpay.xcash.network.model.request.LoginRequest
import com.qpay.xcash.network.model.request.VerifyOtpRequest
import com.qpay.xcash.network.model.response.LoginResponse
import com.qpay.xcash.network.model.response.BaseResponse
import com.qpay.xcash.network.model.response.VerifyOtpResponse

interface PublicApiService {
    // 這裡放不需要 Token 的 API，例如登入、註冊
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @POST("/api/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): BaseResponse<VerifyOtpResponse>
}
