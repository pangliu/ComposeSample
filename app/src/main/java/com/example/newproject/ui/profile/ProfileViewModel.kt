package com.example.newproject.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Error(val message: String) : ProfileState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

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
