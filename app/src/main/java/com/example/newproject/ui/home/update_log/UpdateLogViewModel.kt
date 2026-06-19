package com.example.newproject.ui.home.update_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.UpdateLogResponse
import com.example.newproject.repository.UserRepository
import com.example.newproject.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpdateLogUiState(
    val isLoading: Boolean = true,
    val logs: List<UpdateLogResponse> = emptyList()
)

@HiltViewModel
class UpdateLogViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateLogUiState())
    val uiState: StateFlow<UpdateLogUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        fetchUpdateLog()
    }

    private fun fetchUpdateLog() {
        viewModelScope.launch {
            when (val result = userRepository.fetchUpdateLog()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, logs = result.data ?: emptyList()) }
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
}
