package com.qpay.xcash.ui.cashin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.repository.CardRepository
import com.qpay.xcash.ui.UiEvent
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

data class CashInUiState(
    val isLoadingCards: Boolean = true,
    val primaryCard: CreditCardResponse? = null,
) {
    val isLoading: Boolean get() = isLoadingCards
}

@HiltViewModel
class CashInViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CashInUiState())
    val uiState: StateFlow<CashInUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        fetchCreditCardList()
    }

    private fun fetchCreditCardList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCards = true) }
            when (val result = cardRepository.fetchCreditCardList()) {
                is NetworkResult.Success -> {
                    val primaryCard = result.data?.firstOrNull { it.isPrimary }
                    _uiState.update { it.copy(isLoadingCards = false, primaryCard = primaryCard) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingCards = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingCards = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }
}
