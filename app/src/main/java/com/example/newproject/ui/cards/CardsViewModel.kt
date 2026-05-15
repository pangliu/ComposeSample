package com.example.newproject.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.CreditCardResponse
import com.example.newproject.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CardsState {
    object Loading : CardsState()
    data class Success(val cards: List<CreditCardResponse>, val isRefreshing: Boolean = false) : CardsState()
    data class Error(val message: String) : CardsState()
}

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _cardsState = MutableStateFlow<CardsState>(CardsState.Loading)
    val cardsState: StateFlow<CardsState> = _cardsState.asStateFlow()

    fun fetchCards() {
        viewModelScope.launch {
            _cardsState.value = CardsState.Loading
            when (val result = cardRepository.fetchCreditCardList()) {
                is NetworkResult.Success -> _cardsState.value = CardsState.Success(result.data ?: emptyList())
                is NetworkResult.Error -> _cardsState.value = CardsState.Error(result.message)
                is NetworkResult.Exception -> _cardsState.value = CardsState.Error(result.e.message ?: "Unknown error")
            }
        }
    }

    fun refreshCards() {
        val currentState = _cardsState.value
        if (currentState is CardsState.Success) {
            _cardsState.value = currentState.copy(isRefreshing = true)
        } else {
            _cardsState.value = CardsState.Loading
        }
        
        viewModelScope.launch {
            when (val result = cardRepository.fetchCreditCardList()) {
                is NetworkResult.Success -> _cardsState.value = CardsState.Success(result.data ?: emptyList(), isRefreshing = false)
                is NetworkResult.Error -> _cardsState.value = CardsState.Error(result.message)
                is NetworkResult.Exception -> _cardsState.value = CardsState.Error(result.e.message ?: "Unknown error")
            }
        }
    }
}
