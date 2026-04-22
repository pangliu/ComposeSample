package com.example.newproject.network

import com.example.newproject.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @GET("/api/user/info")
    suspend fun getUserInfo(): BaseResponse<Any> 

    @POST("/api/upload/user/image")
    suspend fun uploadUserImage(): BaseResponse<Any>

    @GET("/api/user/level_info")
    suspend fun getUserLevelInfo(): BaseResponse<Any>
}
