package com.example.newproject.ui.home.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.UserInfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(val userName: String = "")

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userInfoManager: UserInfoManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { info ->
                if (info != null) _uiState.update { it.copy(userName = info.userName) }
            }
        }
    }
}
