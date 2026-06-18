package com.example.newproject.network.fake

import com.example.newproject.network.api.PaymentApiService
import com.example.newproject.network.model.request.ConfirmPaymentRequest
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.ConfirmPaymentResponse
import com.example.newproject.network.model.response.OrderStatus
import com.example.newproject.network.model.response.TransactionDetailResponse
import kotlinx.coroutines.delay

class FakePaymentApiService : PaymentApiService {
    override suspend fun getPaymentList(): Any {
        delay(500)
        return emptyList<Any>()
    }

    override suspend fun confirmPayment(request: ConfirmPaymentRequest): BaseResponse<ConfirmPaymentResponse> {
        delay(1500)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = ConfirmPaymentResponse(
                transactionId = "TXN${System.currentTimeMillis()}",
                status = "SUCCESS"
            )
//            errorMsg = "failed to payment，test xxxx",
//            result = null
        )
    }

    override suspend fun getTransactionDetail(orderId: String): BaseResponse<TransactionDetailResponse> {
        delay(800)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = TransactionDetailResponse(
                orderId = orderId,
                status = OrderStatus.SUCCESS,
                currency = "PHP",
                amount = 300.0,
                fee = 0.0,
                payToName = "Kenny",
                payToAccount = "••••••••6438",
                payToBank = "Bank Name",
                payFromName = "Barbie",
                payFromAccount = "••••••••1637",
                payFromBank = "Bank Name",
                referenceNo = "ITR${System.currentTimeMillis()}",
                date = "18 Jun 2026 at 10:28 PM"
            )
        )
    }
}
