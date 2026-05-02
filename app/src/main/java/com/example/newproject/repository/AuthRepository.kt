package com.example.newproject.repository

import com.example.newproject.network.api.PublicApiService
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.model.request.LoginRequest
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.manager.TokenManager
import com.example.newproject.network.model.request.VerifyOtpRequest
import com.example.newproject.network.model.response.LoginResponse
import com.example.newproject.network.model.response.VerifyOtpResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val publicApi: PublicApiService,
    private val tokenManager: TokenManager,
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    suspend fun login(request: LoginRequest): NetworkResult<LoginResponse> {
        val result = safeApiCall {
            publicApi.login(request)
        }
        
        // 🌟 登入成功時，自動把 Token 存入本地端
        if (result is NetworkResult.Success && result.data != null) {
            tokenManager.saveTokens(
                accessToken = result.data.accessToken,
                refreshToken = result.data.refreshToken
            )
        }
        
        return result
    }

    suspend fun verifyOtp(request: VerifyOtpRequest): NetworkResult<VerifyOtpResponse> {
        val result = safeApiCall {
            publicApi.verifyOtp(request)
        }
        
        // 🌟 驗證成功時，自動把 Token 存入本地端
        if (result is NetworkResult.Success && result.data != null) {
            tokenManager.saveTokens(
                accessToken = result.data.accessToken,
                refreshToken = result.data.refreshToken ?: ""
            )
        }
        
        return result
    }
}
