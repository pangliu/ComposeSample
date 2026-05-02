package com.example.newproject.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.repository.AuthRepository
import com.example.newproject.network.model.request.LoginRequest
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.utils.DeviceInfoProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
    data class NeedsVerification(val phone: String) : LoginState() // 🌟 新增需要驗證狀態
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val deviceInfoProvider: DeviceInfoProvider
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(phoneNum: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            
            // 🌟 透過獨立的 DeviceInfoProvider 取得真實的 Device ID
            val deviceId = deviceInfoProvider.deviceId

            val request = LoginRequest(
                account = "verify",
                password = password,
                deviceId = deviceId
            )
            
            // 使用 AuthRepository 與 NetworkResult 來處理
            val result = authRepository.login(request)
            
            when (result) {
                is NetworkResult.Success -> {
                    // 登入成功
                    _loginState.value = LoginState.Success
                }
                is NetworkResult.Error -> {
                    // 🌟 判斷是否為 2001 (需要驗證 DeviceId)
                    if (result.code == 2001) {
                        _loginState.value = LoginState.NeedsVerification(phoneNum)
                    } else {
                        // 業務邏輯錯誤 (例如帳密錯誤)
                        _loginState.value = LoginState.Error(result.message)
                    }
                }
                is NetworkResult.Exception -> {
                    // 網路崩潰、無網路連線等異常
                    _loginState.value = LoginState.Error(result.e.message ?: "登入異常，請檢查網路")
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
