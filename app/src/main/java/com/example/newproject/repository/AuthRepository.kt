package com.example.newproject.repository

import com.example.newproject.network.api.PublicApiService
import com.example.newproject.network.api.UserApiService
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.manager.TokenManager
import com.example.newproject.network.manager.UserInfoManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.request.LoginRequest
import com.example.newproject.network.model.request.VerifyOtpRequest
import com.example.newproject.network.model.response.LoginResponse
import com.example.newproject.network.model.response.VerifyOtpResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val publicApi: PublicApiService,
    private val userApi: UserApiService,
    private val tokenManager: TokenManager,
    private val userInfoManager: UserInfoManager,
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    suspend fun logout(): NetworkResult<Unit> {
        val result = safeApiCall { userApi.logout() }
        if (result is NetworkResult.Success) {
            tokenManager.clearTokens()
            userInfoManager.clear()
        }
        return result
    }

    suspend fun login(request: LoginRequest): NetworkResult<LoginResponse> {
        val result = safeApiCall {
            publicApi.login(request)
        }
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
        if (result is NetworkResult.Success && result.data != null) {
            tokenManager.saveTokens(
                accessToken = result.data.accessToken,
                refreshToken = result.data.refreshToken ?: ""
            )
        }
        return result
    }
}
