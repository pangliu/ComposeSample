package com.example.newproject.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WelcomeNavigationEvent {
    object Idle : WelcomeNavigationEvent()
    object GoToHome : WelcomeNavigationEvent()
    object GoToLogin : WelcomeNavigationEvent()
}

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _navigationEvent = MutableStateFlow<WelcomeNavigationEvent>(WelcomeNavigationEvent.Idle)
    val navigationEvent: StateFlow<WelcomeNavigationEvent> = _navigationEvent.asStateFlow()

    init {
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            // 這裡可以加上一個短暫的 delay 讓用戶看到歡迎介面動畫 (Splash)
            // 實務上也可以趁這個時候檢查 App 版本號是否需要強制更新等邏輯
            delay(1000)
            
            val token = tokenManager.getAccessToken()
//            if (!token.isNullOrEmpty()) {
//                // 有存過 token，直接去首頁
//                _navigationEvent.value = WelcomeNavigationEvent.GoToHome
//            } else {
                // 沒有 token，代表沒登入過
                _navigationEvent.value = WelcomeNavigationEvent.GoToLogin
//            }
        }
    }
}
