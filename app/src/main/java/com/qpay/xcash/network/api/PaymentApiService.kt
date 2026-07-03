package com.qpay.xcash.network.api

import com.qpay.xcash.network.model.request.ConfirmPaymentRequest
import com.qpay.xcash.network.model.response.BaseResponse
import com.qpay.xcash.network.model.response.ConfirmPaymentResponse
import com.qpay.xcash.network.model.response.TransactionDetailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApiService {
    @GET("/api/payment/list")
    suspend fun getPaymentList(): Any

    @POST("/api/payment/confirm")
    suspend fun confirmPayment(@Body request: ConfirmPaymentRequest): BaseResponse<ConfirmPaymentResponse>

    @GET("/api/payment/detail/{orderId}")
    suspend fun getTransactionDetail(@Path("orderId") orderId: String): BaseResponse<TransactionDetailResponse>
}
