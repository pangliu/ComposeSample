package com.example.newproject.ui.scanpay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.UserInfoManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.FriendResponse
import com.example.newproject.repository.PaymentRepository
import com.example.newproject.repository.UserRepository
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

sealed class ScanPayNavigationEvent {
    object PaymentSuccess : ScanPayNavigationEvent()
}

data class ScanPayUiState(
    val myUserName: String = "",
    val myNickName: String = "",
    val balance: Double = 0.0,
    val tokenBalance: Double = 0.0,
    val recipientAccount: String = "",
    val recipientNickName: String = "",
    val recipientName: String = "",
    val amount: String = "",
    val isConfirming: Boolean = false,
    val confirmErrorMessage: String = "",
    val friendList: List<FriendResponse> = emptyList(),
    val selectedFriendList: List<FriendResponse> = emptyList(),
    val confirmedSplitPartners: List<FriendResponse> = emptyList(),
    val isFetchingFriends: Boolean = false,
    val showSelectPartnerDialog: Boolean = false,
    val showSplitBillDialog: Boolean = false
)

@HiltViewModel
class ScanPayViewModel @Inject constructor(
    private val userInfoManager: UserInfoManager,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScanPayUiState())
    val uiState: StateFlow<ScanPayUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ScanPayNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<ScanPayNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { userInfo ->
                _uiState.update { it.copy(
                    myUserName = userInfo?.userName ?: "",
                    myNickName = userInfo?.nickName ?: "",
                    balance = userInfo?.cashBalance ?: 0.0,
                    tokenBalance = userInfo?.tokenBalance ?: 0.0
                )}
            }
        }
    }

    fun setRecipientInfo(account: String, nickName: String, name: String) {
        _uiState.update { it.copy(
            recipientAccount = account,
            recipientNickName = nickName,
            recipientName = name
        )}
    }

    fun setAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun fetchFriendListAndShowDialog() {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingFriends = true) }
            when (val result = userRepository.fetchFriendList()) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(
                        isFetchingFriends = false,
                        friendList = result.data ?: emptyList(),
                        showSelectPartnerDialog = true
                    )
                }
                else -> _uiState.update { it.copy(isFetchingFriends = false) }
            }
        }
    }

    fun dismissSelectPartnerDialog() {
        _uiState.update { it.copy(showSelectPartnerDialog = false) }
    }

    fun onPartnersConfirmed(selected: List<FriendResponse>) {
        _uiState.update {
            it.copy(
                showSelectPartnerDialog = false,
                selectedFriendList = selected,
                showSplitBillDialog = true
            )
        }
    }

    fun dismissSplitBillDialog() {
        _uiState.update { it.copy(showSplitBillDialog = false) }
    }

    fun confirmSplitBill() {
        _uiState.update { it.copy(
            confirmedSplitPartners = it.selectedFriendList,
            showSplitBillDialog = false
        )}
    }

    fun editSplitBill() {
        _uiState.update { it.copy(showSplitBillDialog = true) }
    }

    fun cancelSplitBill() {
        _uiState.update { it.copy(confirmedSplitPartners = emptyList()) }
    }

    fun clearSplitBillState() {
        _uiState.update { it.copy(
            friendList = emptyList(),
            selectedFriendList = emptyList(),
            confirmedSplitPartners = emptyList(),
            showSelectPartnerDialog = false,
            showSplitBillDialog = false
        )}
    }

    fun confirmPayment() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isConfirming = true) }
            when (val result = paymentRepository.confirmPayment(
                recipientAccount = state.recipientAccount,
                recipientName = state.recipientName,
                amount = state.amount
            )) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isConfirming = false, confirmErrorMessage = "") }
                    _navigationEvent.emit(ScanPayNavigationEvent.PaymentSuccess)
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isConfirming = false, confirmErrorMessage = result.message) }
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isConfirming = false, confirmErrorMessage = result.e.message ?: "網路異常") }
                }
            }
        }
    }
}
