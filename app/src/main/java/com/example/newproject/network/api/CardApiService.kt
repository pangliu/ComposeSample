package com.example.newproject.network.api

import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.CreditCardResponse
import retrofit2.http.GET

interface CardApiService {
    @GET("/api/cridit_card_list")
    suspend fun getCreditCardList(): BaseResponse<List<CreditCardResponse>>
}
