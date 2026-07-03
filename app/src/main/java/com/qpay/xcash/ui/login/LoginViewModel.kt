package com.qpay.xcash.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.CredentialManager
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.request.LoginRequest
import com.qpay.xcash.network.model.request.VerifyOtpRequest
import com.qpay.xcash.repository.AuthRepository
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.utils.BiometricHelper
import com.qpay.xcash.utils.DeviceInfoProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val loginError: String? = null
)

sealed class LoginNavigationEvent {
    object Success : LoginNavigationEvent()
    object PromptBiometricEnroll : LoginNavigationEvent()
    data class NeedsVerification(val phone: String) : LoginNavigationEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val credentialManager: CredentialManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<LoginNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _hasSavedCredentials = MutableStateFlow(credentialManager.hasCredentials())
    val hasSavedCredentials: StateFlow<Boolean> = _hasSavedCredentials.asStateFlow()

    private var pendingPhone: String = ""
    private var pendingPassword: String = ""

    fun login(phoneNum: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }

            val request = LoginRequest(
                account = phoneNum,
                password = password,
                deviceId = deviceInfoProvider.deviceId
            )

            when (val result = authRepository.login(request)) {
                is NetworkResult.Success -> {
                    Log.e("LoginViewModel", "credentialManager: ${credentialManager.hasCredentials()}")
                    Log.e("LoginViewModel", "biometricHelper: ${BiometricHelper.isAvailable(context)}")
                    _uiState.update { it.copy(isLoading = false) }
                    if (!credentialManager.hasCredentials() && BiometricHelper.isAvailable(context)) {
                        pendingPhone = phoneNum
                        pendingPassword = password
                        _navigationEvent.emit(LoginNavigationEvent.PromptBiometricEnroll)
                    } else {
                        _navigationEvent.emit(LoginNavigationEvent.Success)
                    }
                }
                is NetworkResult.Error -> {
                    if (result.code == 2001) {
                        pendingPhone = phoneNum
                        pendingPassword = password
                        _uiState.update { it.copy(isLoading = false) }
                        _navigationEvent.emit(LoginNavigationEvent.NeedsVerification(phoneNum))
                    } else {
                        _uiState.update { it.copy(isLoading = false, loginError = result.message) }
                    }
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoading = false, loginError = result.e.message ?: "登入異常，請檢查網路") }
                }
            }
        }
    }

    fun enrollBiometric() {
        credentialManager.saveCredentials(pendingPhone, pendingPassword)
        _hasSavedCredentials.value = true
        clearPending()
        viewModelScope.launch { _navigationEvent.emit(LoginNavigationEvent.Success) }
    }

    fun skipBiometricEnroll() {
        clearPending()
        viewModelScope.launch { _navigationEvent.emit(LoginNavigationEvent.Success) }
    }

    fun loginWithStoredCredentials() {
        val phone = credentialManager.getPhone() ?: return
        val password = credentialManager.getPassword() ?: return
        login(phone, password)
    }

    fun resetState() {
        _uiState.update { it.copy(loginError = null) }
    }

    fun verifyOtp(phone: String, otp: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val request = VerifyOtpRequest(
                phone = phone,
                otp = otp,
                deviceId = deviceInfoProvider.deviceId
            )
            when (val result = authRepository.verifyOtp(request)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    if (pendingPhone.isNotEmpty() && !credentialManager.hasCredentials() && BiometricHelper.isAvailable(context)) {
                        _navigationEvent.emit(LoginNavigationEvent.PromptBiometricEnroll)
                    } else {
                        clearPending()
                        _navigationEvent.emit(LoginNavigationEvent.Success)
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }

    private fun clearPending() {
        pendingPhone = ""
        pendingPassword = ""
    }
}
