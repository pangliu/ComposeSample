package com.qpay.xcash.ui.cashin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.UserInfoManager
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
    val defaultBankName: String = "",
    val defaultCardNumber: String = "",
    val isLoadingCards: Boolean = false,
    val cards: List<CreditCardResponse> = emptyList(),
    val selectedCardId: Int? = null,
) {
    val isLoading: Boolean get() = isLoadingCards
    val selectedCard: CreditCardResponse? get() = cards.firstOrNull { it.id == selectedCardId }

    // CashInScreen 顯示用：使用者在 PaymentMethod 選過卡就顯示該卡，否則顯示 UserInfoManager 存的預設綁卡
    val displayBankName: String get() = selectedCard?.bankName ?: defaultBankName
    val displayCardNumber: String get() = selectedCard?.cardNumber ?: defaultCardNumber
    val hasBankCardToDisplay: Boolean get() = displayBankName.isNotEmpty() || displayCardNumber.isNotEmpty()
}

@HiltViewModel
class CashInViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val userInfoManager: UserInfoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CashInUiState())
    val uiState: StateFlow<CashInUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        val userInfo = userInfoManager.userInfoFlow.value
        _uiState.update {
            it.copy(
                defaultBankName = userInfo?.defaultBankName.orEmpty(),
                defaultCardNumber = userInfo?.defaultCardNumber.orEmpty()
            )
        }
    }

    // PaymentMethodScreen 每次進入都要呼叫，取得最新的信用卡清單
    fun fetchCreditCardList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCards = true) }
            when (val result = cardRepository.fetchCreditCardList()) {
                is NetworkResult.Success -> {
                    val cards = result.data ?: emptyList()
                    val selectedCardId = _uiState.value.selectedCardId ?: cards.firstOrNull { it.isPrimary }?.id
                    _uiState.update {
                        it.copy(isLoadingCards = false, cards = cards, selectedCardId = selectedCardId)
                    }
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

    fun selectCard(cardId: Int) {
        val card = _uiState.value.cards.firstOrNull { it.id == cardId } ?: return
        if (card.isExpired) return
        _uiState.update { it.copy(selectedCardId = cardId) }
    }
}
