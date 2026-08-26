package com.qpay.xcash.repository

import com.qpay.xcash.network.api.UserApiService
import com.qpay.xcash.network.manager.SessionManager
import com.qpay.xcash.network.manager.UserInfoManager
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.request.AddFriendRequest
import com.qpay.xcash.network.model.request.FindFriendRequest
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.network.model.response.NotificationResponse
import com.qpay.xcash.network.model.response.OrderHistoryResponse
import com.qpay.xcash.network.model.response.UpdateLogResponse
import com.qpay.xcash.network.model.response.UserInfoResponse
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: UserApiService,
    private val userInfoManager: UserInfoManager,
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    var selectedFriend: FriendResponse? = null

    suspend fun fetchUserInfo(): NetworkResult<UserInfoResponse> {
        return safeApiCall { apiService.getUserInfo() }.also { result ->
            if (result is NetworkResult.Success && result.data != null) {
                userInfoManager.save(result.data)
            }
        }
    }

    // 示範 2：上傳圖片
    suspend fun uploadUserImage(image: MultipartBody.Part): NetworkResult<Any> {
        return safeApiCall {
            apiService.uploadUserImage(image)
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

    suspend fun findFriend(countryCode: String, phoneNumber: String): NetworkResult<FriendResponse> {
        return safeApiCall {
            apiService.findFriend(FindFriendRequest(countryCode = countryCode, phoneNumber = phoneNumber))
        }
    }

    suspend fun addFriend(friendId: String): NetworkResult<Unit> {
        return safeApiCall {
            apiService.addFriend(AddFriendRequest(friendId = friendId))
        }
    }

    suspend fun fetchUpdateLog(): NetworkResult<List<UpdateLogResponse>> {
        return safeApiCall {
            apiService.getUpdateLog()
        }
    }

    suspend fun fetchNotifications(): NetworkResult<List<NotificationResponse>> {
        return safeApiCall {
            apiService.getNotifications()
        }
    }
}
