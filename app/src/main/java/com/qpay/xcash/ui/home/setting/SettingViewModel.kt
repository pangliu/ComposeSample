package com.qpay.xcash.ui.home.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.UserInfoManager
import com.qpay.xcash.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val userName: String = "",
    val userEmail: String = "",
    val isLoggingOut: Boolean = false
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userInfoManager: UserInfoManager,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { info ->
                if (info != null) _uiState.update {
                    it.copy(userName = info.userName, userEmail = info.userEmail)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true) }
            authRepository.logout()
            _uiState.update { it.copy(isLoggingOut = false) }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
