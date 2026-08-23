package com.qpay.xcash.ui.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.repository.UserRepository
import com.qpay.xcash.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FriendNavigationEvent {
    object ToFriendList : FriendNavigationEvent()
    object ToEmptyList : FriendNavigationEvent()
}

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<FriendNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<FriendNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    fun onMyListClick() {
        viewModelScope.launch {
            when (val result = userRepository.fetchFriendList()) {
                is NetworkResult.Success -> {
                    val friends = result.data ?: emptyList()
                    _navigationEvent.emit(
                        if (friends.isEmpty()) FriendNavigationEvent.ToEmptyList
                        else FriendNavigationEvent.ToFriendList
                    )
                }
                is NetworkResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowToast(result.message))
                }
                is NetworkResult.Exception -> {
                    _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
                }
            }
        }
    }
}
