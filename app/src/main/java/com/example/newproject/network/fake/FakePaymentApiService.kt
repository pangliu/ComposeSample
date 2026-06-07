package com.example.newproject.network.fake

import com.example.newproject.network.api.PaymentApiService
import com.example.newproject.network.model.request.ConfirmPaymentRequest
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.ConfirmPaymentResponse
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
}
