package com.example.newproject.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.CredentialManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.request.LoginRequest
import com.example.newproject.network.model.request.VerifyOtpRequest
import com.example.newproject.repository.AuthRepository
import com.example.newproject.utils.BiometricHelper
import com.example.newproject.utils.DeviceInfoProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    object PromptBiometricEnroll : LoginState()
    data class Error(val message: String) : LoginState()
    data class NeedsVerification(val phone: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val credentialManager: CredentialManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _hasSavedCredentials = MutableStateFlow(credentialManager.hasCredentials())
    val hasSavedCredentials: StateFlow<Boolean> = _hasSavedCredentials.asStateFlow()

    private var pendingPhone: String = ""
    private var pendingPassword: String = ""

    fun login(phoneNum: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val request = LoginRequest(
                account = phoneNum,
                password = password,
                deviceId = deviceInfoProvider.deviceId
            )

            when (val result = authRepository.login(request)) {
                is NetworkResult.Success -> {
                    Log.e("LoginViewModel", "credentialManager: ${credentialManager.hasCredentials()}")
                    Log.e("LoginViewModel", "biometricHelper: ${BiometricHelper.isAvailable(context)}")
                    if (!credentialManager.hasCredentials() && BiometricHelper.isAvailable(context)) {
                        pendingPhone = phoneNum
                        pendingPassword = password
                        _loginState.value = LoginState.PromptBiometricEnroll
                    } else {
                        _loginState.value = LoginState.Success
                    }
                }
                is NetworkResult.Error -> {
                    if (result.code == 2001) {
                        pendingPhone = phoneNum
                        pendingPassword = password
                        _loginState.value = LoginState.NeedsVerification(phoneNum)
                    } else {
                        _loginState.value = LoginState.Error(result.message)
                    }
                }
                is NetworkResult.Exception -> {
                    _loginState.value = LoginState.Error(result.e.message ?: "登入異常，請檢查網路")
                }
            }
        }
    }

    fun enrollBiometric() {
        credentialManager.saveCredentials(pendingPhone, pendingPassword)
        _hasSavedCredentials.value = true
        clearPending()
        _loginState.value = LoginState.Success
    }

    fun skipBiometricEnroll() {
        clearPending()
        _loginState.value = LoginState.Success
    }

    fun loginWithStoredCredentials() {
        val phone = credentialManager.getPhone() ?: return
        val password = credentialManager.getPassword() ?: return
        login(phone, password)
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }

    fun verifyOtp(phone: String, otp: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val request = VerifyOtpRequest(
                phone = phone,
                otp = otp,
                deviceId = deviceInfoProvider.deviceId
            )
            when (val result = authRepository.verifyOtp(request)) {
                is NetworkResult.Success -> {
                    if (pendingPhone.isNotEmpty() && !credentialManager.hasCredentials() && BiometricHelper.isAvailable(context)) {
                        _loginState.value = LoginState.PromptBiometricEnroll
                    } else {
                        clearPending()
                        _loginState.value = LoginState.Success
                    }
                    onSuccess()
                }
                is NetworkResult.Error -> {
                    _loginState.value = LoginState.NeedsVerification(phone)
                    onError(result.message)
                }
                is NetworkResult.Exception -> {
                    _loginState.value = LoginState.NeedsVerification(phone)
                    onError(result.e.message ?: "網路異常")
                }
            }
        }
    }

    private fun clearPending() {
        pendingPhone = ""
        pendingPassword = ""
    }
}
