package com.example.newproject.network

import retrofit2.http.GET

interface PaymentApiService {
    @GET("/api/payment/list")
    suspend fun getPaymentList(): Any
}
