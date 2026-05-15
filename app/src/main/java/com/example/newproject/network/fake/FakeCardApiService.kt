package com.example.newproject.network.fake

import com.example.newproject.network.api.CardApiService
import com.example.newproject.network.model.response.BaseResponse
import com.example.newproject.network.model.response.CreditCardResponse
import kotlinx.coroutines.delay

class FakeCardApiService : CardApiService {
    override suspend fun getCreditCardList(): BaseResponse<List<CreditCardResponse>> {
        delay(800)
        return BaseResponse(
            code = 200,
            errorMsg = "success",
            result = listOf(
                CreditCardResponse(
                    id = 1,
                    cardType = "Visa",
                    cardName = "Hank Liu",
                    cardNumber = "4242",
                    bankName = "gcash"
                ),
                CreditCardResponse(
                    id = 2,
                    cardType = "Mastercard",
                    cardName = "Hank Liu",
                    cardNumber = "5353",
                    bankName = "gotyme"
                ),
                CreditCardResponse(
                    id = 3,
                    cardType = "Visa",
                    cardName = "Hank Liu",
                    cardNumber = "5353",
                    bankName = "shopee"
                ),
                CreditCardResponse(
                    id = 4,
                    cardType = "Mastercard",
                    cardName = "Hank Liu",
                    cardNumber = "5353",
                    bankName = "metrobank"
                ),
                CreditCardResponse(
                    id = 5,
                    cardType = "Visa",
                    cardName = "Hank Liu",
                    cardNumber = "5353",
                    bankName = "grab"
                )
            )
        )
    }
}
