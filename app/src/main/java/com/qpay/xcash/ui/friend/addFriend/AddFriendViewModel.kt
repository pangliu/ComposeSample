package com.qpay.xcash.ui.friend.addFriend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.network.model.response.FriendResponse
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

data class AddFriendUiState(
    val isLoadingFindFriend: Boolean = false,
    val isLoadingAddFriend: Boolean = false,
    val foundFriend: FriendResponse? = null,
) {
    val isLoading: Boolean get() = isLoadingFindFriend || isLoadingAddFriend
}

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFriendUiState())
    val uiState: StateFlow<AddFriendUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    fun findFriend(countryCode: String, phoneNumber: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFindFriend = true) }
            when (val result = userRepository.findFriend(countryCode, phoneNumber)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoadingFindFriend = false, foundFriend = result.data) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingFindFriend = false, foundFriend = null) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingFindFriend = false, foundFriend = null) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }

    fun clearFoundFriend() {
        _uiState.update { it.copy(foundFriend = null) }
    }

    fun onAddFriendClick() {
        val friendId = _uiState.value.foundFriend?.id ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingAddFriend = true) }
            when (val result = userRepository.addFriend(friendId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoadingAddFriend = false, foundFriend = null) }
                    _eventFlow.emit(UiEvent.ShowToast("好友新增成功"))
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingAddFriend = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoadingAddFriend = false) }
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }
}
