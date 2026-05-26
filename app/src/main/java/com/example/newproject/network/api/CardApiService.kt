package com.example.newproject.network.api

import com.example.newproject.network.model.request.AddNewCardRequest
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.CreditCardResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CardApiService {
    @GET("/api/credit_card_list")
    suspend fun getCreditCardList(): BaseResponse<List<CreditCardResponse>>

    @POST("/api/card/add")
    suspend fun addNewCard(@Body request: AddNewCardRequest): BaseResponse<Unit>
}
