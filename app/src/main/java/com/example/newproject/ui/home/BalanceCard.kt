package com.example.newproject.ui.home

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.theme.BalanceSwitchBackground
import com.example.newproject.ui.theme.CardGradientEnd
import com.example.newproject.ui.theme.CardGradientMid
import com.example.newproject.ui.theme.CardGradientStart
import com.example.newproject.ui.theme.CardShadow
import com.example.newproject.ui.theme.CardStringLight
import com.example.newproject.ui.theme.CardStringNormal
import com.example.newproject.ui.theme.CashInGreen
import com.example.newproject.ui.theme.DarkBackground
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.SendPink
import com.example.newproject.ui.theme.TokenGold
import com.example.newproject.ui.theme.TokenOrange
import com.example.newproject.ui.theme.TokenTextPurple

// ── Balance Card ─────────────────────────────────────────────────────────────

@SuppressLint("DefaultLocale")
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
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = 0.4f),
                        NeonPurple.copy(alpha = 0.8f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 15.dp, vertical = 10.dp)
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
                        color = CardStringNormal,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.balance_toggle_desc),
                        tint = if (isBalanceHidden) NeonCyan else Color.Gray,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { isBalanceHidden = !isBalanceHidden }
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    // 右側：Cash In 綠色膠囊按鈕
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(8.dp),
                                spotColor = CashInGreen // 使用綠色產生光暈感，若要一般陰影可改用 Color.Black
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(CashInGreen)
                            .clickable { /* TODO: Cash In */ }
                            .padding(horizontal = 14.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.mipmap.ic_cash_in),
                                contentDescription = stringResource(R.string.balance_cash_in),
                                tint = Color.Unspecified, // 若圖片本身為黑色則會顯示原貌，若需強制塗成黑色請改為 Color.Black
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
            }

            // ── 第二行：大金額 + Send 粉紫色膠囊按鈕 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBalanceHidden) "PHP ••••••" else "PHP ${String.format("%,.0f", cashBalance)}",
                    color = CardStringLight,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                // Send 粉紫色膠囊按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SendPink)
                        .clickable { /* TODO: Send */ }
                        .padding(horizontal = 14.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.mipmap.ic_balance_send),
                            contentDescription = stringResource(R.string.balance_send),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.balance_send),
                            color = Color.Black,
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
                        color = CardStringNormal,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 右側：Balance Switch 深色膠囊按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(BalanceSwitchBackground)
                        .border(
                            width = 1.dp,
                            color = BalanceSwitchBackground,
                            shape = RoundedCornerShape(50.dp))
                        .clickable { /* TODO: Balance Switch */ }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.mipmap.ic_switch_balance),
                            tint = CardStringNormal,
                            contentDescription = stringResource(R.string.balance_switch),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.balance_switch),
                            color = CardStringNormal,
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
