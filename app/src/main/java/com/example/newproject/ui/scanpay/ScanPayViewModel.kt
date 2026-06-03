package com.example.newproject.ui.scanpay

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ScanPayUiState(
    val recipientUsername: String = "",
    val recipientName: String = "",
    val amount: String = ""
)

@HiltViewModel
class ScanPayViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ScanPayUiState())
    val uiState: StateFlow<ScanPayUiState> = _uiState.asStateFlow()

    fun setRecipientInfo(username: String, name: String) {
        _uiState.update { it.copy(recipientUsername = username, recipientName = name) }
    }

    fun setAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }
}
