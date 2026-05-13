package com.example.newproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.EssentialsManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.OrderHistoryResponse
import com.example.newproject.network.model.response.UserInfoResponse
import com.example.newproject.repository.UserRepository
import com.example.newproject.ui.home.essential.EssentialItem
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
 * 只存資料，不存 UI 狀態；錯誤由 toastEvent 通知，畫面永遠顯示資料。
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
    private val essentialsManager: EssentialsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _myMenuItems = MutableStateFlow(essentialsManager.load())
    val myMenuItems: StateFlow<List<EssentialItem>> = _myMenuItems.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    init {
        fetchUserInfo()
        fetchOrderHistory()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            when (val result = userRepository.fetchUserInfo()) {
                is NetworkResult.Success -> {
                    val data = result.data
                    _uiState.update { it.copy(isLoadingUserInfo = false, userInfo = data ?: UserInfoResponse.empty()) }
                    if (data == null) _toastEvent.emit("無法取得使用者資料")
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    _toastEvent.emit(result.message)
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingUserInfo = false) }
                    _toastEvent.emit(result.e.message ?: "網路異常")
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
                    _toastEvent.emit(result.message)
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingOrders = false) }
                    _toastEvent.emit(result.e.message ?: "網路異常")
                }
            }
        }
    }

    fun saveMyMenu(items: List<EssentialItem>) {
        _myMenuItems.value = items
        essentialsManager.save(items)
    }
}
