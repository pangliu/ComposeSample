package com.qpay.xcash.ui.friend.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun FriendListItem(
    friend: FriendResponse,
    onToggleFavorite: () -> Unit
) {
    val colors = LocalAppColors.current
    val friendListColors = colors.friendList
    val assets = LocalAppAssets.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
