package com.example.newproject.network.fake

import com.example.newproject.network.UserApiService
import com.example.newproject.network.model.BaseResponse
import kotlinx.coroutines.delay

class FakeUserApiService : UserApiService {
    override suspend fun getUserInfo(): BaseResponse<Any> {
        delay(800) // 模擬網路延遲
        return BaseResponse(
            code = 200,
            errorMsg = "成功",
            result = mapOf("name" to "Hank (Fake)", "email" to "hank.fake@gmail.com")
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
