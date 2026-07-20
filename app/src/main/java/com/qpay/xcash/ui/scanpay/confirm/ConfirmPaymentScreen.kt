package com.qpay.xcash.ui.scanpay.confirm

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import android.widget.Toast
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.profile.transaction.formatAmount
import com.qpay.xcash.ui.scanpay.ScanPayNavigationEvent
import com.qpay.xcash.ui.scanpay.ScanPayUiState
import com.qpay.xcash.ui.scanpay.ScanPayViewModel
import com.qpay.xcash.ui.scanpay.dialog.SelectSplitPartnerDialog
import com.qpay.xcash.ui.scanpay.dialog.SplitBillDialog
import com.qpay.xcash.ui.scanpay.components.SplitPartnersRow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.NeonSwitch
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.scanpay.components.MyQrActionButton
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.lemonYellow
import com.qpay.xcash.ui.theme.neonPink
import com.qpay.xcash.ui.theme.neonRed

@Composable
fun ConfirmPaymentScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.clearSplitBillState()
    }
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ScanPayNavigationEvent.PaymentSuccess ->
                    onNavigate(Routes.SCAN_PAY_TRANSACTION_SUCCESSFUL)
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                else -> Unit
            }
        }
    }
    if (uiState.showSelectPartnerDialog) {
        SelectSplitPartnerDialog(
            friendList = uiState.friendList,
            totalAmount = uiState.amount.toDoubleOrNull() ?: 0.0,
            onDismiss = { viewModel.dismissSelectPartnerDialog() },
            onConfirm = { selected -> viewModel.onPartnersConfirmed(selected) }
        )
    }

    if (uiState.showSplitBillDialog) {
        SplitBillDialog(
            totalAmount = uiState.amount.toDoubleOrNull() ?: 0.0,
            friendList = uiState.selectedFriendList,
            myName = uiState.myUserName,
            onDismiss = { viewModel.dismissSplitBillDialog() },
            onConfirm = { viewModel.confirmSplitBill() }
        )
    }

    ConfirmPaymentContent(
        uiState = uiState,
        onBack = onBack,
        onConfirmPay = { viewModel.confirmPayment() },
        onSplitBill = { viewModel.fetchFriendListAndShowDialog() },
        onEditSplit = { viewModel.editSplitBill() },
        onCancelSplit = { viewModel.cancelSplitBill() }
    )
}

@Composable
private fun ConfirmPaymentContent(
    uiState: ScanPayUiState,
    onBack: () -> Unit = {},
    onConfirmPay: () -> Unit = {},
    onSplitBill: () -> Unit = {},
    onEditSplit: () -> Unit = {},
    onCancelSplit: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    var isBalanceVisible by remember { mutableStateOf(false) }
    var useXPoints by remember { mutableStateOf(false) }
    LoadingDialog(isShowing = uiState.isConfirming || uiState.isFetchingFriends)
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            assets.scanPayBackground?.let { resId ->
                Image(
                    painter = painterResource(resId),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SubPageTopBar(
                title = stringResource(R.string.confirm_payment_title),
                onBack = onBack
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val leftDecor = assets.qrSectionLeftDecor
                if (leftDecor != null) {
                    Image(
                        modifier = Modifier
                            .weight(0.2f),
//                        .fillMaxHeight(),
                        painter = painterResource(leftDecor),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.CenterEnd,
                        contentDescription = stringResource(R.string.scan_pay_my_qr_left_qr_code_desc),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(0.2f))
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .weight(0.65f)
                ) {
                    val confirmCardBg = assets.confirmPaymentCardBg
                    Column(
                        modifier = Modifier
                            .testTag("confirm_payment")
                            .then(
                                if (confirmCardBg != null)
                                // Black Gold：背景圖自帶邊框，不另畫 border / 純色底
                                // sizeToIntrinsics = false：高度由內容決定，背景圖只鋪滿、不參與量測
                                    Modifier
                                        .clip(RoundedCornerShape(15.dp))
                                        .paint(
                                            painter = painterResource(confirmCardBg),
//                                        contentScale = ContentScale.FillBounds
                                        )
                                else
                                    Modifier
                                        .border(
                                            width = 1.5.dp,
                                            color = colors.accent.primary,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .then(
                                            if (colors.effect.enableGlow)
                                                Modifier.neonGlow(
                                                    color = colors.accent.primary,
                                                    alpha = 0.6f,
                                                    glowRadius = 8.dp,
                                                    borderRadius = 8.dp
                                                )
                                            else Modifier
                                        )
                                        .background(
                                            color = colors.scanPay.confirm.cardBackground,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                            )
//                            .align(Alignment.Top)
//                            .weight(0.65f)
                            .aspectRatio(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp, horizontal = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .then(
                                        if (colors.effect.enableGlow)
                                            Modifier.neonGlow(
                                                color = colors.accent.primary,
                                                alpha = 0.6f,
                                                glowRadius = 8.dp,
                                                borderRadius = 8.dp
                                            )
                                        else Modifier
                                    )
                                    .then(
                                        colors.scanPay.confirm.avatarBorder?.let { borderColor ->
                                            Modifier.border(
                                                width = 1.5.dp,
                                                color = borderColor,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                        } ?: Modifier
                                    )
                                    .background(
                                        color = colors.bg.page,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Pay To:",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "@${uiState.recipientNickName}",
                                    color = colors.scanPay.confirm.nickNameText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = uiState.recipientName,
                                    color = colors.scanPay.confirm.nameText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "100"
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val amountGlowShadow = if (colors.effect.enableGlow)
                                Shadow(color = colors.accent.primary, blurRadius = 15f)
                            else null
                            Text(
                                text = stringResource(R.string.input_amount_currency),
                                color = colors.accent.primary,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(shadow = amountGlowShadow)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "%,.2f".format(uiState.amount.toDoubleOrNull() ?: 0.0),
                                color = colors.accent.primary,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(shadow = amountGlowShadow)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = stringResource(R.string.scan_pay_my_qr_x_points),
                                color = colors.scanPay.confirm.hintText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.scan_pay_my_qr_confirm_hint),
                                color = colors.scanPay.confirm.hintText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    assets.confirmPaymentQrCrownDecor?.let { resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = "qrcode_crown",
                            modifier = Modifier
                                .size(80.dp)
                                .offset(x = -35.dp, y = -40.dp)
                                .rotate(-35f)
                        )
                    }
                }
                val rightDecor = assets.qrSectionRightDecor
                if (rightDecor != null) {
                    Image(
                        modifier = Modifier
                            .weight(0.2f),
//                        .fillMaxHeight(),
                        painter = painterResource(rightDecor),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.CenterStart,
                        contentDescription = stringResource(R.string.scan_pay_my_qr_right_qr_code_desc)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(0.2f))
                }
            }
//            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            ) {
                Image(
                    painter = painterResource(assets.myQrLeftDecorIcon),
                    contentDescription = "balance_image",
                    modifier = Modifier
                        .size(70.dp)
                        .rotate(-20f)
                        .alpha(if (assets.confirmPaymentBalanceDecor != null) 1f else 0f)
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    color = colors.accent.secondary,
                                    alpha = 0.25f,
                                    glowRadius = 30.dp
                                )
                            else Modifier
                        )
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                color = colors.scanPay.balanceDivider,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "PAYING FROM:",
                            color = colors.scanPay.confirm.balanceLabelText,
                            fontSize = 14.sp,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Wallet name",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    val balanceValue = "%,.2f".format(uiState.balance)
                    val balanceText = stringResource(
                        R.string.scan_pay_my_qr_available_balance,
                        balanceValue
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = buildAnnotatedString {
                            append(balanceText)
                            val valueStart = balanceText.lastIndexOf(balanceValue)
                            if (valueStart >= 0) {
                                addStyle(
                                    SpanStyle(color = colors.scanPay.confirm.balanceValueText),
                                    valueStart,
                                    valueStart + balanceValue.length
                                )
                            }
                        },
                        color = colors.scanPay.confirm.balanceLabelText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                color = colors.scanPay.balanceDivider,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
                Image(
                    painter = painterResource(assets.myQrRightDecorIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(color = lemonYellow, alpha = 0.3f, glowRadius = 30.dp)
                            else Modifier
                        )
                        .align(Alignment.CenterVertically)
                )
            }
//            Spacer(Modifier.height(10.dp))
            if (uiState.confirmErrorMessage.isNotEmpty()) {
                Text(
                    text = uiState.confirmErrorMessage,
                    color = neonRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            } else {
                MyQrActionButton(
                    iconRes = R.mipmap.ic_money,
                    label = stringResource(R.string.scan_pay_my_qr_split_bill_btn),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fillWidth = false,
                    onClick = onSplitBill
                )
            }
            if (uiState.confirmedSplitPartners.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                SplitPartnersRow(
                    myName = uiState.myUserName,
                    partners = uiState.confirmedSplitPartners,
                    onEdit = onEditSplit,
                    onCancel = onCancelSplit
                )
            }
            Spacer(Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .border(
                        width = 1.5.dp,
                        color = colors.accent.primary.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(12.dp)
                    )
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
                        color = colors.bg.page,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(R.mipmap.ic_balance_coin),
                    contentDescription = stringResource(id = R.string.balance_coin),
                    tint = Color.Companion.Unspecified,

                    modifier = Modifier.Companion
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    color = lemonYellow,
                                    alpha = 0.7f,
                                    glowRadius = 10.dp
                                )
                            else Modifier
                        )
                        .size(30.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.scan_pay_my_qr_use_points),
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(
                            R.string.scan_pay_my_qr_points_balance,
                            formatAmount(uiState.tokenBalance)
                        ),
                        color = colors.text.body,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
//                Switch(
//                    modifier = Modifier.scale(0.8f),
//                    checked = useXPoints,
//                    onCheckedChange = { useXPoints = it },
//                    colors = SwitchDefaults.colors(
//                        checkedThumbColor = Color.White,
//                        checkedTrackColor = Color.Gray,
//                        checkedBorderColor = neonCyan,
//                        uncheckedThumbColor = Color.White,
//                        uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
//                    )
//                )
                NeonSwitch(
                    checked = useXPoints,
                    onCheckedChange = { useXPoints = it },
                    activeColor = colors.accent.primary,
                    showLabel = true
                )
            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .border(
                            width = 1.5.dp,
                            color = colors.scanPay.confirm.cancelButtonBorder,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    color = colors.accent.secondary,
                                    alpha = 0.4f,
                                    glowRadius = 25.dp,
                                    borderRadius = 25.dp
                                )
                            else Modifier
                        )
                        .background(
                            brush = colors.scanPay.confirm.cancelButtonFill,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onBack() }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Close,
                        tint = colors.scanPay.confirm.cancelIcon,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(R.string.confirm_payment_cancel),
                        fontSize = 14.sp,
                        color = colors.scanPay.confirm.cancelText,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(10.dp))
                val hasError = uiState.confirmErrorMessage.isNotEmpty()
                val confirmBtnGlowColor = if (hasError) neonPink else colors.accent.primary
                val confirmBtnFill = if (hasError) SolidColor(neonPink)
                else colors.scanPay.confirm.payButtonFill
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .border(
                            width = 1.5.dp,
                            color = colors.accent.primary,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    color = confirmBtnGlowColor,
                                    alpha = 0.6f,
                                    glowRadius = 25.dp,
                                    borderRadius = 25.dp
                                )
                            else Modifier
                        )
                        .clickable(
                            enabled = !uiState.isConfirming,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onConfirmPay() }
                        .padding(5.dp)
                        .background(
                            brush = confirmBtnFill,
                            shape = RoundedCornerShape(30.dp),
                            alpha = if (uiState.isConfirming) 0.4f else 1f
                        )
                ) {
                    if (!hasError) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Check,
                            tint = Color.Black,
                            contentDescription = null,
                        )
                    }
                    Text(
                        text = if (hasError) stringResource(R.string.confirm_payment_try_again)
                        else stringResource(R.string.confirm_payment_confirm_pay),
                        color = if (hasError) Color.White
                        else Color.Black,
                        fontSize = if (hasError) 12.sp
                        else 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        }
    }
}


@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun ConfirmPaymentScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        ConfirmPaymentContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner",
                amount = "100.00"
            )
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun ConfirmPaymentScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        ConfirmPaymentContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner",
                amount = "100.00"
            )
        )
    }
}
