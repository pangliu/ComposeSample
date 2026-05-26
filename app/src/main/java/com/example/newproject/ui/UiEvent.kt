package com.example.newproject.ui

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class ShowDialog(val title: String, val message: String) : UiEvent()
}
