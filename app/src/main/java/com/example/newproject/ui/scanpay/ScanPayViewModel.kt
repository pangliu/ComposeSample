package com.example.newproject.ui.scanpay

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class ScanPayUiState(
    val isLoading: Boolean = false
)

@HiltViewModel
class ScanPayViewModel @Inject constructor() : ViewModel()
