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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.components.QrMode
import com.example.newproject.ui.components.QrModeTabSelector
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.balanceGold
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun ScanPayScreen() {
    var selectedMode by rememberSaveable { mutableStateOf(QrMode.MY_QR) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(welcomeBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // SCAN QR / MY QR 切換 Tab
        QrModeTabSelector(
            selectedMode = selectedMode,
            onModeChange = { selectedMode = it },
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        when (selectedMode) {
            QrMode.SCAN_QR -> ScanQrContent()
            QrMode.MY_QR   -> MyQrContent()
        }
    }
}

@Composable
private fun ScanQrContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .aspectRatio(1f)
            .border(2.dp, neonCyan, RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.scan_pay_hint),
            color = normalText,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun MyQrContent() {
    var isBalanceVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
//            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── QR Code Section ──────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            // QR Card（保留左右 28dp 讓裝飾 icon 能顯示在卡片兩側）
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.mipmap.bg_qrcode),
                    contentDescription = stringResource(R.string.scan_pay_my_qr_qr_code_desc),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 5.dp),
                    text = stringResource(R.string.scan_pay_my_qr_username),
                    color = neonCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 5.dp),
                    text = stringResource(R.string.scan_pay_my_qr_name),
                    color = normalText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Balance Section ──────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Image(
                painter = painterResource(R.mipmap.ic_car),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .neonGlow(
                        color = neonPurple,
                        alpha = 0.25f,
                        glowRadius = 30.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.scan_pay_my_qr_balance),
                        color = normalText,
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBalanceVisible) "PHP 1000" else "••••",
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
                    .neonGlow(
                        color = balanceGold,
                        alpha = 0.3f,
                        glowRadius = 30.dp)
                    .align(Alignment.CenterVertically)
            )
//            Text(
//                text = stringResource(R.string.scan_pay_my_qr_x_points),
//                color = neonCyan,
//                fontSize = 13.sp
//            )
        }


        Spacer(Modifier.height(20.dp))

        // ── Action Buttons ───────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MyQrActionButton(
                iconRes = R.mipmap.ic_gift,
                label = stringResource(R.string.scan_pay_my_qr_generate_ang_pao_btn),
                modifier = Modifier.weight(1f)
            )
            MyQrActionButton(
                iconRes = R.mipmap.ic_money,
                label = stringResource(R.string.scan_pay_my_qr_split_bill_btn),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Daily Quests Card ────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .neonGlow(
                    color = neonCyan,
                    alpha = 0.5f,
                    glowRadius = 8.dp,
                    borderRadius = 12.dp)
                .border(
                    width = 1.5.dp,
                    color = neonCyan.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp))
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

//            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.scan_pay_my_qr_daily_quest_title),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string.scan_pay_my_qr_daily_quest_progress),
                        color = normalText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = 0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(50)),
                    color = neonCyan,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.scan_pay_my_qr_daily_quest_progress),
                    color = normalText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
//                Spacer(Modifier.height(6.dp))
            }
            Spacer(Modifier.width(10.dp))
            Image(
                painter = painterResource(R.mipmap.ic_girl),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun MyQrActionButton(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .border(
                width = 1.5.dp,
                color = neonPurple.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp))
            .neonGlow(
                color = neonPurple,
                alpha = 0.3f,
                glowRadius = 8.dp,
                borderRadius = 12.dp)
            .background(
                color = welcomeBackground.copy(alpha = 0.7f),
                shape = RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .border(
                    color = neonCyan,
                    width = 1.5.dp,
                    shape = RoundedCornerShape(50.dp))
                .neonGlow(
                    color = neonCyan,
                    alpha = 0.7f,
                    glowRadius = 50.dp,
                    borderRadius = 50.dp)
                .background(
                    color = welcomeBackground.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(50.dp))
                .padding(10.dp)

        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(5.dp))
        Text(
            text = label,
            color = normalText,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            lineHeight = 15.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun ScanPayScreenPreview() {
    MaterialTheme {
        ScanPayScreen()
    }
}
