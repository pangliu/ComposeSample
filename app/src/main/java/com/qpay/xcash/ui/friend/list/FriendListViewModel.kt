package com.qpay.xcash.ui.friend.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.manager.RecentFriendManager
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

enum class FriendListTab { ALL, FAVORITES, RECENT }

enum class FriendListSortOrder { A_TO_Z, Z_TO_A, RECENTLY_CONTACTED }

// "All" chip 固定顯示在最前面（見 FriendListCategoryChips），這裡只需排序 tagLabel 分類；不在清單內的標籤排到最後
private val CATEGORY_DISPLAY_ORDER = listOf("Family", "Besties")

data class FriendListUiState(
    val isLoadingFriends: Boolean = true,
    val isRefreshing: Boolean = false,
    val friends: List<FriendResponse> = emptyList(),
    val recentFriends: List<FriendResponse> = emptyList(),
    val searchQuery: String = "",
    val selectedTab: FriendListTab = FriendListTab.ALL,
    val selectedCategory: String? = null, // null = "All"
    val sortOrder: FriendListSortOrder = FriendListSortOrder.A_TO_Z,
) {
    val isLoading: Boolean get() = isLoadingFriends

    val categories: List<String>
        get() = friends.mapNotNull { it.tagLabel }.distinct().sortedBy { tag ->
            CATEGORY_DISPLAY_ORDER.indexOf(tag).let { if (it == -1) CATEGORY_DISPLAY_ORDER.size else it }
        }

    val filteredFriends: List<FriendResponse>
        get() {
            var result = when (selectedTab) {
                FriendListTab.ALL -> friends
                FriendListTab.FAVORITES -> friends.filter { it.isFavorite }
                FriendListTab.RECENT -> recentFriends
            }
            selectedCategory?.let { category ->
                result = result.filter { it.tagLabel == category }
            }
            if (searchQuery.isNotBlank()) {
                result = result.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                        it.nickName.contains(searchQuery, ignoreCase = true)
                }
            }
            return when (sortOrder) {
                FriendListSortOrder.A_TO_Z -> result.sortedBy { it.name }
                FriendListSortOrder.Z_TO_A -> result.sortedByDescending { it.name }
                FriendListSortOrder.RECENTLY_CONTACTED -> result.sortedWith(
                    compareByDescending<FriendResponse> { it.isRecent }.thenBy { it.name }
                )
            }
        }
}

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val recentFriendManager: RecentFriendManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendListUiState())
    val uiState: StateFlow<FriendListUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        fetchFriendList()
        loadRecentFriends()
    }

    private fun loadRecentFriends() {
        _uiState.update { it.copy(recentFriends = recentFriendManager.load().asReversed()) }
    }

    private fun fetchFriendList() {
        viewModelScope.launch {
            loadFriends()
            _uiState.update { it.copy(isLoadingFriends = false) }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadFriends()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun loadFriends() {
        when (val result = userRepository.fetchFriendList()) {
            is NetworkResult.Success -> {
                _uiState.update { it.copy(friends = result.data ?: emptyList()) }
            }
            is NetworkResult.Error -> {
                _eventFlow.emit(UiEvent.ShowToast(result.message))
            }
            is NetworkResult.Exception -> {
                _eventFlow.emit(UiEvent.ShowToast(result.e.message ?: "網路異常"))
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onTabSelected(tab: FriendListTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        if (tab == FriendListTab.RECENT) {
            loadRecentFriends()
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onSortOrderSelected(sortOrder: FriendListSortOrder) {
        _uiState.update { it.copy(sortOrder = sortOrder) }
    }

    fun onToggleFavorite(friendId: String) {
        _uiState.update { state ->
            state.copy(
                friends = state.friends.map { friend ->
                    if (friend.id == friendId) friend.copy(isFavorite = !friend.isFavorite) else friend
                }
            )
        }
    }

    fun onRemoveFriend(friendId: String) {
        _uiState.update { state ->
            state.copy(friends = state.friends.filterNot { it.id == friendId })
        }
    }

    fun selectFriend(friend: FriendResponse) {
        userRepository.selectedFriend = friend
    }

}
