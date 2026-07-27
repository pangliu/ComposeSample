package com.qpay.xcash.ui.avatar

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpay.xcash.network.model.NetworkResult
import com.qpay.xcash.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

data class EditAvatarUiState(
    val isUploading: Boolean = false,
    val showUploadError: Boolean = false
)

sealed class EditAvatarNavigationEvent {
    object UploadSuccess : EditAvatarNavigationEvent()
}

@HiltViewModel
class EditAvatarViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditAvatarUiState())
    val uiState: StateFlow<EditAvatarUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<EditAvatarNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<EditAvatarNavigationEvent> = _navigationEvent.asSharedFlow()

    fun uploadUserImage(imageUri: Uri) {
        _uiState.update { it.copy(isUploading = true, showUploadError = false) }
        viewModelScope.launch {
            val imagePart = createImagePart(imageUri)
            if (imagePart == null) {
                _uiState.update { it.copy(isUploading = false, showUploadError = true) }
                return@launch
            }
            when (val result = userRepository.uploadUserImage(imagePart)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isUploading = false) }
                    _navigationEvent.emit(EditAvatarNavigationEvent.UploadSuccess)
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isUploading = false, showUploadError = true) }
                }
                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isUploading = false, showUploadError = true) }
                }
            }
        }
    }

    fun dismissUploadError() {
        _uiState.update { it.copy(showUploadError = false) }
    }

    private fun createImagePart(imageUri: Uri): MultipartBody.Part? {
        val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
        val file = File(context.cacheDir, "avatar_upload_${System.currentTimeMillis()}.jpg")
        inputStream.use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }
}
