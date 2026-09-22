package com.qpay.xcash.network.fake

import com.qpay.xcash.network.api.CardApiService
import com.qpay.xcash.network.model.request.AddNewCardRequest
import com.qpay.xcash.network.model.response.BaseResponse
import com.qpay.xcash.network.model.response.CreditCardResponse
import kotlinx.coroutines.delay

class FakeCardApiService : CardApiService {

    companion object {
        val sampleCards = listOf(
            CreditCardResponse(1, "Mastercard", "My Main Card", "5353", "gcash", isPrimary = true, nickName = "Daily Card", fee = 10),
            CreditCardResponse(2, "Visa", "Travel Card", "4242", "bpi", isPrimary = false, nickName = "Travel Card", fee = 10),
            CreditCardResponse(3, "Visa", "Shopping Card", "1111", "metrobank", isPrimary = false, nickName = "", isExpired = true, fee = 10),
            CreditCardResponse(4, "Visa", "Sample Card", "0000", "sample bank", isPrimary = false, nickName = "", fee = 10),
            CreditCardResponse(5, "Visa", "Shopping Card", "2222", "pnb", isPrimary = false, nickName = "", isExpired = true, fee = 10),
            CreditCardResponse(6, "Visa", "Sample Card", "0000", "sample bank", isPrimary = false, nickName = "", fee = 10)
        )
    }

    override suspend fun getCreditCardList(): BaseResponse<List<CreditCardResponse>> {
        delay(800)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = sampleCards
//            result = emptyList()
        )
    }

    override suspend fun addNewCard(request: AddNewCardRequest): BaseResponse<Unit> {
        delay(800)
        return BaseResponse(code = 200, errorMsg = "success", result = Unit)
    }
}
