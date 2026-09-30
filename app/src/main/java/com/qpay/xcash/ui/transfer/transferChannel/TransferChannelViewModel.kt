package com.qpay.xcash.ui.transfer.transferChannel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.TransferChannelItem
import com.qpay.xcash.repository.PaymentRepository
import com.qpay.xcash.ui.UiEvent
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

data class TransferChannelUiState(
    val isLoadingChannels: Boolean = true,
    val eWallets: List<TransferChannelItem> = emptyList(),
    val banks: List<TransferChannelItem> = emptyList(),
    val selectedChannelId: String? = null,
) {
    val isLoading: Boolean get() = isLoadingChannels
    val selectedChannel: TransferChannelItem?
        get() = (eWallets + banks).firstOrNull { it.id == selectedChannelId }
}

@HiltViewModel
class TransferChannelViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferChannelUiState())
    val uiState: StateFlow<TransferChannelUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        fetchTransferChannels()
    }

    private fun fetchTransferChannels() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingChannels = true) }
            when (val result = paymentRepository.fetchTransferChannels()) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingChannels = false,
                            eWallets = result.data?.eWallet ?: emptyList(),
                            banks = result.data?.bank ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingChannels = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingChannels = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }

    fun selectChannel(channelId: String) {
        _uiState.update { it.copy(selectedChannelId = channelId) }
    }
}
