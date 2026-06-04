package com.example.newproject.ui.scanpay

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newproject.R
import com.example.newproject.ui.Routes
import com.example.newproject.ui.components.SubPageTopBar
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.balanceGold
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun InputAmountScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    InputAmountContent(
        uiState = uiState,
        onBack = onBack,
        onReviewDetails = { amount ->
            viewModel.setAmount(amount)
            onNavigate(Routes.SCAN_PAY_CONFIRM_PAYMENT)
        }
    )
}

@Composable
private fun InputAmountContent(
    uiState: ScanPayUiState,
    onBack: () -> Unit = {},
    onReviewDetails: (String) -> Unit = {}
) {
    var amount by remember { mutableStateOf("") }
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
                title = stringResource(R.string.input_amount_title),
                onBack = onBack
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .neonGlow(
                        color = neonCyan,
                        alpha = 0.4f,
                        glowRadius = 12.dp,
                        borderRadius = 12.dp
                    )
                    .background(color = welcomeBackground, shape = RoundedCornerShape(12.dp))
                    .border(
                        width = 1.5.dp,
                        color = neonCyan.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .neonGlow(
                            color = neonPurple,
                            alpha = 0.25f,
                            glowRadius = 12.dp,
                            borderRadius = 12.dp
                        )
                        .padding(10.dp)
//                        .background(welcomeBackground, RoundedCornerShape(12.dp))
                        .border(1.5.dp, neonPurple.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = welcomeBackground)
                                .border(
                                    width = 1.5.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            neonCyan.copy(alpha = 0.7f),
                                            neonPurple.copy(alpha = 0.7f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ){

                        }
                        Spacer(Modifier.height(5.dp))
                        Text(
                            text = "@${uiState.recipientNickName}",
                            color = neonPurpleLight,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = uiState.recipientName,
                            color = neonPurpleLight,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            Spacer(Modifier.height(32.dp))

            // ── Amount Input ─────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.input_amount_currency),
                    color = neonCyan,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(color = neonCyan, blurRadius = 15f)
                    )
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        textStyle = TextStyle(
                            color = neonCyan,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(color = neonCyan, blurRadius = 15f)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        cursorBrush = SolidColor(neonCyan),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (amount.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.input_amount_hint),
                                    color = neonCyan.copy(alpha = 0.3f),
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        shadow = Shadow(color = neonCyan, blurRadius = 15f)
                                    )
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(R.string.input_amount_title),
                color = neonCyan.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(stringResource(R.string.input_amount_title))
            ){
                Image(
                    painter = painterResource(R.mipmap.ic_car),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(vertical = 15.dp)
                        .size(80.dp)
                        .neonGlow(color = neonPurple, alpha = 0.25f, glowRadius = 30.dp)

                )
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.scan_pay_my_qr_balance),
                        color = normalText,
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBalanceVisible) "PHP ${uiState.balance}" else "••••",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(R.string.balance_toggle_desc),
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { isBalanceVisible = !isBalanceVisible }
                    )
                }
                Image(
                    painter = painterResource(R.mipmap.ic_monkey),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .neonGlow(color = balanceGold, alpha = 0.3f, glowRadius = 30.dp)
                        .align(Alignment.TopEnd)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .neonGlow(
                            color = neonPurple,
                            alpha = 0.7f,
                            glowRadius = 12.dp,
                            borderRadius = 15.dp
                        )
                        .background(color = neonPurple, shape = RoundedCornerShape(30.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onReviewDetails(amount) }
                        .padding(horizontal = 40.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.input_amount_review_details),
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun InputAmountScreenPreview() {
    MaterialTheme {
        InputAmountContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner"
            )
        )
    }
}
