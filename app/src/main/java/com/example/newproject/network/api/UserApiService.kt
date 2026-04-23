package com.example.newproject.network.api

import com.example.newproject.network.model.response.BaseResponse
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
