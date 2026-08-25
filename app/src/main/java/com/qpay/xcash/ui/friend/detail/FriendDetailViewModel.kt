package com.qpay.xcash.ui.friend.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.RecentFriendManager
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

val FRIEND_DETAIL_TAG_OPTIONS = listOf("Friend", "Family", "Besties")

data class FriendDetailUiState(
    val friend: FriendResponse? = null,
    val nameInput: String = "",
    val selectedTag: String? = null,
    val memoInput: String = "",
    val availableTags: List<String> = FRIEND_DETAIL_TAG_OPTIONS
)

@HiltViewModel
class FriendDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val recentFriendManager: RecentFriendManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendDetailUiState())
    val uiState: StateFlow<FriendDetailUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _navigateBack = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateBack: SharedFlow<Unit> = _navigateBack.asSharedFlow()

    init {
        userRepository.selectedFriend?.let { friend ->
            _uiState.update {
                it.copy(
                    friend = friend,
                    nameInput = friend.name,
                    selectedTag = friend.tagLabel,
                    memoInput = friend.memo.orEmpty()
                )
            }
            recentFriendManager.save(friend)
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(nameInput = value) }
    }

    fun onNameReset() {
        _uiState.update { it.copy(nameInput = it.friend?.name.orEmpty()) }
    }

    fun onNameConfirm() {
        _uiState.update { state ->
            state.copy(friend = state.friend?.copy(name = state.nameInput))
        }
    }

    fun onTagSelected(tag: String) {
        _uiState.update { it.copy(selectedTag = if (it.selectedTag == tag) null else tag) }
    }

    fun onMemoChange(value: String) {
        _uiState.update { it.copy(memoInput = value) }
    }

    fun saveFriend() {
        _uiState.update { state ->
            val updated = state.friend?.copy(
                name = state.nameInput,
                tagLabel = state.selectedTag,
                memo = state.memoInput
            )
            userRepository.selectedFriend = updated
            state.copy(friend = updated)
        }
        viewModelScope.launch { _eventFlow.emit(UiEvent.ShowToast("Saved")) }
    }

    fun removeFriend() {
        userRepository.selectedFriend = null
        viewModelScope.launch { _navigateBack.emit(Unit) }
    }
}
