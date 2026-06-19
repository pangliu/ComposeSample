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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.newproject.R
import com.example.newproject.network.model.response.FriendResponse
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonMint
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.welcomeBackground

private enum class SplitMode { EQUALLY, CUSTOM }
private val InputFieldBackground = Color(0xFF0D1525)

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

    // 切換模式時同步金額；EQUALLY 模式：整數平分，餘數給 You
    val initAmounts: (SplitMode) -> Unit = { mode ->
        if (mode == SplitMode.EQUALLY && participants.isNotEmpty()) {
            val n = participants.size
            val total = totalAmount.toInt()
            val share = total / n
            val remainder = total % n
            participants.forEachIndexed { i, _ ->
                amounts[i] = if (i == 0) "${share + remainder}" else "$share"
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

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(color = neonCyan, alpha = 0.5f, glowRadius = 12.dp, borderRadius = 16.dp)
                .background(welcomeBackground, RoundedCornerShape(16.dp))
                .border(1.5.dp, neonCyan.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
        ) {
            // Title
            Text(
                text = stringResource(R.string.split_bill_title),
                color = neonPurple,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                style = TextStyle(shadow = Shadow(
                    color = neonPurple.copy(alpha = 0.6f),
                    blurRadius = 25f
                ))
            )

            Spacer(Modifier.height(12.dp))

            // EQUALLY / CUSTOM toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                neonCyan.copy(alpha = 0.4f),
                                neonPurple.copy(alpha = 0.8f)
                            )
                        ),
                        shape = RoundedCornerShape(50.dp))

                    .background(Color(0xFF0D1525), RoundedCornerShape(50.dp))
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
                                    Modifier.background(neonCyan, RoundedCornerShape(50.dp))
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
                            color = if (isSelected) Color(0xFF0A0E1A) else normalText,
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
                color = neonCyan,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 24.dp),
                style = TextStyle(shadow = Shadow(
                    color = neonCyan.copy(alpha = 0.6f),
                    blurRadius = 25f
                ))
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
                    .background(InputFieldBackground)
                    .padding(horizontal = 15.dp)
                    .border(width = 1.dp, color = neonCyan.copy(alpha = 0.4f), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.split_bill_remaining),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "PHP %.2f".format(remaining),
                    color = neonMint,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(20.dp))
            // Confirm Request button
            val isConfirmEnabled = kotlin.math.abs(remaining) < 0.01
            val confirmBorderColor = if (isConfirmEnabled) neonPurple else neonPurple.copy(alpha = 0.3f)
            val confirmTextColor = if (isConfirmEnabled) neonPurple else neonPurple.copy(alpha = 0.35f)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(40.dp)
//                    .then(
//                        if (isConfirmEnabled)
//                            Modifier.neonGlow(neonPurple, alpha = 0.45f, glowRadius = 10.dp, borderRadius = 12.dp)
//                        else Modifier
//                    )
                    .border(1.5.dp, confirmBorderColor, RoundedCornerShape(50.dp))
                    .background(neonPurple.copy(alpha = if (isConfirmEnabled) 0.15f else 0.05f), RoundedCornerShape(12.dp))
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
                    color = confirmTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(20.dp))
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
                painter = painterResource(R.mipmap.ic_male),
            )
        }

        Spacer(Modifier.width(10.dp))

        Text(
            text = displayName,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        // Amount input
        val borderColor = when {
            !enabled -> neonCyan.copy(alpha = 0.25f)
            else     -> neonCyan.copy(alpha = 0.4f)
        }
        val textColor = if (enabled) Color.White else Color.White.copy(alpha = 0.7f)
        BasicTextField(
            value = amount,
            onValueChange = onAmountChange,
            modifier = Modifier
                .height(40.dp)
                .width(110.dp),
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = textColor,
                fontSize = 14.sp
            ),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(neonCyan),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(InputFieldBackground, RoundedCornerShape(8.dp))
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

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SplitBillParticipantItemPreview() {
    MaterialTheme {
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
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SplitBillDialogPreview() {
    MaterialTheme {
        SplitBillDialog(
            totalAmount = 350.0,
            friendList = listOf(
                FriendResponse(id = "F001", name = "Friend A", nickName = "frienda"),
                FriendResponse(id = "F002", name = "Friend B", nickName = "friendb")
            ),
            myName = "Hank Liu",
            onDismiss = {}
        )
    }
}

