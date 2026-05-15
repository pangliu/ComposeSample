package com.example.newproject.repository

import com.example.newproject.network.api.CardApiService
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.CreditCardResponse
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val apiService: CardApiService,
    sessionManager: SessionManager
) : BaseRepository(sessionManager) {

    suspend fun fetchCreditCardList(): NetworkResult<List<CreditCardResponse>> {
        return safeApiCall { apiService.getCreditCardList() }
    }
}
