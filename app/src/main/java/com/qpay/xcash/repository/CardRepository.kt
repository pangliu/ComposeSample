package com.qpay.xcash.repository

import com.qpay.xcash.network.api.CardApiService
import com.qpay.xcash.network.manager.SessionManager
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.request.AddNewCardRequest
import com.qpay.xcash.network.model.response.CreditCardResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val apiService: CardApiService,
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    var selectedCard: CreditCardResponse? = null

    suspend fun fetchCreditCardList(): NetworkResult<List<CreditCardResponse>> {
        return safeApiCall { apiService.getCreditCardList() }
    }

    suspend fun addNewCard(
        cardNumber: String,
        cardholderName: String,
        expiryDate: String,
        cvv: String,
        billingZip: String
    ): NetworkResult<Unit> {
        return safeApiCall {
            apiService.addNewCard(
                AddNewCardRequest(
                    cardNumber = cardNumber,
                    cardholderName = cardholderName,
                    expiryDate = expiryDate,
                    cvv = cvv,
                    billingZip = billingZip
                )
            )
        }
    }
}
