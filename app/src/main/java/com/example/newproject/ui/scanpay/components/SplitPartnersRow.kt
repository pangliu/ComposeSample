package com.example.newproject.ui.scanpay.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.network.model.response.FriendResponse
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.balanceGold
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPink
import com.example.newproject.ui.theme.neonRed

private const val MAX_SPLIT_PARTNERS = 5

@Composable
fun SplitPartnersRow(
    partners: List<FriendResponse>,
    onEdit: () -> Unit = {},
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
            repeat(MAX_SPLIT_PARTNERS) { index ->
                val friend = partners.getOrNull(index)
                if (friend != null) {
                    SplitPartnerAvatar(friend = friend, modifier = Modifier.weight(1f))
                } else {
                    Spacer(Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SplitActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(10.dp))},
                borderColor = balanceGold,
                bgColor = balanceGold.copy(alpha = 0.12f),
                onClick = onEdit
            )
            SplitActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)) },
                borderColor = neonPink,
                bgColor = neonPink.copy(alpha = 0.12f),
                onClick = onCancel
            )
        }
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
            .neonGlow(
                color = borderColor,
                alpha = 0.7f,
                glowRadius = 25.dp,
                borderRadius = 25.dp
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
private fun SplitPartnerAvatar(
    friend: FriendResponse,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(neonCyan.copy(alpha = 0.15f), CircleShape)
                .border(1.dp, neonCyan.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = friend.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                color = neonCyan,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = friend.name,
            color = Color.White,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327, name = "5 partners")
@Composable
private fun SplitPartnersRowFullPreview() {
    MaterialTheme {
        SplitPartnersRow(
            partners = listOf(
                FriendResponse(id = "F001", name = "Bruce Banner", nickName = "bruceb"),
                FriendResponse(id = "F002", name = "Tony Stark", nickName = "ironman"),
                FriendResponse(id = "F003", name = "Natasha Romanoff", nickName = "blackwidow"),
                FriendResponse(id = "F004", name = "Steve Rogers", nickName = "cap"),
                FriendResponse(id = "F005", name = "Wanda Maximoff", nickName = "scarlet")
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327, name = "2 partners")
@Composable
private fun SplitPartnersRowPartialPreview() {
    MaterialTheme {
        SplitPartnersRow(
            partners = listOf(
                FriendResponse(id = "F001", name = "Bruce Banner", nickName = "bruceb"),
                FriendResponse(id = "F002", name = "Tony Stark", nickName = "ironman")
            )
        )
    }
}
