package com.example.newproject.network

import com.example.newproject.model.Post
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

import com.example.newproject.network.model.LoginRequest
import com.example.newproject.network.model.LoginResponse
import com.example.newproject.network.model.BaseResponse

interface PublicApiService {
    // 這裡放不需要 Token 的 API，例如登入、註冊
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @GET("posts?_limit=2") // 僅作為範例
    suspend fun getPublicPosts(): List<Post>
}
