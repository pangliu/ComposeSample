package com.example.newproject.network.api

import com.example.newproject.model.Post
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

import com.example.newproject.network.model.request.LoginRequest
import com.example.newproject.network.model.request.VerifyOtpRequest
import com.example.newproject.network.model.response.LoginResponse
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.VerifyOtpResponse

interface PublicApiService {
    // 這裡放不需要 Token 的 API，例如登入、註冊
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @POST("/api/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): BaseResponse<VerifyOtpResponse>
}
