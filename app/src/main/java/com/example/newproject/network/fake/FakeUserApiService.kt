package com.example.newproject.network.fake

import com.example.newproject.network.api.UserApiService
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.UserInfoResponse
import kotlinx.coroutines.delay

class FakeUserApiService : UserApiService {
    override suspend fun getUserInfo(): BaseResponse<UserInfoResponse> {
        delay(800) // 模擬網路延遲
        return BaseResponse(
            code = 200,
            errorMsg = "成功",
            result = UserInfoResponse(
                userId = "U12345678",
                userName = "Hank Liu",
                userPhone = "0912345678",
                userEmail = "hank.fake@gmail.com",
                cashBalance = 12500.50,
                tokenBalance = 8888.0
            )
        )
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
}
