package com.qpay.xcash.ui.transfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.UserInfoManager
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.repository.CardRepository
import com.qpay.xcash.repository.UserRepository
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

data class GeneralTransferUiState(
    val isLoadingCards: Boolean = true,
    val isLoadingFriend: Boolean = false,
    val recipient: FriendResponse? = null,
    // QR code 內帶的 phone_number，API 沒回電話時 RecipientCard 用它顯示
    val scannedPhoneNumber: String = "",
    val cards: List<CreditCardResponse> = emptyList(),
    // null = 選 Xcash Wallet（預設，且不在 API 清單內）
    val selectedCardId: Int? = null,
    // 選 Xcash Wallet 時帶入 WalletTransferScreen account number 欄位用
    val selfPhoneNumber: String = "",
) {
    val isLoading: Boolean get() = isLoadingCards || isLoadingFriend
}

@HiltViewModel
class GeneralTransferViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository,
    private val userRepository: UserRepository,
    userInfoManager: UserInfoManager
) : ViewModel() {

    private val countryCode: String = savedStateHandle.get<String>("countryCode").orEmpty()
    private val phoneNumber: String = savedStateHandle.get<String>("phoneNumber").orEmpty()

    private val _uiState = MutableStateFlow(
        GeneralTransferUiState(
            isLoadingFriend = phoneNumber.isNotEmpty(),
            scannedPhoneNumber = phoneNumber,
            selfPhoneNumber = userInfoManager.userInfoFlow.value?.userPhone.orEmpty()
        )
    )
    val uiState: StateFlow<GeneralTransferUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        if (phoneNumber.isNotEmpty()) findRecipient()
        fetchCreditCardList()
    }

    private fun findRecipient() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFriend = true) }
            when (val result = userRepository.findFriend(countryCode, phoneNumber)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoadingFriend = false, recipient = result.data) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingFriend = false, recipient = null) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingFriend = false, recipient = null) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }

    private fun fetchCreditCardList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCards = true) }
            when (val result = cardRepository.fetchCreditCardList()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoadingCards = false, cards = result.data ?: emptyList()) }
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

    // cardId = null 代表選回 Xcash Wallet
    fun selectAccount(cardId: Int?) {
        if (cardId != null) {
            val card = _uiState.value.cards.firstOrNull { it.id == cardId } ?: return
            if (card.isExpired) return
        }
        _uiState.update { it.copy(selectedCardId = cardId) }
    }
}
