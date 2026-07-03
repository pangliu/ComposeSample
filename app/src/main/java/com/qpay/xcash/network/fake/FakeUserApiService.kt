package com.qpay.xcash.network.fake

import com.qpay.xcash.network.api.UserApiService
import com.qpay.xcash.network.model.response.BaseResponse
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.network.model.response.NotificationResponse
import com.qpay.xcash.network.model.response.NotificationType
import com.qpay.xcash.network.model.response.OrderHistoryResponse
import com.qpay.xcash.network.model.response.OrderStatus
import com.qpay.xcash.network.model.response.OrderType
import com.qpay.xcash.network.model.response.UpdateLogResponse
import com.qpay.xcash.network.model.response.UserInfoResponse
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

    override suspend fun getNotifications(): BaseResponse<List<NotificationResponse>> {
        delay(700)
        val now = System.currentTimeMillis()
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = listOf(
                NotificationResponse(
                    id = "N001",
                    type = NotificationType.PROMO,
                    title = "Double Rewards Weekend",
                    message = "Earn 2x tokens on every scan & pay transaction this weekend only.",
                    isRead = false,
                    createdAt = now - 5 * 60_000
                ),
                NotificationResponse(
                    id = "N002",
                    type = NotificationType.SYSTEM,
                    title = "Security Check Passed",
                    message = "Your recent login was verified successfully from a new device.",
                    isRead = false,
                    createdAt = now - 15 * 60_000
                ),
                NotificationResponse(
                    id = "N003",
                    type = NotificationType.ACTIVITY,
                    title = "Payment Sent",
                    message = "Your payment of PHP 500.00 to John Cruz was completed successfully.",
                    isRead = false,
                    createdAt = now - 32 * 60_000
                ),
                NotificationResponse(
                    id = "N004",
                    type = NotificationType.PROMO,
                    title = "Cash In Bonus Unlocked",
                    message = "Cash in PHP 1,000 or more today and receive a free reward voucher.",
                    isRead = true,
                    createdAt = now - 2 * 60 * 60_000
                ),
                NotificationResponse(
                    id = "N005",
                    type = NotificationType.SYSTEM,
                    title = "App Updated",
                    message = "We've improved app performance and fixed several minor bugs.",
                    isRead = true,
                    createdAt = now - 25 * 60 * 60_000
                ),
                NotificationResponse(
                    id = "N006",
                    type = NotificationType.ACTIVITY,
                    title = "Weekly Summary Ready",
                    message = "Check out your spending summary and quest progress from last week.",
                    isRead = true,
                    createdAt = now - 8 * 24 * 60 * 60_000
                )
            )
        )
    }
}
