package com.example.newproject.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.theme.BalanceSwitchBackground
import com.example.newproject.ui.theme.CardBorder
import com.example.newproject.ui.theme.CardGradientEnd
import com.example.newproject.ui.theme.CardGradientMid
import com.example.newproject.ui.theme.CardGradientStart
import com.example.newproject.ui.theme.CardShadow
import com.example.newproject.ui.theme.CashInGreen
import com.example.newproject.ui.theme.DarkBackground
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.SendPink
import com.example.newproject.ui.theme.TokenGold
import com.example.newproject.ui.theme.TokenOrange
import com.example.newproject.ui.theme.TokenTextPurple

// ── Balance Card ─────────────────────────────────────────────────────────────

@Composable
fun BalanceCard(cashBalance: Double, tokenBalance: Double) {
    var isBalanceHidden by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 20.dp, spotColor = CardShadow.copy(alpha = 0.6f), shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        CardGradientStart,
                        CardGradientMid,
                        CardGradientEnd
                    )
                )
            )
            .border(1.dp, CardBorder.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            // ── 第一行：BALANCE 標題 + 眼睛 + Cash In 按鈕 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左側：BALANCE 標題 + 鎖圖示
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.balance_title),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.balance_toggle_desc),
                        tint = if (isBalanceHidden) NeonCyan else Color.Gray,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { isBalanceHidden = !isBalanceHidden }
                    )
                }

                // 右側：Cash In 綠色膠囊按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(CashInGreen)
                        .clickable { /* TODO: Cash In */ }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.balance_cash_in),
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.balance_cash_in),
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ── 第二行：大金額 + Send 粉紫色膠囊按鈕 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBalanceHidden) "PHP ••••••" else "PHP ${String.format("%,.0f", cashBalance)}",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                // Send 粉紫色膠囊按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(SendPink)
                        .clickable { /* TODO: Send */ }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = stringResource(R.string.balance_send),
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.balance_send),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ── 第三行：Token 金幣 + 數量 + Balance Switch 按鈕 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左側：金幣圓圈 + Token 數量
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(6.dp, CircleShape, spotColor = TokenGold)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(TokenGold, TokenOrange)
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.balance_token_symbol),
                            color = TokenTextPurple,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isBalanceHidden) "••••" else String.format("%,.0f", tokenBalance),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 右側：Balance Switch 深色膠囊按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(BalanceSwitchBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(50.dp))
                        .clickable { /* TODO: Balance Switch */ }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.balance_switch),
                            tint = Color.Gray,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.balance_switch),
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun BalanceCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBackground)
                .padding(16.dp)
        ) {
            BalanceCard(cashBalance = 12345.0, tokenBalance = 500.0)
        }
    }
}
