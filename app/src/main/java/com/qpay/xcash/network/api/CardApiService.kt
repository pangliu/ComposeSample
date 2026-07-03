package com.qpay.xcash.network.api

import com.qpay.xcash.network.model.request.AddNewCardRequest
import com.qpay.xcash.network.model.response.BaseResponse
import com.qpay.xcash.network.model.response.CreditCardResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CardApiService {
    @GET("/api/credit_card_list")
    suspend fun getCreditCardList(): BaseResponse<List<CreditCardResponse>>

    @POST("/api/card/add")
    suspend fun addNewCard(@Body request: AddNewCardRequest): BaseResponse<Unit>
}
