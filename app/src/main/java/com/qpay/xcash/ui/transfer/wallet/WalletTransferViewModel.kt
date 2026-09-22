package com.qpay.xcash.ui.transfer.wallet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.qpay.xcash.network.manager.UserInfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class WalletTransferUiState(
    // GeneralTransferScreen 選定的帳戶名稱（Xcash Wallet / 銀行名稱）與手續費
    val accountName: String = "",
    val fee: Int = 0,
    // 選卡片帶卡號、選 Xcash Wallet 帶使用者手機號碼，預填 account number 欄位
    val accountNumber: String = "",
    // 從掃碼進入時鎖定 account number 欄位，不可編輯且隱藏掃碼 icon
    val isAccountNumberLocked: Boolean = false,
    // 使用者輸入的轉帳金額，與 ConfirmTransferScreen 共用同一個 ViewModel 所以放在 UiState
    val amount: String = "",
    val availableBalance: Double = 0.0,
    // 使用者自己的預設帳戶（UserInfoManager），ConfirmTransferScreen 第一張 AccountSummaryCard 顯示用
    val selfDefaultBankName: String = "",
    val selfDefaultCardNumber: String = "",
    // TODO: 目前沒有對應 API，先用固定值
    val transferBonusRemaining: Int = 10,
)

@HiltViewModel
class WalletTransferViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userInfoManager: UserInfoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WalletTransferUiState(
            accountName = savedStateHandle.get<String>("accountName").orEmpty(),
            fee = savedStateHandle.get<Int>("fee") ?: 0,
            accountNumber = savedStateHandle.get<String>("accountNumber").orEmpty(),
            isAccountNumberLocked = savedStateHandle.get<Boolean>("accountNumberLocked") ?: false,
            availableBalance = userInfoManager.userInfoFlow.value?.cashBalance ?: 0.0,
            selfDefaultBankName = userInfoManager.userInfoFlow.value?.defaultBankName.orEmpty(),
            selfDefaultCardNumber = userInfoManager.userInfoFlow.value?.defaultCardNumber.orEmpty()
        )
    )
    val uiState: StateFlow<WalletTransferUiState> = _uiState.asStateFlow()

    fun updateAmount(value: String) {
        _uiState.update { it.copy(amount = value) }
    }

    fun updateAccountNumber(value: String) {
        _uiState.update { it.copy(accountNumber = value) }
    }
}
