package com.qpay.xcash.network.api

import com.qpay.xcash.network.model.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.POST

import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.network.model.response.NotificationResponse
import com.qpay.xcash.network.model.response.OrderHistoryResponse
import com.qpay.xcash.network.model.response.UpdateLogResponse
import com.qpay.xcash.network.model.response.UserInfoResponse

interface UserApiService {
    @GET("/api/user/info")
    suspend fun getUserInfo(): BaseResponse<UserInfoResponse>

    @POST("/api/logout")
    suspend fun logout(): BaseResponse<Unit>

    @POST("/api/upload/user/image")
    suspend fun uploadUserImage(): BaseResponse<Any>

    @GET("/api/user/level_info")
    suspend fun getUserLevelInfo(): BaseResponse<Any>

    @GET("/api/order/history")
    suspend fun getOrderHistory(): BaseResponse<List<OrderHistoryResponse>>

    @GET("/api/user/friend_list")
    suspend fun getFriendList(): BaseResponse<List<FriendResponse>>

    @GET("/api/update_log")
    suspend fun getUpdateLog(): BaseResponse<List<UpdateLogResponse>>

    @GET("/api/notifications")
    suspend fun getNotifications(): BaseResponse<List<NotificationResponse>>
}
