package com.qpay.xcash.ui.cashin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.UserInfoManager
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.repository.CardRepository
import com.qpay.xcash.repository.PaymentRepository
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

data class CashInResultInfo(
    val isSuccess: Boolean,
    val amount: Long,
    val bankName: String,
    val cardNumber: String,
    val balanceAfter: Double,
    val transactionId: String,
    val failureReason: String = "",
    val timestampMillis: Long = System.currentTimeMillis()
)

// CashInScreen 送出後的一次性導航事件；結果資料本身放在 CashInUiState（CashInResultScreen 讀取用）
sealed class CashInNavigationEvent {
    data object NavigateToResult : CashInNavigationEvent()
}

data class CashInUiState(
    val defaultBankName: String = "",
    val defaultCardNumber: String = "",
    val cashBalance: Double = 0.0,
    val isLoadingCards: Boolean = false,
    val isSubmittingTopUp: Boolean = false,
    val cards: List<CreditCardResponse> = emptyList(),
    val selectedCardId: Int? = null,
    val topUpResult: CashInResultInfo? = null,
) {
    val isLoading: Boolean get() = isLoadingCards || isSubmittingTopUp
    val selectedCard: CreditCardResponse? get() = cards.firstOrNull { it.id == selectedCardId }

    // CashInScreen 顯示用：使用者在 PaymentMethod 選過卡就顯示該卡，否則顯示 UserInfoManager 存的預設綁卡
    val displayBankName: String get() = selectedCard?.bankName ?: defaultBankName
    val displayCardNumber: String get() = selectedCard?.cardNumber ?: defaultCardNumber
    val hasBankCardToDisplay: Boolean get() = displayBankName.isNotEmpty() || displayCardNumber.isNotEmpty()
}

@HiltViewModel
class CashInViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val paymentRepository: PaymentRepository,
    private val userInfoManager: UserInfoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CashInUiState())
    val uiState: StateFlow<CashInUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<CashInNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<CashInNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        val userInfo = userInfoManager.userInfoFlow.value
        _uiState.update {
            it.copy(
                defaultBankName = userInfo?.defaultBankName.orEmpty(),
                defaultCardNumber = userInfo?.defaultCardNumber.orEmpty(),
                cashBalance = userInfo?.cashBalance ?: 0.0
            )
        }
    }

    // PaymentMethodScreen 每次進入都要呼叫，取得最新的信用卡清單
    fun fetchCreditCardList() {
        viewModelScope.launch { loadCreditCardList() }
    }

    private suspend fun loadCreditCardList(): List<CreditCardResponse>? {
        _uiState.update { it.copy(isLoadingCards = true) }
        return when (val result = cardRepository.fetchCreditCardList()) {
            is NetworkResult.Success -> {
                val cards = result.data ?: emptyList()
                val selectedCardId = _uiState.value.selectedCardId ?: cards.firstOrNull { it.isPrimary }?.id
                _uiState.update {
                    it.copy(isLoadingCards = false, cards = cards, selectedCardId = selectedCardId)
                }
                cards
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoadingCards = false) }
                _eventFlow.emit(UiEvent.ShowToast(result.message))
                null
            }
            is NetworkResult.Exception -> {
                _uiState.update { it.copy(isLoadingCards = false) }
                _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                null
            }
        }
    }

    fun selectCard(cardId: Int) {
        val card = _uiState.value.cards.firstOrNull { it.id == cardId } ?: return
        if (card.isExpired) return
        _uiState.update { it.copy(selectedCardId = cardId) }
    }

    // CashInScreen 按下 Top Up、且生物辨識／PIN 驗證通過後呼叫
    fun submitTopUp(amount: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingTopUp = true) }

            // 使用者若還沒進過 PaymentMethod 選卡，這裡先補抓一次完整卡片資料（card_type / card_name 只有清單 API 才有）
            val card = _uiState.value.selectedCard ?: loadCreditCardList()?.let { cards ->
                cards.firstOrNull { it.id == _uiState.value.selectedCardId } ?: cards.firstOrNull { it.isPrimary }
            }
            if (card == null) {
                _uiState.update { it.copy(isSubmittingTopUp = false) }
                _eventFlow.emit(UiEvent.ShowToast("尚未綁定付款卡片"))
                return@launch
            }

            val userId = userInfoManager.userInfoFlow.value?.userId.orEmpty()
            val cashBalanceBefore = _uiState.value.cashBalance
            when (val result = paymentRepository.cashIn(userId = userId, card = card)) {
                is NetworkResult.Success -> {
                    val isSuccess = result.data?.status.equals("SUCCESS", ignoreCase = true)
                    val balanceAfter = if (isSuccess) cashBalanceBefore + amount else cashBalanceBefore
                    _uiState.update {
                        it.copy(
                            isSubmittingTopUp = false,
                            cashBalance = balanceAfter,
                            topUpResult = CashInResultInfo(
                                isSuccess = isSuccess,
                                amount = amount,
                                bankName = card.bankName,
                                cardNumber = card.cardNumber,
                                balanceAfter = balanceAfter,
                                transactionId = result.data?.transactionId.orEmpty(),
                                failureReason = if (isSuccess) "" else "Insufficient available balance."
                            )
                        )
                    }
                    _navigationEvent.emit(CashInNavigationEvent.NavigateToResult)
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isSubmittingTopUp = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isSubmittingTopUp = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }
}
