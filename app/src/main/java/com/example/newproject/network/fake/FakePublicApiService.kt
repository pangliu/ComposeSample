package com.example.newproject.network.fake

import com.example.newproject.model.Post
import com.example.newproject.network.PublicApiService
import com.example.newproject.network.model.BaseResponse
import com.example.newproject.network.model.LoginRequest
import com.example.newproject.network.model.LoginResponse
import kotlinx.coroutines.delay

class FakePublicApiService : PublicApiService {
    override suspend fun login(request: LoginRequest): BaseResponse<LoginResponse> {
        delay(1000) // 模擬網路延遲
        
        // 假資料邏輯判斷
        return if (request.account == "test" || request.account == "admin") {
            BaseResponse(
                code = 200,
                errorMsg = "登入成功",
                result = LoginResponse(
                    accessToken = "mock_access_token_123",
                    refreshToken = "mock_refresh_token_456"
                )
            )
        } else {
            BaseResponse(
                code = 1005, // 模擬之前提到的密碼錯誤/無效 token 代碼
                errorMsg = "帳號或密碼錯誤 (Mock)",
                result = null
            )
        }
    }

    override suspend fun getPublicPosts(): List<Post> {
        delay(500)
        return listOf(
            Post(userId = 1, id = 101, title = "Mock Title 1", body = "Mock Body 1"),
            Post(userId = 1, id = 102, title = "Mock Title 2", body = "Mock Body 2")
        )
    }
}
