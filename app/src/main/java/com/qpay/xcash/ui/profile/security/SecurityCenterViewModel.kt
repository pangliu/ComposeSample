package com.qpay.xcash.ui.profile.security

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

data class SecurityCenterUiState(
    val userName: String = "",
    val xcashId: String = "",
    val isVerified: Boolean = true,
    val securityScore: Int = 95
)

@HiltViewModel
class SecurityCenterViewModel @Inject constructor(
    private val userInfoManager: UserInfoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SecurityCenterUiState())
    val uiState: StateFlow<SecurityCenterUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { userInfo ->
                if (userInfo != null) {
                    _uiState.update {
                        it.copy(
                            userName = userInfo.userName,
                            xcashId = userInfo.userPhone
                        )
                    }
                }
            }
        }
    }
}
