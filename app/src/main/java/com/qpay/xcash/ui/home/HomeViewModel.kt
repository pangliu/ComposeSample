package com.qpay.xcash.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.EssentialsManager
import com.qpay.xcash.network.manager.TransactionPreviewHolder
import com.qpay.xcash.network.manager.UserInfoManager
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.OrderHistoryResponse
import com.qpay.xcash.network.model.response.UserInfoResponse
import com.qpay.xcash.repository.UserRepository
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.home.essential.EssentialItem
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

/**
 * HomeScreen 的統一 UI 資料。
 * 只存資料，不存 UI 狀態；錯誤由 eventFlow 通知，畫面永遠顯示資料。
 * 新增初始 API 時，加一個 isLoading 欄位與對應資料欄位即可。
 */
data class HomeUiState(
    val isLoadingUserInfo: Boolean = true,
    val isLoadingOrders: Boolean = true,
    val userInfo: UserInfoResponse = UserInfoResponse.empty(),
    val orders: List<OrderHistoryResponse> = emptyList(),
) {
    val isLoading: Boolean get() = isLoadingUserInfo || isLoadingOrders
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userInfoManager: UserInfoManager,
    private val essentialsManager: EssentialsManager,
    private val transactionPreviewHolder: TransactionPreviewHolder
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _myMenuItems = MutableStateFlow(essentialsManager.load())
    val myMenuItems: StateFlow<List<EssentialItem>> = _myMenuItems.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        // 有 cache 時立即顯示，跳過 loading 狀態
        userInfoManager.userInfoFlow.value?.let { cached ->
            _uiState.update { it.copy(isLoadingUserInfo = false, userInfo = cached) }
        }
        // 訂閱後續更新（API 刷新後自動同步）
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { userInfo ->
                if (userInfo != null) {
                    _uiState.update { it.copy(userInfo = userInfo) }
                }
            }
        }
        fetchUserInfo()
        fetchOrderHistory()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            when (val result = userRepository.fetchUserInfo()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    if (result.data == null) _eventFlow.emit(UiEvent.ShowToast("無法取得使用者資料"))
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }

    private fun fetchOrderHistory() {
        viewModelScope.launch {
            when (val result = userRepository.fetchOrderHistory()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoadingOrders = false, orders = result.data ?: emptyList()) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingOrders = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingOrders = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }

    fun selectOrder(order: OrderHistoryResponse) {
        transactionPreviewHolder.set(order)
    }

    fun saveMyMenu(items: List<EssentialItem>) {
        _myMenuItems.value = items
        essentialsManager.save(items)
    }
}
