package com.example.newproject.network.fake

import com.example.newproject.model.Post
import com.example.newproject.network.api.PublicApiService
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.request.LoginRequest
import com.example.newproject.network.model.request.VerifyOtpRequest
import com.example.newproject.network.model.response.LoginResponse
import com.example.newproject.network.model.response.VerifyOtpResponse
import kotlinx.coroutines.delay

class FakePublicApiService : PublicApiService {
    override suspend fun login(request: LoginRequest): BaseResponse<LoginResponse> {
        delay(1000) // 模擬網路延遲
        
        // 假資料邏輯判斷
        return when (request.account) {
            "test", "admin", "success" -> {
                BaseResponse(
                    code = 200,
                    errorMsg = "登入成功",
                    result = LoginResponse(
                        accessToken = "mock_access_token_123",
                        refreshToken = "mock_refresh_token_456"
                    )
                )
            }
            "verify" -> {
                BaseResponse(
                    code = 2001, // 模擬 deviceId 錯誤需簡訊驗證
                    errorMsg = "需要簡訊驗證",
                    result = null
                )
            }
            else -> {
                BaseResponse(
                    code = 1005, // 模擬之前提到的密碼錯誤/無效 token 代碼
                    errorMsg = "帳號或密碼錯誤 (Mock)",
                    result = null
                )
            }
        }
    }

    override suspend fun verifyOtp(request: VerifyOtpRequest): BaseResponse<VerifyOtpResponse> {
        delay(1000)
        return if (request.otp == "123456") {
            BaseResponse(
                code = 200,
                errorMsg = "驗證成功",
                result = VerifyOtpResponse(
                    accessToken = "mock_access_token_from_otp",
                    refreshToken = "mock_refresh_token_from_otp"
                )
            )
        } else {
            BaseResponse(
                code = 1006, // 假設 1006 是 OTP 錯誤
                errorMsg = "OTP 錯誤",
                result = null
            )
        }
    }
}
