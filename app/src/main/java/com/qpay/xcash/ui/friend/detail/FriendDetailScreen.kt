package com.qpay.xcash.ui.friend.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.friend.components.FriendRemoveConfirmDialog
import com.qpay.xcash.ui.friend.components.FriendTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun FriendDetailScreen(
    viewModel: FriendDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateBack.collect { onBack() }
    }

    FriendDetailContent(
        uiState = uiState,
        onBack = onBack,
        onNameChange = viewModel::onNameChange,
        onNameReset = viewModel::onNameReset,
        onNameConfirm = viewModel::onNameConfirm,
        onTagSelected = viewModel::onTagSelected,
        onMemoChange = viewModel::onMemoChange,
        onSave = viewModel::saveFriend,
        onRemove = viewModel::removeFriend
    )
}

@Composable
fun FriendDetailContent(
    uiState: FriendDetailUiState,
    onBack: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onNameReset: () -> Unit = {},
    onNameConfirm: () -> Unit = {},
    onTagSelected: (String) -> Unit = {},
    onMemoChange: (String) -> Unit = {},
    onSave: () -> Unit = {},
    onRemove: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    var isRemoveDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FriendTopBar(
                title = stringResource(R.string.friend_title),
                onBack = onBack,
                trailingContent = {
                    val colors = LocalAppColors.current
                    Icon(
                        imageVector = Icons.Default.Autorenew,
                        contentDescription = stringResource(R.string.friend_detail_refresh_desc),
                        tint = colors.accent.primary,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { /* TODO: 重新整理好友資料 */ }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                uiState.friend?.let { friend ->
                    Spacer(modifier = Modifier.height(16.dp))

                    FriendDetailAvatar(friend = friend)

                    Spacer(modifier = Modifier.height(16.dp))

                    FriendDetailNameField(
                        nameInput = uiState.nameInput,
                        onNameChange = onNameChange,
                        onNameReset = onNameReset,
                        onNameConfirm = onNameConfirm
                    )

                    friend.phoneNumber?.let { phoneNumber ->
                        Spacer(modifier = Modifier.height(6.dp))
                        FriendDetailCopyableValue(
                            value = phoneNumber,
                            textColor = colors.friendDetail.infoValueText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Text(
                            text = stringResource(R.string.friend_detail_tags_label),
                            color = colors.friendDetail.infoLabelText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            uiState.availableTags.forEach { tag ->
                                FriendDetailTagChip(
                                    label = tag,
                                    isSelected = tag == uiState.selectedTag,
                                    onClick = { onTagSelected(tag) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    friend.phoneNumber?.let { xcashId ->
                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            Text(
                                text = stringResource(R.string.friend_detail_xcash_id_label),
                                color = colors.friendDetail.infoLabelText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FriendDetailCopyableValue(
                                value = xcashId,
                                textColor = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                horizontalArrangement = Arrangement.Start
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    HorizontalDivider(
                        color = Color.Transparent,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .background(
                                brush = colors.friendDetail.divider)

                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Text(
                            text = stringResource(R.string.friend_detail_memo_label),
                            color = colors.friendDetail.infoLabelText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        FriendDetailMemoField(
                            value = uiState.memoInput,
                            onValueChange = onMemoChange
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        FriendDetailSaveButton(onClick = onSave)
                        Spacer(modifier = Modifier.height(12.dp))
                        FriendDetailRemoveButton(onClick = { isRemoveDialogVisible = true })
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        FriendRemoveConfirmDialog(
            isVisible = isRemoveDialogVisible,
            onCancel = { isRemoveDialogVisible = false },
            onConfirm = {
                isRemoveDialogVisible = false
                onRemove()
            }
        )
    }
}

@Composable
private fun FriendDetailAvatar(friend: FriendResponse) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = friend.avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(assets.friendMaleAvatar),
            error = painterResource(assets.friendMaleAvatar),
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .border(2.dp, colors.friendDetail.avatarBorder, CircleShape)
        )
    }
}

@Composable
private fun FriendDetailNameField(
    nameInput: String,
    onNameChange: (String) -> Unit,
    onNameReset: () -> Unit,
    onNameConfirm: () -> Unit
) {
    val colors = LocalAppColors.current.friendDetail
    var isEditing by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun exitEditing() {
        isEditing = false
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isEditing) {
            BasicTextField(
                value = nameInput,
                onValueChange = onNameChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = colors.nameText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(colors.nameCursor),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onNameConfirm()
                    exitEditing()
                }),
                modifier = Modifier.focusRequester(focusRequester)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.friend_detail_name_reset_desc),
                tint = colors.nameResetIcon,
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onNameReset()
                        exitEditing()
                    }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = stringResource(R.string.friend_detail_name_save_desc),
                tint = colors.nameSaveIcon,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onNameConfirm()
                        exitEditing()
                    }
            )
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        } else {
            Text(
                text = nameInput,
                color = colors.nameText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.friend_detail_name_edit_desc),
                tint = colors.nameEditIcon,
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isEditing = true }
            )
        }
    }
}

@Composable
private fun FriendDetailCopyableValue(
    value: String,
    textColor: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center
) {
    val colors = LocalAppColors.current.friendDetail
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val copiedMessage = stringResource(R.string.friend_detail_copied_toast)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = value,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = stringResource(R.string.friend_detail_copy_desc),
            tint = colors.copyIconTint,
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    clipboardManager.setText(AnnotatedString(value))
                    Toast.makeText(context, copiedMessage, Toast.LENGTH_SHORT).show()
                }
        )
    }
}

@Composable
private fun FriendDetailTagChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current.friendDetail
    Box(
        modifier = Modifier
            .then(
                if (isSelected) Modifier.background(colors.tagSelectedFill, RoundedCornerShape(50))
                else Modifier.border(1.dp, colors.tagUnselectedBorder, RoundedCornerShape(50))
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) colors.tagSelectedText else colors.tagUnselectedText,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun FriendDetailMemoField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val colors = LocalAppColors.current.friendDetail
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp)
            .background(colors.memoBackground, RoundedCornerShape(16.dp))
            .border(1.dp, colors.memoBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = stringResource(R.string.friend_detail_memo_hint),
                color = colors.infoValueText.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = colors.memoText, fontSize = 14.sp, lineHeight = 20.sp),
            cursorBrush = SolidColor(colors.memoCursor),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun FriendDetailSaveButton(onClick: () -> Unit) {
    val colors = LocalAppColors.current.friendDetail
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(brush = colors.saveButtonBackground, shape = RoundedCornerShape(8.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.friend_detail_save_btn),
            color = colors.saveButtonText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FriendDetailRemoveButton(onClick: () -> Unit) {
    val colors = LocalAppColors.current.friendDetail
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = colors.removeButtonBackground)
            .border(1.5.dp, colors.removeButtonBorder, RoundedCornerShape(8.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.friend_detail_remove_btn),
            color = colors.removeButtonText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private val previewFriend = FriendResponse(
    id = "F003",
    name = "Isabella Torres",
    nickName = "isabella_t",
    contactType = ContactType.PHONE_NUM,
    avatarUrl = "https://i.pravatar.cc/150?u=F003",
    isFavorite = true,
    tagLabel = "Family",
    phoneNumber = "09174798166",
    memo = "Met at the road trip last winter."
)

private val previewUiState = FriendDetailUiState(
    friend = previewFriend,
    nameInput = previewFriend.name,
    selectedTag = previewFriend.tagLabel,
    memoInput = previewFriend.memo.orEmpty()
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun FriendDetailScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        FriendDetailContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FriendDetailScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        FriendDetailContent(uiState = previewUiState)
    }
}
