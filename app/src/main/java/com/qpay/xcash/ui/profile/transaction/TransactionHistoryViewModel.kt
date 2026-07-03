package com.qpay.xcash.ui.profile.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.UserInfoManager
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.OrderHistoryResponse
import com.qpay.xcash.network.model.response.OrderType
import com.qpay.xcash.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimeFilter { TODAY, THIS_WEEK, THIS_MONTH }
enum class CategoryFilter { ALL, SPENT, RECEIVED, MERCHANT_SERVICE }

data class TransactionHistoryUiState(
    val balance: Double = 0.0,
    val tokenBalance: Double = 0.0,
    val isLoading: Boolean = true,
    val transactions: List<OrderHistoryResponse> = emptyList(),
    val selectedTimeFilter: TimeFilter = TimeFilter.TODAY,
    val selectedCategory: CategoryFilter = CategoryFilter.ALL,
    val errorMessage: String = ""
) {
    val filteredTransactions: List<OrderHistoryResponse>
        get() = transactions.filter { tx ->
            when (selectedCategory) {
                CategoryFilter.ALL -> true
                CategoryFilter.SPENT -> tx.type == OrderType.OUTGOING
                CategoryFilter.RECEIVED -> tx.type == OrderType.INCOMING
                CategoryFilter.MERCHANT_SERVICE -> tx.type == OrderType.OUTGOING
            }
        }
}

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val userInfoManager: UserInfoManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { userInfo ->
                if (userInfo != null) {
                    _uiState.update {
                        it.copy(balance = userInfo.cashBalance, tokenBalance = userInfo.tokenBalance)
                    }
                }
            }
        }
        fetchTransactions()
    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = userRepository.fetchOrderHistory()) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(isLoading = false, transactions = result.data ?: emptyList())
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
                is NetworkResult.Exception -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.e.message ?: "")
                }
            }
        }
    }

    fun setTimeFilter(filter: TimeFilter) {
        _uiState.update { it.copy(selectedTimeFilter = filter) }
    }

    fun setCategoryFilter(filter: CategoryFilter) {
        _uiState.update { it.copy(selectedCategory = filter) }
    }
}
