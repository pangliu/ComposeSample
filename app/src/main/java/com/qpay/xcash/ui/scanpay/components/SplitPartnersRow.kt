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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.lemonYellow
import com.qpay.xcash.ui.theme.neonPink

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
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                },
                borderColor = lemonYellow,
                bgColor = lemonYellow.copy(alpha = 0.12f),
                onClick = onEdit
            )
            SplitActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                },
                borderColor = neonPink,
                bgColor = neonPink.copy(alpha = 0.12f),
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
                painter = painterResource(R.drawable.ic_friend_male),
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = displayName,
            color = Color.White,
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
    Box(
        modifier = Modifier
            .size(24.dp)
            .neonGlow(color = borderColor, alpha = 0.7f, glowRadius = 25.dp, borderRadius = 25.dp)
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

@Preview(showBackground = true, backgroundColor = 0xFF0B1327, name = "4 partners + you")
@Composable
private fun SplitPartnersRowFullPreview() {
    MaterialTheme {
        SplitPartnersRow(
            myName = "Hank Liu",
            partners = listOf(
                FriendResponse(id = "F001", name = "Bruce Banner", nickName = "bruceb", contactType = ContactType.FACEBOOK),
                FriendResponse(id = "F002", name = "Tony Stark", nickName = "ironman", contactType = ContactType.PHONE_NUM),
                FriendResponse(id = "F003", name = "Natasha Romanoff", nickName = "blackwidow", contactType = ContactType.FACEBOOK),
                FriendResponse(id = "F004", name = "Steve Rogers", nickName = "cap", contactType = ContactType.PHONE_NUM)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327, name = "2 partners + you")
@Composable
private fun SplitPartnersRowPartialPreview() {
    MaterialTheme {
        SplitPartnersRow(
            myName = "Hank Liu",
            partners = listOf(
                FriendResponse(id = "F001", name = "Bruce Banner", nickName = "bruceb", contactType = ContactType.FACEBOOK),
                FriendResponse(id = "F002", name = "Tony Stark", nickName = "ironman", contactType = ContactType.PHONE_NUM)
            )
        )
    }
}
