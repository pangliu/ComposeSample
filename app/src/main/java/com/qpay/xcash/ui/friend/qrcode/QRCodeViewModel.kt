package com.qpay.xcash.ui.friend.qrcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.UserInfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QRCodeUiState(
    val userName: String = "",
    val nickName: String = "",
    val userPhone: String = ""
)

@HiltViewModel
class QRCodeViewModel @Inject constructor(
    userInfoManager: UserInfoManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(QRCodeUiState())
    val uiState: StateFlow<QRCodeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { userInfo ->
                _uiState.update {
                    it.copy(
                        userName = userInfo?.userName ?: "",
                        nickName = userInfo?.nickName ?: "",
                        userPhone = userInfo?.userPhone ?: ""
                    )
                }
            }
        }
    }
}
