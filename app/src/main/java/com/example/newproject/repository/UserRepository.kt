package com.example.newproject.repository

import com.example.newproject.network.api.UserApiService
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.manager.UserInfoManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.FriendResponse
import com.example.newproject.network.model.response.OrderHistoryResponse
import com.example.newproject.network.model.response.UpdateLogResponse
import com.example.newproject.network.model.response.UserInfoResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: UserApiService,
    private val userInfoManager: UserInfoManager,
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    suspend fun fetchUserInfo(): NetworkResult<UserInfoResponse> {
        return safeApiCall { apiService.getUserInfo() }.also { result ->
            if (result is NetworkResult.Success && result.data != null) {
                userInfoManager.save(result.data)
            }
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

    suspend fun fetchOrderHistory(): NetworkResult<List<OrderHistoryResponse>> {
        return safeApiCall {
            apiService.getOrderHistory()
        }
    }

    suspend fun fetchFriendList(): NetworkResult<List<FriendResponse>> {
        return safeApiCall {
            apiService.getFriendList()
        }
    }

    suspend fun fetchUpdateLog(): NetworkResult<List<UpdateLogResponse>> {
        return safeApiCall {
            apiService.getUpdateLog()
        }
    }
}
