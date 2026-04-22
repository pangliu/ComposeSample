package com.example.newproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.model.Post
import com.example.newproject.repository.UserRepository
import com.example.newproject.network.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val userInfo: String, val posts: List<Post>) : HomeState()
    data class Error(val message: String) : HomeState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            
            // 使用重構後的 UserRepository 來呼叫，並根據 NetworkResult 解析狀態
            val result = userRepository.fetchUserInfo()
            
            when (result) {
                is NetworkResult.Success -> {
                    _homeState.value = HomeState.Success(
                        userInfo = result.data.toString(), // 這裡的 data 已經解開 BaseResponse 拿到內層物件了
                        posts = listOf(Post(userId = 1, id = 1, title="Hello Domain API", body="成功利用 Repository 拆分！"))
                    )
                }
                is NetworkResult.Error -> {
                    // API 正常，但是後端回傳特殊的錯誤代碼與訊息
                    _homeState.value = HomeState.Error(result.message)
                }
                is NetworkResult.Exception -> {
                    // 網路異常或 JSON 解析失敗
                    _homeState.value = HomeState.Error(result.e.message ?: "無法取得資料：網路異常")
                }
            }
        }
    }
}
