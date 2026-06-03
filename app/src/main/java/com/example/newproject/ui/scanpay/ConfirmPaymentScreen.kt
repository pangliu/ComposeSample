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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.newproject.ui.components.SubPageTopBar
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.balanceGold
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonCyanLight
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.qrCodeBackground
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun ConfirmPaymentScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var isBalanceVisible by remember { mutableStateOf(false) }
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
                                text = "@testName",
                                color = neonCyanLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Mark",
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
                        text = stringResource(R.string.scan_pay_my_qr_x_points),
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
            MyQrActionButton(
                iconRes = R.mipmap.ic_money,
                label = stringResource(R.string.scan_pay_my_qr_split_bill_btn),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
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
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.5.dp,
                            color = neonPurple,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .neonGlow(
                            color = neonPurple,
                            alpha = 0.4f,
                            glowRadius = 20.dp,
                            borderRadius = 20.dp
                        )
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Close,
                        tint = neonPurple,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(R.string.confirm_payment_cancel),
                        color = neonPurpleLight
                    )
                }
                Spacer(Modifier.width(10.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.5.dp,
                            color = neonCyan,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .neonGlow(
                            color = neonCyan,
                            alpha = 0.6f,
                            glowRadius = 20.dp,
                            borderRadius = 20.dp
                        )
                        .padding(5.dp)
                        .background(
                            color = neonCyan,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 3.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Check,
                        tint = Color.Black,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(R.string.confirm_payment_confirm_pay),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
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
        ConfirmPaymentScreen()
    }
}
