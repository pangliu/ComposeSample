package com.qpay.xcash.ui.home.transaction_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.TransactionPreviewHolder
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.OrderHistoryResponse
import com.qpay.xcash.network.model.response.OrderStatus
import com.qpay.xcash.network.model.response.OrderType
import com.qpay.xcash.network.model.response.TransactionDetailResponse
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

data class TransactionDetailUiState(
    val isLoading: Boolean = true,
    val detail: TransactionDetailResponse? = null
)

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val paymentRepository: PaymentRepository,
    private val transactionPreviewHolder: TransactionPreviewHolder
) : ViewModel() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])

    private val _uiState = MutableStateFlow(
        TransactionDetailUiState(
            isLoading = true,
            detail = transactionPreviewHolder.get()?.toPreviewDetail()
        )
    )
    val uiState: StateFlow<TransactionDetailUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        fetchDetail()
    }

    private fun fetchDetail() {
        viewModelScope.launch {
            when (val result = paymentRepository.fetchTransactionDetail(orderId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, detail = result.data) }
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

private fun OrderHistoryResponse.toPreviewDetail(): TransactionDetailResponse {
    val isOutgoing = type == OrderType.OUTGOING
    return TransactionDetailResponse(
        orderId = orderId,
        status = status,
        currency = "PHP",
        amount = amount,
        fee = 0.0,
        payToName = if (isOutgoing) paymentName else "",
        payToAccount = if (isOutgoing) targetAccount else account,
        payToBank = paymentName,
        payFromName = if (isOutgoing) "" else paymentName,
        payFromAccount = if (isOutgoing) account else targetAccount,
        payFromBank = paymentName,
        referenceNo = orderId,
        date = ""
    )
}
