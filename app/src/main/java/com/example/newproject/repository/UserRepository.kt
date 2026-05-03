package com.example.newproject.repository

import com.example.newproject.network.api.UserApiService
import com.example.newproject.network.model.NetworkResult

import com.example.newproject.network.manager.SessionManager
import javax.inject.Inject

import com.example.newproject.network.model.response.UserInfoResponse

/**
 * 負責處理 User 相關的所有資料邏輯
 */
class UserRepository @Inject constructor(
    private val apiService: UserApiService,
    // 🎯 讓 Hilt 提供 SessionManager，並將它傳給 BaseRepository
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    // 示範 1：取得使用者資訊
    suspend fun fetchUserInfo(): NetworkResult<UserInfoResponse> {
        // 只要用 safeApiCall 包起來，就會自動獲得錯誤代碼攔截、Thread 切換與 try-catch
        return safeApiCall {
            apiService.getUserInfo()
        }
    }

    // 示範 2：上傳圖片
    suspend fun uploadUserImage(): NetworkResult<Any> {
        return safeApiCall {
            apiService.uploadUserImage()
        }
    }

    // 示範 3：取得等級
    suspend fun fetchUserLevelInfo(): NetworkResult<Any> {
        return safeApiCall {
            apiService.getUserLevelInfo()
        }
    }
}
