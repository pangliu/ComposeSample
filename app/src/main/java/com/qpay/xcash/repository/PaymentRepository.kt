package com.qpay.xcash.repository

import com.qpay.xcash.network.api.PaymentApiService
import com.qpay.xcash.network.manager.SessionManager
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.request.CashInPaymentRequest
import com.qpay.xcash.network.model.request.ConfirmPaymentRequest
import com.qpay.xcash.network.model.response.ConfirmPaymentResponse
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.network.model.response.TransactionDetailResponse
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val apiService: PaymentApiService,
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    suspend fun fetchTransactionDetail(orderId: String): NetworkResult<TransactionDetailResponse> {
        return safeApiCall { apiService.getTransactionDetail(orderId) }
    }

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

    suspend fun cashIn(userId: String, card: CreditCardResponse): NetworkResult<ConfirmPaymentResponse> {
        return safeApiCall {
            apiService.cashIn(
                CashInPaymentRequest(
                    userId = userId,
                    cardType = card.cardType,
                    cardName = card.cardName,
                    cardNumber = card.cardNumber,
                    bankName = card.bankName
                )
            )
        }
    }
}
