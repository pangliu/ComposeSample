package com.example.newproject.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.manager.UserInfoManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.repository.AuthRepository
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

data class ProfileUiState(
    val isLoadingUserInfo: Boolean = true,
    val isLoggingOut: Boolean = false,
    val userName: String = "",
    val xcashId: String = "",
    val inviteCode: String = "G12345",
    val badgeCount: Int = 8,
    val isVerified: Boolean = true
) {
    val isLoading: Boolean get() = isLoadingUserInfo || isLoggingOut
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userInfoManager: UserInfoManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        // 有 cache 時立即顯示，跳過 loading 狀態
        userInfoManager.userInfoFlow.value?.let { cached ->
            _uiState.update { it.copy(
                isLoadingUserInfo = false,
                userName = cached.userName,
                xcashId = cached.userPhone
            )}
        }
        // 訂閱後續更新（API 刷新後自動同步）
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { userInfo ->
                if (userInfo != null) {
                    _uiState.update { it.copy(userName = userInfo.userName, xcashId = userInfo.userPhone) }
                }
            }
        }
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            when (val result = userRepository.fetchUserInfo()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true) }
            when (val result = authRepository.logout()) {
                is NetworkResult.Success -> sessionManager.triggerLogout()
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoggingOut = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoggingOut = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }
}
