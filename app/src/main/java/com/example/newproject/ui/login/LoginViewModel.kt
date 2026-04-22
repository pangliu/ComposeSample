package com.example.newproject.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.repository.AuthRepository
import com.example.newproject.network.model.LoginRequest
import com.example.newproject.network.model.NetworkResult
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
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            
            val request = LoginRequest(
                account = "test",
                password = "1234"
            )
            
            // 使用 AuthRepository 與 NetworkResult 來處理
            val result = authRepository.login(request)
            
            when (result) {
                is NetworkResult.Success -> {
                    // 登入成功
                    _loginState.value = LoginState.Success
                }
                is NetworkResult.Error -> {
                    // 業務邏輯錯誤 (例如帳密錯誤)
                    _loginState.value = LoginState.Error(result.message)
                }
                is NetworkResult.Exception -> {
                    // 網路崩潰、無網路連線等異常
                    _loginState.value = LoginState.Error(result.e.message ?: "登入異常，請檢查網路")
                }
            }
        }
    }
}
