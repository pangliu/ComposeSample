package com.example.newproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.EssentialsManager
import com.example.newproject.network.model.NetworkResult
import com.example.newproject.network.model.response.UserInfoResponse
import com.example.newproject.repository.UserRepository
import com.example.newproject.ui.home.essential.EssentialItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val userInfo: UserInfoResponse) : HomeState()
    data class Error(val message: String) : HomeState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val essentialsManager: EssentialsManager
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val _myMenuItems = MutableStateFlow(essentialsManager.load())
    val myMenuItems: StateFlow<List<EssentialItem>> = _myMenuItems.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            val result = userRepository.fetchUserInfo()

            when (result) {
                is NetworkResult.Success -> {
                    val userInfo = result.data
                    if (userInfo != null) {
                        _homeState.value = HomeState.Success(userInfo = userInfo)
                    } else {
                        _homeState.value = HomeState.Error("無法取得使用者資料")
                    }
                }
                is NetworkResult.Error -> {
                    _homeState.value = HomeState.Error(result.message)
                }
                is NetworkResult.Exception -> {
                    _homeState.value = HomeState.Error(result.e.message ?: "無法取得資料：網路異常")
                }
            }
        }
    }

    fun saveMyMenu(items: List<EssentialItem>) {
        _myMenuItems.value = items
        essentialsManager.save(items)
    }
}
