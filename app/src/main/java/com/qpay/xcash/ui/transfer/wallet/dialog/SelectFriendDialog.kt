package com.qpay.xcash.ui.transfer.wallet.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.SelectFriendDialogColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DISMISS_ANIM_MILLIS = 300
private val SheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

/**
 * WalletTransferScreen 點選 Friends 模式後從下方彈出的好友選擇清單。
 * 清單資料由呼叫端（ViewModel 呼叫 friend_list API）傳入；點選好友後等收合動畫結束再 [onSelect]。
 */
@Composable
fun SelectFriendDialog(
    friendList: List<FriendResponse>,
    onDismiss: () -> Unit,
    onSelect: (FriendResponse) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { isVisible = true }

    fun dismissWithAnimation(afterAnimation: () -> Unit) {
        isVisible = false
        scope.launch {
            delay(DISMISS_ANIM_MILLIS.toLong())
            afterAnimation()
        }
    }

    Dialog(
        onDismissRequest = { dismissWithAnimation(onDismiss) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { dismissWithAnimation(onDismiss) }
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(DISMISS_ANIM_MILLIS)) +
                    fadeIn(animationSpec = tween(DISMISS_ANIM_MILLIS)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(DISMISS_ANIM_MILLIS)) +
                    fadeOut(animationSpec = tween(DISMISS_ANIM_MILLIS))
            ) {
                SelectFriendDialogContent(
                    friendList = friendList,
                    onSelect = { friend -> dismissWithAnimation { onSelect(friend) } }
                )
            }
        }
    }
}

@Composable
private fun SelectFriendDialogContent(
    friendList: List<FriendResponse>,
    onSelect: (FriendResponse) -> Unit
) {
    val colors = LocalAppColors.current.selectFriendDialog
    var searchQuery by remember { mutableStateOf("") }
    val filteredFriendList = remember(friendList, searchQuery) {
        if (searchQuery.isBlank()) friendList
        else friendList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                it.nickName.contains(searchQuery, ignoreCase = true)
        }
    }
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
            // 吃掉彈窗內的點擊，避免穿透到外層的「點背景關閉」
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
            .background(colors.sheetBackground, SheetShape)
            .border(1.dp, colors.sheetBorder, SheetShape)
            .padding(top = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp)
                .height(4.dp)
                .background(colors.dragHandle, RoundedCornerShape(2.dp))
        )

        Spacer(Modifier.height(20.dp))

        SearchField(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            colors = colors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        if (filteredFriendList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.select_friend_empty),
                    color = colors.emptyText,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScrollbar(listState, colors.scrollbar)
                    .padding(horizontal = 24.dp)
            ) {
                items(filteredFriendList, key = { it.id }) { friend ->
                    SelectFriendItem(friend = friend, onClick = { onSelect(friend) })
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    colors: SelectFriendDialogColors,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colors.searchBackground)
            .border(1.dp, colors.searchBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(R.string.select_friend_search_hint),
                    color = colors.searchHintText,
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(color = colors.searchText, fontSize = 14.sp),
                cursorBrush = SolidColor(colors.searchText),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.select_friend_search_desc),
            tint = colors.searchIcon,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SelectFriendItem(
    friend: FriendResponse,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current.selectFriendDialog
    val assets = LocalAppAssets.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = friend.avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(assets.friendMaleAvatar),
            error = painterResource(assets.friendMaleAvatar),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(1.5.dp, colors.avatarBorder, CircleShape)
        )

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = friend.name,
                    color = colors.nameText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                friend.tagLabel?.let { tag ->
                    Spacer(Modifier.width(8.dp))
                    TagChip(tag = tag, colors = colors)
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = if (friend.contactType == ContactType.PHONE_NUM && !friend.phoneNumber.isNullOrEmpty()) {
                    maskPhoneNumber(friend.phoneNumber)
                } else {
                    stringResource(R.string.select_friend_xcash_account)
                },
                color = colors.subText,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TagChip(tag: String, colors: SelectFriendDialogColors) {
    val (background, textColor) = when (tag) {
        "Family" -> colors.tagFamilyBackground to colors.tagFamilyText
        else -> colors.tagBestiesBackground to colors.tagBestiesText
    }
    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = tag,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// 保留前 3 碼與後 3 碼，中間以 * 遮罩（例：09123456687 → 091*****687）
private fun maskPhoneNumber(value: String): String {
    if (value.length <= 6) return value
    return value.take(3) + "*".repeat(value.length - 6) + value.takeLast(3)
}

// 列表右側細捲軸（設計稿僅顯示一條窄色條，無額外互動）
private fun Modifier.verticalScrollbar(state: LazyListState, color: Color): Modifier =
    drawWithContent {
        drawContent()
        val info = state.layoutInfo
        val total = info.totalItemsCount
        val visible = info.visibleItemsInfo
        if (total == 0 || visible.isEmpty() || visible.size >= total) return@drawWithContent

        val itemSize = visible.first().size.toFloat().coerceAtLeast(1f)
        val scrolledItems = state.firstVisibleItemIndex + state.firstVisibleItemScrollOffset / itemSize
        val maxScroll = (total - visible.size).toFloat()
        val thumbHeight = (size.height * visible.size / total).coerceAtLeast(24.dp.toPx())
        val thumbTop = (size.height - thumbHeight) * (scrolledItems / maxScroll).coerceIn(0f, 1f)
        val thumbWidth = 3.dp.toPx()
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width - 12.dp.toPx(), thumbTop),
            size = Size(thumbWidth, thumbHeight),
            cornerRadius = CornerRadius(thumbWidth / 2)
        )
    }

private val previewFriends = listOf(
    FriendResponse(id = "1", name = "Isabella Torres", nickName = "isabella", contactType = ContactType.PHONE_NUM, avatarUrl = "", isFavorite = false, tagLabel = "Family", phoneNumber = "09171234687"),
    FriendResponse(id = "2", name = "John Cruz", nickName = "jcruz", contactType = ContactType.FACEBOOK, avatarUrl = "", isFavorite = false, tagLabel = "Besties"),
    FriendResponse(id = "3", name = "Maria Santos", nickName = "maria", contactType = ContactType.FACEBOOK, avatarUrl = "", isFavorite = false, tagLabel = "Family"),
    FriendResponse(id = "4", name = "Nia Nakamura", nickName = "nia", contactType = ContactType.FACEBOOK, avatarUrl = "", isFavorite = false)
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B, heightDp = 640)
@Composable
private fun SelectFriendDialogPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            SelectFriendDialogContent(friendList = previewFriends, onSelect = {})
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505, heightDp = 640)
@Composable
private fun SelectFriendDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            SelectFriendDialogContent(friendList = previewFriends, onSelect = {})
        }
    }
}
