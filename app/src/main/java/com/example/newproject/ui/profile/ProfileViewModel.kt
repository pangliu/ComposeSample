package com.example.newproject.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.UserInfoResponse
import com.example.newproject.repository.AuthRepository
import com.example.newproject.repository.UserRepository
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
    val userName: String = "",
    val xcashId: String = "",
    val inviteCode: String = "G12345",
    val badgeCount: Int = 8,
    val isVerified: Boolean = true
) {
    val isLoading: Boolean get() = isLoadingUserInfo
}

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Error(val message: String) : ProfileState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    init {
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            when (val result = userRepository.fetchUserInfo()) {
                is NetworkResult.Success -> {
                    val data = result.data ?: UserInfoResponse.empty()
                    _uiState.update {
                        it.copy(
                            isLoadingUserInfo = false,
                            userName = data.userName,
                            xcashId = data.userPhone
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    _toastEvent.emit(result.message)
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    _toastEvent.emit(result.e.message ?: "網路異常")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            when (val result = authRepository.logout()) {
                is NetworkResult.Success   -> sessionManager.triggerLogout()
                is NetworkResult.Error     -> _profileState.value = ProfileState.Error(result.message)
                is NetworkResult.Exception -> _profileState.value = ProfileState.Error(result.e.message ?: "網路異常")
            }
        }
    }

    fun resetState() {
        _profileState.value = ProfileState.Idle
    }
}
