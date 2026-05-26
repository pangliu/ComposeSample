package com.example.newproject.ui.cards.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newproject.network.model.response.CreditCardResponse
import com.example.newproject.repository.CardRepository
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

data class CardDetailUiState(
    val card: CreditCardResponse? = null,
    val nicknameInput: String = "",
    val isPrimary: Boolean = false,
    val showUnlinkDialog: Boolean = false
)

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository
) : ViewModel() {

    private val cardId: Int = checkNotNull(savedStateHandle["cardId"])

    private val _uiState = MutableStateFlow(CardDetailUiState())
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()

    private val _navigateBack = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateBack: SharedFlow<Unit> = _navigateBack.asSharedFlow()

    init {
        cardRepository.selectedCard?.let { card ->
            _uiState.update { it.copy(card = card, nicknameInput = card.nickName, isPrimary = card.isPrimary) }
        }
    }

    fun updateNicknameInput(value: String) {
        _uiState.update { it.copy(nicknameInput = value) }
    }

    fun saveNickname() {
        _uiState.update { it.copy(card = it.card?.copy(cardName = it.nicknameInput)) }
    }

    fun togglePrimary() {
        _uiState.update { it.copy(isPrimary = !it.isPrimary) }
    }

    fun showUnlinkDialog() {
        _uiState.update { it.copy(showUnlinkDialog = true) }
    }

    fun hideUnlinkDialog() {
        _uiState.update { it.copy(showUnlinkDialog = false) }
    }

    fun unlinkCard() {
        _uiState.update { it.copy(showUnlinkDialog = false) }
        viewModelScope.launch { _navigateBack.emit(Unit) }
    }
}
