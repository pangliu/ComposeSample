package com.qpay.xcash.ui.transfer.wallet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.qpay.xcash.network.manager.UserInfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class WalletTransferUiState(
    // GeneralTransferScreen 選定的帳戶名稱（Xcash Wallet / 銀行名稱）與手續費
    val accountName: String = "",
    val fee: Int = 0,
    // 選卡片帶卡號、選 Xcash Wallet 帶使用者手機號碼，預填 account number 欄位
    val accountNumber: String = "",
    val availableBalance: Double = 0.0,
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
            availableBalance = userInfoManager.userInfoFlow.value?.cashBalance ?: 0.0
        )
    )
    val uiState: StateFlow<WalletTransferUiState> = _uiState.asStateFlow()
}
