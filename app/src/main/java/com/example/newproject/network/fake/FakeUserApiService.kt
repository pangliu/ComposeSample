package com.example.newproject.network.fake

import com.example.newproject.network.api.UserApiService
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.FriendResponse
import com.example.newproject.network.model.response.OrderHistoryResponse
import com.example.newproject.network.model.response.OrderStatus
import com.example.newproject.network.model.response.OrderType
import com.example.newproject.network.model.response.UpdateLogResponse
import com.example.newproject.network.model.response.UserInfoResponse
import kotlinx.coroutines.delay

class FakeUserApiService : UserApiService {
    override suspend fun getUserInfo(): BaseResponse<UserInfoResponse> {
        delay(2000) // 模擬網路延遲
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = UserInfoResponse(
                userId = "U12345678",
                userName = "Hank Liu",
                nickName = "Hank",
                userPhone = "0912345678",
                userEmail = "hank.fake@gmail.com",
                cashBalance = 12500.50,
                tokenBalance = 8888.0
            )
//            result = null
        )
    }

    override suspend fun getOrderHistory(): BaseResponse<List<OrderHistoryResponse>> {
        delay(800)
        val now = System.currentTimeMillis()
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = listOf(
                OrderHistoryResponse(
                    orderId = "ORD20250001",
                    amount = 30.0,
                    type = OrderType.INCOMING,
                    paymentName = "GCash",
                    account = "09123456789",
                    targetAccount = "09987654321",
                    status = OrderStatus.SUCCESS,
                    expiredAt = now - 3_600_000
                ),
                OrderHistoryResponse(
                    orderId = "ORD20250002",
                    amount = 60.0,
                    type = OrderType.OUTGOING,
                    paymentName = "GoTyme",
                    account = "09123456789",
                    targetAccount = "09111222333",
                    status = OrderStatus.SUCCESS,
                    expiredAt = now - 7_200_000
                ),
                OrderHistoryResponse(
                    orderId = "ORD20250003",
                    amount = 500.0,
                    type = OrderType.INCOMING,
                    paymentName = "GCash",
                    account = "09123456789",
                    targetAccount = "09444555666",
                    status = OrderStatus.FAILED,
                    expiredAt = now - 86_400_000
                )
            )
        )
    }

    override suspend fun logout(): BaseResponse<Unit> {
        delay(1200) // 模擬網路延遲
        return BaseResponse(code = 200, errorMsg = "登出成功", result = null)
    }

    override suspend fun uploadUserImage(): BaseResponse<Any> {
        delay(1500)
        return BaseResponse(code = 200, errorMsg = "上傳成功", result = null)
    }

    override suspend fun getUserLevelInfo(): BaseResponse<Any> {
        delay(500)
        return BaseResponse(
            code = 200,
            errorMsg = "成功",
            result = mapOf("level" to 99, "exp" to 9999)
        )
    }

    override suspend fun getFriendList(): BaseResponse<List<FriendResponse>> {
        delay(600)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = listOf(
                FriendResponse(id = "F001", name = "Bruce Banner", nickName = "bruceb"),
                FriendResponse(id = "F002", name = "Tony Stark", nickName = "ironman"),
                FriendResponse(id = "F003", name = "Natasha Romanoff", nickName = "blackwidow")
            )
        )
    }

    override suspend fun getUpdateLog(): BaseResponse<List<UpdateLogResponse>> {
        delay(500)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = listOf(
                UpdateLogResponse(
                    date = "2026-06-18",
                    title = "v2.3.0 Release",
                    message = "New split bill feature, improved QR scan performance, and various bug fixes."
                ),
                UpdateLogResponse(
                    date = "2026-05-01",
                    title = "v2.2.0 Release",
                    message = "Added transaction history export, fixed login crash on Android 12."
                ),
                UpdateLogResponse(
                    date = "2026-03-15",
                    title = "v2.1.0 Release",
                    message = "Introduced neon theme, biometric login support, and QR code sharing."
                )
            )
        )
    }
}
