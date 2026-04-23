package com.example.newproject.network.api

import retrofit2.http.GET
import retrofit2.http.POST

interface CardApiService {
    @GET("/api/card/list")
    suspend fun getCardList(): Any

    @POST("/api/card/update_balance")
    suspend fun updateBalance(): Any
}
