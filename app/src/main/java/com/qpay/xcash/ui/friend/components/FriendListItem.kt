package com.qpay.xcash.ui.friend.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.neonRed
import kotlin.math.roundToInt

private val SWIPE_ACTION_WIDTH = 88.dp

@Composable
fun FriendListItem(
    friend: FriendResponse,
    onToggleFavorite: () -> Unit,
    onRemove: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val density = LocalDensity.current
    val actionWidthPx = with(density) { SWIPE_ACTION_WIDTH.toPx() }

    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val offsetX by animateFloatAsState(
        targetValue = dragOffsetX,
        animationSpec = if (isDragging) snap() else tween(200),
        label = "friendItemSwipeOffset"
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        // 左滑後露出的 Remove 背景，佔滿整個 cell（matchParentSize 會取用 Box 最終量測出的完整大小）
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(neonRed)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    dragOffsetX = 0f
                    onRemove()
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = stringResource(R.string.friend_list_item_remove),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(SWIPE_ACTION_WIDTH)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .background(LocalAppColors.current.bg.page)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = {
                            isDragging = false
                            dragOffsetX = if (dragOffsetX < -actionWidthPx / 2) -actionWidthPx else 0f
                        },
                        onDragCancel = {
                            isDragging = false
                            dragOffsetX = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            dragOffsetX = (dragOffsetX + dragAmount).coerceIn(-actionWidthPx, 0f)
                        }
                    )
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FriendListItemContent(friend = friend, onToggleFavorite = onToggleFavorite)
        }
    }
}

@Composable
private fun RowScope.FriendListItemContent(
    friend: FriendResponse,
    onToggleFavorite: () -> Unit
) {
    val colors = LocalAppColors.current
    val friendListColors = colors.friendList
    val assets = LocalAppAssets.current

    AsyncImage(
        model = friend.avatarUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(assets.friendMaleAvatar),
        error = painterResource(assets.friendMaleAvatar),
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .border(1.5.dp, colors.accent.primary, CircleShape)
    )

    Spacer(Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = friend.name,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "@${friend.nickName}",
            color = colors.text.body,
            fontSize = 12.sp
        )
    }

    friend.tagLabel?.let { tag ->
        Box(
            modifier = Modifier
                .background(friendListColors.tagBackground, RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Text(
                text = tag,
                color = friendListColors.tagText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.width(10.dp))
    }

    Icon(
        imageVector = if (friend.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
        contentDescription = null,
        tint = if (friend.isFavorite) colors.accent.primary else friendListColors.starInactive,
        modifier = Modifier
            .size(22.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onToggleFavorite() }
    )
}

@Composable
private fun FriendListItemPreviewContent() {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .background(colors.bg.page)
            .padding(16.dp)
    ) {
        FriendListItem(
            friend = FriendResponse(
                id = "F001",
                name = "Angela Reyes",
                nickName = "areyes",
                contactType = ContactType.FACEBOOK,
                avatarUrl = "",
                isFavorite = false,
                tagLabel = "Family"
            ),
            onToggleFavorite = {}
        )
        FriendListItem(
            friend = FriendResponse(
                id = "F002",
                name = "Carlos Garcia",
                nickName = "cgarcia",
                contactType = ContactType.PHONE_NUM,
                avatarUrl = "",
                isFavorite = false,
                tagLabel = null
            ),
            onToggleFavorite = {}
        )
        FriendListItem(
            friend = FriendResponse(
                id = "F004",
                name = "John Cruz",
                nickName = "jcruz",
                contactType = ContactType.PHONE_NUM,
                avatarUrl = "",
                isFavorite = true,
                tagLabel = "Besties"
            ),
            onToggleFavorite = {}
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun FriendListItemPreviewNeon() {
    AppTheme(colors = NeonColors) {
        FriendListItemPreviewContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FriendListItemPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        FriendListItemPreviewContent()
    }
}
