package com.example.newproject.ui

import androidx.lifecycle.ViewModel
import com.example.newproject.network.manager.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val sessionManager: SessionManager
) : ViewModel() {
    // 透過 ViewModel 把它暴露給 Compose 層
    val logoutEvent = sessionManager.logoutEvent
}
