package com.example.newproject.ui.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.manager.UserInfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileEditUiState(
    val fullName: String = "",
    val gender: String = "Male",
    val mobile: String = "",
    val email: String = "",
    val dob: String = "1970-01-01",
    val educationLevel: String = "PhD",
    val currentHustle: String = "Scientist",
    val incomeCurrency: String = "PHP",
    val yearlyBag: String = "5,000,000 PHP",
    val relationshipStatus: String = "Single",
    val gotKids: String = "None",
    val howFoundUs: String = "Social Media",
    val referralName: String = "X-STARKLABS",
    val referralCode: String = "STARK777"
)

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val userInfoManager: UserInfoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userInfoManager.userInfoFlow.collect { userInfo ->
                if (userInfo != null) {
                    _uiState.update {
                        it.copy(
                            fullName = userInfo.userName,
                            mobile = userInfo.userPhone,
                            email = userInfo.userEmail
                        )
                    }
                }
            }
        }
    }
}
