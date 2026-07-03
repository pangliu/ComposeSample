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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.LocalAppColors

private val InputFieldBackground = Color(0xFF0D1525)

@Composable
fun SelectSplitPartnerDialog(
    friendList: List<FriendResponse>,
    totalAmount: Double,
    onDismiss: () -> Unit,
    onConfirm: (List<FriendResponse>) -> Unit
) {
    val colors = LocalAppColors.current
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    val selectedCount = selectedIds.size

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(color = colors.accent.primary, alpha = 0.5f, glowRadius = 12.dp, borderRadius = 16.dp)
                .background(colors.bg.page, RoundedCornerShape(16.dp))
                .border(1.5.dp, colors.accent.primary.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
        ) {
            // Title
            Text(
                text = stringResource(R.string.select_partner_title),
                color = colors.accent.secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                style = TextStyle(
                    shadow = Shadow(color = colors.accent.secondary.copy(alpha = 0.6f), blurRadius = 25f)
                )
            )

            // Selected count hint
            Text(
                text = stringResource(R.string.select_partner_selected_count, selectedCount),
                color = if (selectedCount > 0) colors.accent.primary else colors.text.body,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp, bottom = 8.dp)
            )

            // Friend list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
                    .padding(horizontal = 16.dp)
            ) {
                items(friendList, key = { it.id }) { friend ->
                    val isSelected = friend.id in selectedIds
                    SelectPartnerItem(
                        friend = friend,
                        isSelected = isSelected,
                        onClick = {
                            selectedIds = if (isSelected) {
                                selectedIds - friend.id
                            } else {
                                selectedIds + friend.id
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Total to Split bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .neonGlow(color = colors.accent.primary, alpha = 0.5f, glowRadius = 8.dp, borderRadius = 8.dp)
                    .background(
                        color = colors.bg.page,
                        shape = RoundedCornerShape(8.dp))
                    .border(width = 1.5.dp, color = colors.accent.primary.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.select_partner_total_to_split),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "PHP %.2f".format(totalAmount),
                    color = colors.accent.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Assign Amounts button
            val isConfirmEnabled = selectedCount > 0
            Box(
                modifier = Modifier
//                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(40.dp)
                    .then(
                        if (isConfirmEnabled)
                            Modifier.neonGlow(colors.accent.secondary, alpha = 0.45f, glowRadius = 10.dp, borderRadius = 12.dp)
                        else Modifier
                    )
                    .border(
                        1.5.dp,
                        if (isConfirmEnabled) colors.accent.secondary else colors.accent.secondary.copy(alpha = 0.3f),
                        RoundedCornerShape(50.dp)
                    )
                    .background(
                        colors.accent.secondary.copy(alpha = if (isConfirmEnabled) 0.15f else 0.05f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 30.dp)
                    .clickable(
                        enabled = isConfirmEnabled,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onConfirm(friendList.filter { it.id in selectedIds })
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.select_partner_assign_amounts),
                    color = if (isConfirmEnabled) Color.White else Color.White.copy(alpha = 0.35f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SelectPartnerItem(
    friend: FriendResponse,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp),
                tint = Color.Unspecified,
                contentDescription = null,
                painter = painterResource(R.mipmap.ic_male),
            )
        }

        Spacer(Modifier.width(10.dp))

        // Name + nickName
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = friend.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "@${friend.nickName}",
                color = colors.text.body,
                fontSize = 12.sp
            )
        }

        // Selection indicator
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    if (isSelected) colors.accent.secondary.copy(alpha = 0.8f) else Color.Transparent,
                    CircleShape
                )
                .border(
                    1.5.dp,
                    if (isSelected) colors.accent.secondary else colors.text.body.copy(alpha = 0.5f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SelectSplitPartnerDialogPreview() {
    MaterialTheme {
        SelectSplitPartnerDialog(
            friendList = listOf(
                FriendResponse(id = "F001", name = "Bruce Banner", nickName = "bruceb"),
                FriendResponse(id = "F002", name = "Tony Stark", nickName = "ironman"),
                FriendResponse(id = "F003", name = "Natasha Romanoff", nickName = "blackwidow")
            ),
            totalAmount = 350.0,
            onDismiss = {},
            onConfirm = {}
        )
    }
}
