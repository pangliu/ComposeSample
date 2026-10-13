package com.qpay.xcash.network.fake

import com.qpay.xcash.network.api.PaymentApiService
import com.qpay.xcash.network.model.request.CashInPaymentRequest
import com.qpay.xcash.network.model.request.ConfirmPaymentRequest
import com.qpay.xcash.network.model.request.TransferRequest
import com.qpay.xcash.network.model.response.BaseResponse
import com.qpay.xcash.network.model.response.ConfirmPaymentResponse
import com.qpay.xcash.network.model.response.OrderStatus
import com.qpay.xcash.network.model.response.TransactionDetailResponse
import com.qpay.xcash.network.model.response.TransferChannelItem
import com.qpay.xcash.network.model.response.TransferChannelType
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

    override suspend fun cashIn(request: CashInPaymentRequest): BaseResponse<ConfirmPaymentResponse> {
        delay(1500)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = ConfirmPaymentResponse(
                transactionId = "TXN${System.currentTimeMillis()}",
                status = "SUCCESS"
                // 🎯 測試「業務性失敗」（HTTP 200 但交易被拒）時，改用下面這組：
                // status = "FAILED"
            )
        )
    }

    override suspend fun transfer(request: TransferRequest): BaseResponse<ConfirmPaymentResponse> {
        delay(1500)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = ConfirmPaymentResponse(
                transactionId = "TRF${System.currentTimeMillis()}",
                // 🎯 目前模擬「業務性失敗」（HTTP 200 但交易被拒）→ TransferResultScreen 顯示失敗畫面
                // 測試成功流程時改回 status = "SUCCESS"
                status = "SUCCESS"
//                status = "FAILED"
            )
        )
    }

    override suspend fun getTransferChannels(): BaseResponse<List<TransferChannelItem>> {
        delay(800)
        // 🎯 API 尚未完成，image_url 先給空字串，畫面顯示預設白框
        val eWallet = TransferChannelType.E_WALLET
        val bank = TransferChannelType.BANK
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = listOf(
                TransferChannelItem(id = "xcash", name = "Xcash", channel = eWallet),
                TransferChannelItem(id = "gcash", name = "GCash", channel = eWallet),
                TransferChannelItem(id = "maya", name = "Maya", channel = eWallet),
                TransferChannelItem(id = "gotyme", name = "GoTyme", channel = eWallet),
                TransferChannelItem(id = "grab", name = "Grab", channel = eWallet),
                TransferChannelItem(id = "qrph", name = "QRPh", channel = eWallet),
                TransferChannelItem(id = "shopee_pay", name = "ShopeePay", channel = eWallet),
                TransferChannelItem(id = "bdo_business", name = "BDO Business", channel = bank),
                TransferChannelItem(id = "uno_digital_bank", name = "UNO Digital Bank", channel = bank),
                TransferChannelItem(id = "komo", name = "Komo", channel = bank),
                TransferChannelItem(id = "metrobank", name = "Metrobank", channel = bank),
                TransferChannelItem(id = "tayocash", name = "TayoCash", channel = bank),
                TransferChannelItem(id = "landbank", name = "LANDBANK", channel = bank),
                TransferChannelItem(id = "coin_ph", name = "Coins.ph", channel = bank),
                TransferChannelItem(id = "star_pay", name = "StarPay", channel = bank),
                TransferChannelItem(id = "union_digital_bank", name = "UnionDigital Bank", channel = bank),
                TransferChannelItem(id = "cimb_bank", name = "CIMB Bank", channel = bank),
            )
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
