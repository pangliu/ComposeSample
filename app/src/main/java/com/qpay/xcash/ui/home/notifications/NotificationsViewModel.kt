package com.qpay.xcash.ui.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.NotificationResponse
import com.qpay.xcash.network.model.response.NotificationType
import com.qpay.xcash.repository.UserRepository
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

enum class NotificationTab { ALL, UNREAD, READ }
enum class NotificationCategoryFilter { ALL, SYSTEM, ACTIVITY, PROMO }

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val notifications: List<NotificationResponse> = emptyList(),
    val selectedTab: NotificationTab = NotificationTab.ALL,
    val selectedCategory: NotificationCategoryFilter = NotificationCategoryFilter.ALL
) {
    val filteredNotifications: List<NotificationResponse>
        get() = notifications.filter { notification ->
            val matchesTab = when (selectedTab) {
                NotificationTab.ALL -> true
                NotificationTab.UNREAD -> !notification.isRead
                NotificationTab.READ -> notification.isRead
            }
            val matchesCategory = when (selectedCategory) {
                NotificationCategoryFilter.ALL -> true
                NotificationCategoryFilter.SYSTEM -> notification.type == NotificationType.SYSTEM
                NotificationCategoryFilter.ACTIVITY -> notification.type == NotificationType.ACTIVITY
                NotificationCategoryFilter.PROMO -> notification.type == NotificationType.PROMO
            }
            matchesTab && matchesCategory
        }
}

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = userRepository.fetchNotifications()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, notifications = result.data ?: emptyList()) }
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

    fun setTab(tab: NotificationTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setCategoryFilter(filter: NotificationCategoryFilter) {
        _uiState.update { it.copy(selectedCategory = filter) }
    }
}
