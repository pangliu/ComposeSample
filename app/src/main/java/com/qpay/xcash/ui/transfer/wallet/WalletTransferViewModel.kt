package com.qpay.xcash.ui.transfer.wallet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.UserInfoManager
import com.qpay.xcash.network.model.NetworkResult
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

// TransferResultScreen 顯示用的轉帳結果
data class TransferResultInfo(
    val isSuccess: Boolean,
    val amount: Long,
    val recipientName: String,
    val recipientAccountNumber: String,
    val balanceAfter: Double,
    val transactionId: String,
    val failureReason: String = "",
    val timestampMillis: Long = System.currentTimeMillis()
)

// ConfirmTransferScreen 送出後的一次性導航事件；結果資料本身放在 WalletTransferUiState（TransferResultScreen 讀取用）
sealed class WalletTransferNavigationEvent {
    data object NavigateToResult : WalletTransferNavigationEvent()
}

data class WalletTransferUiState(
    // GeneralTransferScreen 選定的帳戶名稱（Xcash Wallet / 銀行名稱）與手續費
    val accountName: String = "",
    val fee: Int = 0,
    // 選卡片帶卡號、選 Xcash Wallet 帶使用者手機號碼，預填 account number 欄位
    val accountNumber: String = "",
    // 從掃碼進入時鎖定 accountName / account number 欄位：皆由 QR code 帶入，不可編輯且隱藏下拉箭頭與掃碼 icon
    val isAccountNumberLocked: Boolean = false,
    // 使用者輸入的轉帳金額，與 ConfirmTransferScreen 共用同一個 ViewModel 所以放在 UiState
    val amount: String = "",
    val availableBalance: Double = 0.0,
    // 使用者自己的預設帳戶（UserInfoManager），ConfirmTransferScreen 第一張 AccountSummaryCard 顯示用
    val selfDefaultBankName: String = "",
    val selfDefaultCardNumber: String = "",
    // 使用者自己的手機號碼（Xcash 帳號），TransferResultScreen 付款方卡片顯示用
    val selfPhoneNumber: String = "",
    // TODO: 目前沒有對應 API，先用固定值
    val transferBonusRemaining: Int = 10,
    val isSubmittingTransfer: Boolean = false,
    val transferResult: TransferResultInfo? = null,
) {
    val isLoading: Boolean get() = isSubmittingTransfer

    // 收款方式、帳號、金額三個欄位都有值才可按 Next
    val isNextEnabled: Boolean
        get() = accountName.isNotBlank() &&
            accountNumber.isNotBlank() &&
            (amount.toLongOrNull() ?: 0L) > 0L
}

@HiltViewModel
class WalletTransferViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userInfoManager: UserInfoManager,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WalletTransferUiState(
            accountName = savedStateHandle.get<String>("accountName").orEmpty(),
            fee = savedStateHandle.get<Int>("fee") ?: 0,
            accountNumber = savedStateHandle.get<String>("accountNumber").orEmpty(),
            isAccountNumberLocked = savedStateHandle.get<Boolean>("accountNumberLocked") ?: false,
            availableBalance = userInfoManager.userInfoFlow.value?.cashBalance ?: 0.0,
            selfDefaultBankName = userInfoManager.userInfoFlow.value?.defaultBankName.orEmpty(),
            selfDefaultCardNumber = userInfoManager.userInfoFlow.value?.defaultCardNumber.orEmpty(),
            selfPhoneNumber = userInfoManager.userInfoFlow.value?.userPhone.orEmpty()
        )
    )
    val uiState: StateFlow<WalletTransferUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<WalletTransferNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<WalletTransferNavigationEvent> = _navigationEvent.asSharedFlow()

    fun updateAmount(value: String) {
        _uiState.update { it.copy(amount = value) }
    }

    fun updateAccountNumber(value: String) {
        _uiState.update { it.copy(accountNumber = value) }
    }

    // TransferChannelScreen 選定渠道後回填收款方式
    fun selectReceivingChannel(channelName: String) {
        _uiState.update { it.copy(accountName = channelName) }
    }

    // ConfirmTransferScreen 按下 Next、且生物辨識／PIN 驗證通過後呼叫
    fun submitTransfer() {
        val state = _uiState.value
        if (state.isSubmittingTransfer) return
        val amount = state.amount.toLongOrNull() ?: 0L

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingTransfer = true) }

            when (val result = paymentRepository.transfer(
                accountNumber = state.accountNumber,
                amount = state.amount
            )) {
                is NetworkResult.Success -> {
                    val isSuccess = result.data?.status.equals("SUCCESS", ignoreCase = true)
                    val balanceAfter = if (isSuccess) {
                        state.availableBalance - amount - state.fee
                    } else {
                        state.availableBalance
                    }
                    _uiState.update {
                        it.copy(
                            isSubmittingTransfer = false,
                            availableBalance = balanceAfter,
                            transferResult = TransferResultInfo(
                                isSuccess = isSuccess,
                                amount = amount,
                                recipientName = state.accountName,
                                recipientAccountNumber = state.accountNumber,
                                balanceAfter = balanceAfter,
                                transactionId = result.data?.transactionId.orEmpty(),
                                failureReason = if (isSuccess) "" else "Insufficient available balance."
                            )
                        )
                    }
                    _navigationEvent.emit(WalletTransferNavigationEvent.NavigateToResult)
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isSubmittingTransfer = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isSubmittingTransfer = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }
}
