package com.qpay.xcash.ui.scanpay.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

// "You" 固定佔第 1 格，其餘 4 格給好友，合計 5 格
private const val MAX_SPLIT_FRIENDS = 4

@Composable
fun SplitPartnersRow(
    myName: String,
    partners: List<FriendResponse>,
    onEdit: () -> Unit = {},
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val rowColors = colors.scanPay.splitPartners
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 第 1 格：固定顯示 "You"
            SplitAvatarItem(
                avatarLetter = myName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                displayName = stringResource(R.string.split_bill_you),
                avatarColor = colors.accent.secondary,
                modifier = Modifier.weight(1f)
            )

            // 後 4 格：選中的好友
            repeat(MAX_SPLIT_FRIENDS) { index ->
                val friend = partners.getOrNull(index)
                if (friend != null) {
                    SplitAvatarItem(
                        avatarLetter = friend.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                        displayName = friend.name,
                        avatarColor = colors.accent.primary,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            SplitActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = rowColors.editButtonIcon,
                        modifier = Modifier.size(10.dp)
                    )
                },
                borderColor = rowColors.editButtonBorder,
                bgColor = rowColors.editButtonBackground,
                onClick = onEdit
            )
            SplitActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = rowColors.cancelButtonIcon,
                        modifier = Modifier.size(15.dp)
                    )
                },
                borderColor = rowColors.cancelButtonBorder,
                bgColor = rowColors.cancelButtonBackground,
                onClick = onCancel
            )
        }
    }
}

@Composable
private fun SplitAvatarItem(
    avatarLetter: String,
    displayName: String,
    avatarColor: Color,
    modifier: Modifier = Modifier
) {
    val rowColors = LocalAppColors.current.scanPay.splitPartners
    val assets = LocalAppAssets.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp),
//                .background(avatarColor.copy(alpha = 0.15f), CircleShape)
//                .border(1.dp, avatarColor.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp),
                tint = Color.Unspecified,
                contentDescription = null,
                painter = painterResource(assets.friendMaleAvatar),
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = displayName,
            color = rowColors.partnerNameText,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SplitActionButton(
    icon: @Composable () -> Unit,
    borderColor: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .size(24.dp)
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(color = borderColor, alpha = 0.7f, glowRadius = 25.dp, borderRadius = 25.dp)
                else Modifier
            )
            .background(bgColor, CircleShape)
            .border(1.5.dp, borderColor.copy(alpha = 0.6f), CircleShape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

@Composable
private fun SplitPartnersRowFullPreviewContent() {
    SplitPartnersRow(
        myName = "Hank Liu",
        partners = listOf(
            FriendResponse(id = "F001", name = "Bruce Banner", nickName = "bruceb", contactType = ContactType.FACEBOOK, avatarUrl = "", isFavorite = false),
            FriendResponse(id = "F002", name = "Tony Stark", nickName = "ironman", contactType = ContactType.PHONE_NUM, avatarUrl = "", isFavorite = false),
            FriendResponse(id = "F003", name = "Natasha Romanoff", nickName = "blackwidow", contactType = ContactType.FACEBOOK, avatarUrl = "", isFavorite = false),
            FriendResponse(id = "F004", name = "Steve Rogers", nickName = "cap", contactType = ContactType.PHONE_NUM, avatarUrl = "", isFavorite = false)
        )
    )
}

@Composable
private fun SplitPartnersRowPartialPreviewContent() {
    SplitPartnersRow(
        myName = "Hank Liu",
        partners = listOf(
            FriendResponse(id = "F001", name = "Bruce Banner", nickName = "bruceb", contactType = ContactType.FACEBOOK, avatarUrl = "", isFavorite = false),
            FriendResponse(id = "F002", name = "Tony Stark", nickName = "ironman", contactType = ContactType.PHONE_NUM, avatarUrl = "", isFavorite = false)
        )
    )
}

@Preview(name = "Neon — 4 partners + you", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SplitPartnersRowFullPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SplitPartnersRowFullPreviewContent()
    }
}

@Preview(name = "Black Gold — 4 partners + you", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SplitPartnersRowFullPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        SplitPartnersRowFullPreviewContent()
    }
}

@Preview(name = "Neon — 2 partners + you", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SplitPartnersRowPartialPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SplitPartnersRowPartialPreviewContent()
    }
}

@Preview(name = "Black Gold — 2 partners + you", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SplitPartnersRowPartialPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        SplitPartnersRowPartialPreviewContent()
    }
}
