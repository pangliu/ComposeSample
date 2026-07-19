package com.qpay.xcash.ui.scanpay.dialog

import android.view.Gravity
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

private enum class SplitMode { EQUALLY, CUSTOM }

@Composable
fun SplitBillDialog(
    totalAmount: Double,
    friendList: List<FriendResponse>,
    myName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = {}
) {
    var splitMode by remember { mutableStateOf(SplitMode.CUSTOM) }

    val participants = remember(friendList, myName) {
        listOf(myName) + friendList.map { it.name }
    }
    val amounts = remember { mutableStateMapOf<Int, String>() }

    // 切換模式時同步金額；EQUALLY 模式：小數部分給自己，整數平分後餘數也給自己
    val initAmounts: (SplitMode) -> Unit = { mode ->
        if (mode == SplitMode.EQUALLY && participants.isNotEmpty()) {
            val n = participants.size
            val totalCents = (totalAmount * 100).toLong()
            val decimalCents = totalCents % 100          // 小數部分（cents），全給自己
            val integerPart = (totalCents / 100)         // 整數部分
            val shareInt = integerPart / n               // 每人整數平分
            val remainderInt = integerPart % n           // 整數除不盡的餘數，給自己
            participants.forEachIndexed { i, _ ->
                if (i == 0) {
                    // 自己：整數份額 + 整數餘數 + 小數部分
                    val myCents = shareInt * 100 + remainderInt * 100 + decimalCents
                    amounts[i] = "%.2f".format(myCents / 100.0)
                } else {
                    // 其他人：整數份額，無小數
                    amounts[i] = "$shareInt"
                }
            }
        } else {
            participants.forEachIndexed { i, _ ->
                amounts[i] = ""
            }
        }
    }

    // 初始化
    if (amounts.isEmpty()) initAmounts(splitMode)

    val totalAssigned = amounts.values.sumOf { it.toDoubleOrNull() ?: 0.0 }
    val remaining = totalAmount - totalAssigned

    val colors = LocalAppColors.current
    val dialogColors = colors.scanPay.splitBill
    Dialog(onDismissRequest = onDismiss) {
        val view = LocalView.current
        SideEffect {
            val window = (view.parent as? DialogWindowProvider)?.window
            window?.setDimAmount(0.8f) // 0f = 完全透明, 1f = 全黑
            window?.setGravity(Gravity.BOTTOM)
        }
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
                    text = stringResource(R.string.split_bill_title),
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

                Spacer(Modifier.height(12.dp))

                // EQUALLY / CUSTOM toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .border(
                            width = 1.5.dp,
                            brush = dialogColors.toggleBorder,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .background(dialogColors.toggleBackground, RoundedCornerShape(50.dp))
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(SplitMode.EQUALLY, SplitMode.CUSTOM).forEach { mode ->
                        val isSelected = splitMode == mode
                        val label = stringResource(
                            if (mode == SplitMode.EQUALLY) R.string.split_bill_equally
                            else R.string.split_bill_custom
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .then(
                                    if (isSelected)
                                        Modifier.background(dialogColors.toggleSelectedFill, RoundedCornerShape(50.dp))
                                    else Modifier
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    splitMode = mode
                                    initAmounts(mode)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) dialogColors.toggleSelectedText else dialogColors.toggleUnselectedText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Manual-Split header
                Text(
                    text = stringResource(R.string.split_bill_manual_split),
                    color = dialogColors.manualSplitText,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 24.dp),
                    style = if (colors.effect.enableGlow) {
                        TextStyle(
                            shadow = Shadow(
                                color = dialogColors.manualSplitText.copy(alpha = 0.6f),
                                blurRadius = 25f
                            )
                        )
                    } else TextStyle.Default
                )

                Spacer(Modifier.height(4.dp))

                // Participant list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    itemsIndexed(participants) { index, name ->
                        val isMe = index == 0
                        SplitBillParticipantItem(
                            displayName = if (isMe) stringResource(R.string.split_bill_you)
                                          else "@${friendList[index - 1].nickName}",
                            avatarLetter = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                            amount = amounts[index] ?: "",
                            enabled = splitMode == SplitMode.CUSTOM,
                            onAmountChange = { if (splitMode == SplitMode.CUSTOM) amounts[index] = it }
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Bottom bar: Remaining
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(dialogColors.remainingBarBackground)
                        .padding(horizontal = 15.dp)
                        .border(width = 1.dp, color = dialogColors.remainingBarBorder, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.split_bill_remaining),
                        color = dialogColors.remainingLabelText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "PHP %.2f".format(remaining),
                        color = dialogColors.remainingAmountText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(20.dp))
            }
            Spacer(Modifier.height(10.dp))
            // Confirm Request button（位於 dialog 外框下方）
            val isConfirmEnabled = kotlin.math.abs(remaining) < 0.01
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(40.dp)
                    .border(
                        1.5.dp,
                        if (isConfirmEnabled) dialogColors.confirmButtonBorder else dialogColors.confirmButtonDisabledBorder,
                        RoundedCornerShape(50.dp)
                    )
                    .background(
                        brush = if (isConfirmEnabled) dialogColors.confirmButtonFill else dialogColors.confirmButtonDisabledFill,
                        shape = RoundedCornerShape(50.dp),
                        alpha = if (isConfirmEnabled) 1f else 0.5f
                    )
                    .padding(horizontal = 30.dp)
                    .clickable(
                        enabled = isConfirmEnabled,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onConfirm() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.split_bill_confirm_request),
                    color = if (isConfirmEnabled) dialogColors.confirmButtonText else dialogColors.confirmButtonDisabledText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
internal fun SplitBillParticipantItem(
    displayName: String,
    avatarLetter: String,
    amount: String,
    enabled: Boolean,
    onAmountChange: (String) -> Unit
) {
    val dialogColors = LocalAppColors.current.scanPay.splitBill
    val assets = LocalAppAssets.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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

        Text(
            text = displayName,
            color = dialogColors.participantNameText,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        // Amount input
        val borderColor = if (enabled) dialogColors.inputBorder else dialogColors.inputDisabledBorder
        val textColor = if (enabled) dialogColors.inputText else dialogColors.inputDisabledText
        BasicTextField(
            value = amount,
            onValueChange = onAmountChange,
            modifier = Modifier
                .height(40.dp)
                .width(110.dp),
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = TextStyle(
                color = textColor,
                fontSize = 14.sp
            ),
            cursorBrush = SolidColor(dialogColors.inputCursor),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(dialogColors.inputBackground, RoundedCornerShape(8.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun SplitBillParticipantItemPreviewContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        SplitBillParticipantItem(
            displayName = "You",
            avatarLetter = "H",
            amount = "117",
            enabled = false,
            onAmountChange = {}
        )
        SplitBillParticipantItem(
            displayName = "@frienda",
            avatarLetter = "F",
            amount = "116",
            enabled = false,
            onAmountChange = {}
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SplitBillParticipantItemPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SplitBillParticipantItemPreviewContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SplitBillParticipantItemPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        SplitBillParticipantItemPreviewContent()
    }
}

@Composable
private fun SplitBillDialogPreviewContent() {
    SplitBillDialog(
        totalAmount = 350.0,
        friendList = listOf(
            FriendResponse(id = "F001", name = "Friend A", nickName = "frienda", contactType = ContactType.FACEBOOK),
            FriendResponse(id = "F002", name = "Friend B", nickName = "friendb", contactType = ContactType.PHONE_NUM)
        ),
        myName = "Hank Liu",
        onDismiss = {}
    )
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SplitBillDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SplitBillDialogPreviewContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SplitBillDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        SplitBillDialogPreviewContent()
    }
}
