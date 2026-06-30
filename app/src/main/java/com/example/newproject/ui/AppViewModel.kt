package com.example.newproject.ui

import androidx.lifecycle.ViewModel
import com.example.newproject.network.manager.SessionManager
import com.example.newproject.network.manager.ThemeManager
import com.example.newproject.ui.theme.AppColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val sessionManager: SessionManager,
    private val themeManager: ThemeManager
) : ViewModel() {
    // 透過 ViewModel 把它暴露給 Compose 層
    val logoutEvent = sessionManager.logoutEvent

    private val _currentColors = MutableStateFlow(themeManager.load())
    val currentColors: StateFlow<AppColors> = _currentColors

    fun setTheme(colors: AppColors) {
        themeManager.save(colors)
        _currentColors.value = colors
    }
}
