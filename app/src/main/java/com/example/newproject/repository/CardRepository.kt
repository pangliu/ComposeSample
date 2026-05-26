package com.example.newproject.repository

import com.example.newproject.network.api.CardApiService
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.request.AddNewCardRequest
import com.example.newproject.network.model.response.CreditCardResponse
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
