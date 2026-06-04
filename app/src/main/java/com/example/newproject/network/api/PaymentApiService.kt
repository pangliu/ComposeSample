package com.example.newproject.network.api

import com.example.newproject.network.model.request.ConfirmPaymentRequest
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.ConfirmPaymentResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentApiService {
    @GET("/api/payment/list")
    suspend fun getPaymentList(): Any

    @POST("/api/payment/confirm")
    suspend fun confirmPayment(@Body request: ConfirmPaymentRequest): BaseResponse<ConfirmPaymentResponse>
}
