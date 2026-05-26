package com.example.newproject.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.CreditCardResponse
import com.example.newproject.repository.CardRepository
import com.example.newproject.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardsUiState(
    val isLoadingCards: Boolean = true,
    val isRefreshing: Boolean = false,
    val isAddingCard: Boolean = false,
    val cards: List<CreditCardResponse> = emptyList()
) {
    val isLoading: Boolean get() = isLoadingCards
}

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    fun fetchCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCards = true) }
            when (val result = cardRepository.fetchCreditCardList()) {
                is NetworkResult.Success -> _uiState.update { it.copy(isLoadingCards = false, cards = result.data ?: emptyList()) }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingCards = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingCards = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "Unknown error"))
                }
            }
        }
    }

    fun refreshCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            when (val result = cardRepository.fetchCreditCardList()) {
                is NetworkResult.Success -> _uiState.update { it.copy(isRefreshing = false, cards = result.data ?: emptyList()) }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "Unknown error"))
                }
            }
        }
    }

    fun selectCard(card: CreditCardResponse) {
        cardRepository.selectedCard = card
    }

    fun addNewCard(
        cardNumber: String,
        cardholderName: String,
        expiryDate: String,
        cvv: String,
        billingZip: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingCard = true) }
            when (val result = cardRepository.addNewCard(cardNumber, cardholderName, expiryDate, cvv, billingZip)) {
                is NetworkResult.Success -> Unit
                is NetworkResult.Error -> _eventFlow.emit(UiEvent.ShowToast(result.message))
                is NetworkResult.Exception -> _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "Unknown error"))
            }
            _uiState.update { it.copy(isAddingCard = false) }
        }
    }
}
