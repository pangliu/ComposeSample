package com.qpay.xcash.ui.friend.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun FoundFriendItem(
    friend: FriendResponse,
    onAdd: () -> Unit = {},
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val appColors = LocalAppColors.current
    val colors = appColors.foundFriend
    val assets = LocalAppAssets.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = colors.addButtonIcon,
            modifier = Modifier
                .size(15.dp)
                .background(colors.addButtonBackground, CircleShape)
//                .border(1.5.dp, colors.addButtonBorder, CircleShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onAdd() }
//                .padding(8.dp)
        )

        Spacer(Modifier.width(12.dp))

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

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            GradientText(
                text = friend.name,
                color = colors.nameText,
//                brush = appColors.gradient.goldShimmer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@${friend.nickName}",
                color = colors.usernameText,
                fontSize = 13.sp
            )
        }

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = colors.dismissIconTint,
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() }
        )
    }
}

private val previewFriend = FriendResponse(
    id = "F010",
    name = "Tom Miller",
    nickName = "tommiller",
    contactType = ContactType.PHONE_NUM,
    avatarUrl = "https://i.pravatar.cc/150?u=F010",
    isFavorite = false,
    tagLabel = null
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun FoundFriendItemPreviewNeon() {
    AppTheme(colors = NeonColors) {
        FoundFriendItem(
            friend = previewFriend,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FoundFriendItemPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        FoundFriendItem(
            friend = previewFriend,
            modifier = Modifier.padding(16.dp)
        )
    }
}
