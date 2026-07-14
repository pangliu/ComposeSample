package com.qpay.xcash.ui.scanpay.dialog

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

@Composable
fun SelectSplitPartnerDialog(
    friendList: List<FriendResponse>,
    totalAmount: Double,
    onDismiss: () -> Unit,
    onConfirm: (List<FriendResponse>) -> Unit
) {
    val colors = LocalAppColors.current
    val dialogColors = colors.scanPay.selectPartner
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    var searchQuery by remember { mutableStateOf("") }
    val selectedCount = selectedIds.size
    val filteredFriendList = remember(friendList, searchQuery) {
        if (searchQuery.isBlank()) friendList
        else friendList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                it.nickName.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Dialog 外框
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(
                                color = colors.accent.primary,
                                alpha = 0.5f,
                                glowRadius = 12.dp,
                                borderRadius = 16.dp
                            )
                        else Modifier
                    )
                    .background(dialogColors.dialogBackground, RoundedCornerShape(16.dp))
                    .border(1.5.dp, dialogColors.dialogBorder, RoundedCornerShape(16.dp))
            ) {
                // Title
                Text(
                    text = stringResource(R.string.select_partner_title),
                    color = dialogColors.titleText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    style = if (colors.effect.enableGlow) {
                        TextStyle(
                            shadow = Shadow(
                                color = dialogColors.titleText.copy(alpha = 0.6f),
                                blurRadius = 25f
                            )
                        )
                    } else TextStyle.Default
                )

                // Selected count hint
                Text(
                    text = stringResource(R.string.select_partner_selected_count, selectedCount),
                    color = if (selectedCount > 0) dialogColors.countHintActiveText else dialogColors.countHintText,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp, bottom = 8.dp)
                )

                // Search input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(1.dp, dialogColors.searchBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = stringResource(R.string.select_partner_search_hint),
                                color = dialogColors.searchHintText,
                                fontSize = 14.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(color = dialogColors.searchText, fontSize = 14.sp),
                            cursorBrush = SolidColor(dialogColors.searchText),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = dialogColors.searchIcon,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Friend list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    items(filteredFriendList, key = { it.id }) { friend ->
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
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    color = colors.accent.primary,
                                    alpha = 0.5f,
                                    glowRadius = 8.dp,
                                    borderRadius = 8.dp
                                )
                            else Modifier
                        )
                        .background(
                            color = dialogColors.totalBarBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            color = dialogColors.totalBarBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.select_partner_total_to_split),
                        color = dialogColors.totalLabelText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "PHP %.2f".format(totalAmount),
                        color = dialogColors.totalAmountText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
            Spacer(Modifier.height(10.dp))
            // Assign Amounts button（位於 dialog 外框下方）
            val isConfirmEnabled = selectedCount > 0
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(40.dp)
                    .then(
                        if (isConfirmEnabled && colors.effect.enableGlow)
                            Modifier.neonGlow(
                                colors.accent.secondary,
                                alpha = 0.45f,
                                glowRadius = 10.dp,
                                borderRadius = 12.dp
                            )
                        else Modifier
                    )
                    .border(
                        1.5.dp,
                        if (isConfirmEnabled) dialogColors.confirmButtonBorder else dialogColors.confirmButtonDisabledBorder,
                        RoundedCornerShape(50.dp)
                    )
                    .background(
                        if (isConfirmEnabled) dialogColors.confirmButtonFill else dialogColors.confirmButtonDisabledFill,
                        RoundedCornerShape(50.dp)
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
                    color = if (isConfirmEnabled) dialogColors.confirmButtonText else dialogColors.confirmButtonDisabledText,
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
    val dialogColors = LocalAppColors.current.scanPay.selectPartner
    val assets = LocalAppAssets.current
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
                painter = painterResource(assets.friendMaleAvatar),
            )
        }

        Spacer(Modifier.width(10.dp))

        // Name + nickName
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = friend.name,
                color = dialogColors.itemNameText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "@${friend.nickName}",
                color = dialogColors.itemNickNameText,
                fontSize = 12.sp
            )
        }

        // Contact type icon
        Icon(
            painter = painterResource(
                when (friend.contactType) {
                    ContactType.FACEBOOK -> assets.contactFacebookIcon
                    ContactType.PHONE_NUM -> assets.contactPhoneIcon
                }
            ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(12.dp))

        // Selection indicator
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(
                    1.5.dp,
                    if (isSelected) dialogColors.indicatorSelected else dialogColors.indicatorBorder,
                    RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = dialogColors.indicatorSelected,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun SelectPartnerItemPreviewContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        SelectPartnerItem(
            friend = FriendResponse(
                id = "F001",
                name = "Bruce Banner",
                nickName = "bruceb",
                contactType = ContactType.FACEBOOK
            ),
            isSelected = true,
            onClick = {}
        )
        SelectPartnerItem(
            friend = FriendResponse(
                id = "F002",
                name = "Tony Stark",
                nickName = "ironman",
                contactType = ContactType.PHONE_NUM
            ),
            isSelected = false,
            onClick = {}
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SelectPartnerItemPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SelectPartnerItemPreviewContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SelectPartnerItemPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        SelectPartnerItemPreviewContent()
    }
}

@Composable
private fun SelectSplitPartnerDialogPreviewContent() {
    SelectSplitPartnerDialog(
        friendList = listOf(
            FriendResponse(
                id = "F001",
                name = "Bruce Banner",
                nickName = "bruceb",
                contactType = ContactType.FACEBOOK
            ),
            FriendResponse(
                id = "F002",
                name = "Bruce Banner",
                nickName = "bruceb",
                contactType = ContactType.FACEBOOK
            ),
            FriendResponse(
                id = "F003",
                name = "Bruce Banner",
                nickName = "bruceb",
                contactType = ContactType.FACEBOOK
            ),
            FriendResponse(
                id = "F004",
                name = "Bruce Banner",
                nickName = "bruceb",
                contactType = ContactType.FACEBOOK
            ),
            FriendResponse(
                id = "F005",
                name = "Tony Stark",
                nickName = "ironman",
                contactType = ContactType.PHONE_NUM
            ),
            FriendResponse(
                id = "F006",
                name = "Natasha Romanoff",
                nickName = "blackwidow",
                contactType = ContactType.FACEBOOK
            )
        ),
        totalAmount = 350.0,
        onDismiss = {},
        onConfirm = {}
    )
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SelectSplitPartnerDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SelectSplitPartnerDialogPreviewContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SelectSplitPartnerDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        SelectSplitPartnerDialogPreviewContent()
    }
}
