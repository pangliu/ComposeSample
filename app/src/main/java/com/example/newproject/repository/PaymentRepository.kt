package com.example.newproject.repository

import com.example.newproject.network.api.PaymentApiService
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.request.ConfirmPaymentRequest
import com.example.newproject.network.model.response.ConfirmPaymentResponse
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val apiService: PaymentApiService,
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    suspend fun confirmPayment(
        recipientAccount: String,
        recipientName: String,
        amount: String
    ): NetworkResult<ConfirmPaymentResponse> {
        return safeApiCall {
            apiService.confirmPayment(
                ConfirmPaymentRequest(
                    recipientAccount = recipientAccount,
                    recipientName = recipientName,
                    amount = amount
                )
            )
        }
    }
}
