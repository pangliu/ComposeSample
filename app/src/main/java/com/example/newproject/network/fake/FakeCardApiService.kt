package com.example.newproject.network.fake

import com.example.newproject.network.api.CardApiService
import com.example.newproject.network.model.request.AddNewCardRequest
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.CreditCardResponse
import kotlinx.coroutines.delay

class FakeCardApiService : CardApiService {

    companion object {
        val sampleCards = listOf(
            CreditCardResponse(1, "Mastercard", "My Main Card", "5353", "gcash", isPrimary = true, nickName = "Daily Card"),
            CreditCardResponse(2, "Visa", "Travel Card", "4242", "bpi", isPrimary = false, nickName = "Travel Card"),
            CreditCardResponse(3, "Visa", "Shopping Card", "1111", "metrobank", isPrimary = false, nickName = "")
        )
    }

    override suspend fun getCreditCardList(): BaseResponse<List<CreditCardResponse>> {
        delay(800)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = sampleCards
        )
    }

    override suspend fun addNewCard(request: AddNewCardRequest): BaseResponse<Unit> {
        delay(800)
        return BaseResponse(code = 200, errorMsg = "success", result = Unit)
    }
}
