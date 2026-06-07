package com.example.newproject.ui.scanpay

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import android.widget.Toast
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
import com.example.newproject.ui.UiEvent
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.Routes
import com.example.newproject.ui.components.LoadingDialog
import com.example.newproject.ui.components.SubPageTopBar
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.balanceGold
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonCyanLight
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.neonPink
import com.example.newproject.ui.theme.neonRed
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.qrCodeBackground
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun ConfirmPaymentScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
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
    ConfirmPaymentContent(
        uiState = uiState,
        onBack = onBack,
        onConfirmPay = { viewModel.confirmPayment() }
    )
}

@Composable
private fun ConfirmPaymentContent(
    uiState: ScanPayUiState,
    onBack: () -> Unit = {},
    onConfirmPay: () -> Unit = {}
) {
    var isBalanceVisible by remember { mutableStateOf(false) }
    var useXPoints by remember { mutableStateOf(false) }
    LoadingDialog(isShowing = uiState.isConfirming)
    Scaffold(
        containerColor = welcomeBackground,
        contentColor = Color.White
    ) { paddingValues ->
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
                Image(
                    modifier = Modifier
                        .weight(0.2f),
//                        .fillMaxHeight(),
                    painter = painterResource(R.mipmap.bg_left_qrcode),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.CenterEnd,
                    contentDescription = stringResource(R.string.scan_pay_my_qr_left_qr_code_desc),
                )
                Column(
                    modifier = Modifier
                        .testTag("confirm_payment")
                        .border(
                            width = 1.5.dp,
                            color = neonCyan,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .neonGlow(
                            color = neonCyan,
                            alpha = 0.6f,
                            glowRadius = 8.dp,
                            borderRadius = 8.dp
                        )
                        .background(
                            color = qrCodeBackground,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .align(Alignment.CenterVertically)
                        .weight(0.65f)
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
                                .neonGlow(
                                    color = neonCyan,
                                    alpha = 0.6f,
                                    glowRadius = 8.dp,
                                    borderRadius = 8.dp
                                )
                                .background(
                                    color = welcomeBackground,
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
                                color = neonCyanLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = uiState.recipientName,
                                color = neonPurpleLight,
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
                        Text(
                            text = stringResource(R.string.input_amount_currency),
                            color = neonCyan,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(
                                shadow = Shadow(color = neonCyan, blurRadius = 15f)
                            )
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = uiState.amount.ifEmpty { "0.00" },
                            color = neonCyan,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(
                                shadow = Shadow(color = neonCyan, blurRadius = 15f)
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = stringResource(R.string.scan_pay_my_qr_x_points),
                            color = normalText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.scan_pay_my_qr_confirm_hint),
                            color = normalText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Image(
                    modifier = Modifier
                        .weight(0.2f),
//                        .fillMaxHeight(),
                    painter = painterResource(R.mipmap.bg_right_qrcode),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.CenterStart,
                    contentDescription = stringResource(R.string.scan_pay_my_qr_right_qr_code_desc)
                )
            }
//            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            ) {
                Image(
                    painter = painterResource(R.mipmap.ic_car),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .neonGlow(color = neonPurple, alpha = 0.25f, glowRadius = 30.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "PAYING FROM:",
                            color = normalText,
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
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.scan_pay_my_qr_available_balance, uiState.balance),
                        color = normalText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Image(
                    painter = painterResource(R.mipmap.ic_monkey),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .neonGlow(color = balanceGold, alpha = 0.3f, glowRadius = 30.dp)
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
                    modifier = Modifier.align(Alignment.CenterHorizontally)
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
                        color = neonCyan.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(12.dp))
                    .neonGlow(
                        color = neonCyan,
                        alpha = 0.5f,
                        glowRadius = 8.dp,
                        borderRadius = 8.dp)
                    .background(
                        color = welcomeBackground,
                        shape = RoundedCornerShape(15.dp))
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(R.mipmap.ic_balance_coin),
                    contentDescription = stringResource(id = R.string.balance_coin),
                    tint = Color.Companion.Unspecified,

                    modifier = Modifier.Companion
                        .neonGlow(
                            color = balanceGold,
                            alpha = 0.7f,
                            glowRadius = 10.dp)
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
                        text = stringResource(R.string.scan_pay_my_qr_points_balance, uiState.tokenBalance),
                        color = normalText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Switch(
                    modifier = Modifier.scale(0.8f),
                    checked = useXPoints,
                    onCheckedChange = { useXPoints = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Gray,
                        checkedBorderColor = neonCyan,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
                    )
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
                            color = neonPurple,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .neonGlow(
                            color = neonPurple,
                            alpha = 0.4f,
                            glowRadius = 25.dp,
                            borderRadius = 25.dp
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onBack() }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Close,
                        tint = neonPurple,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(R.string.confirm_payment_cancel),
                        fontSize = 14.sp,
                        color = neonPurpleLight,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(10.dp))
                val hasError = uiState.confirmErrorMessage.isNotEmpty()
                val confirmBtnColor = if (hasError) neonPink else neonCyan
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .border(
                            width = 1.5.dp,
                            color = neonCyan,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .neonGlow(
                            color = confirmBtnColor,
                            alpha = 0.6f,
                            glowRadius = 25.dp,
                            borderRadius = 25.dp
                        )
                        .clickable(
                            enabled = !uiState.isConfirming,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onConfirmPay() }
                        .padding(5.dp)
                        .background(
                            color = if (uiState.isConfirming) confirmBtnColor.copy(alpha = 0.4f) else confirmBtnColor,
                            shape = RoundedCornerShape(30.dp)
                        )
                ) {
                    if(!hasError) {
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
                        fontSize = if(hasError)12.sp
                                else 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun ConfirmPaymentScreenPreview() {
    MaterialTheme {
        ConfirmPaymentContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner",
                amount = "100.00"
            )
        )
    }
}
