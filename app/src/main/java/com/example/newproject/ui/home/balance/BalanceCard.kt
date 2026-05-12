package com.example.newproject.ui.home.balance

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.BalanceGold
import com.example.newproject.ui.theme.BalanceSwitchBackground
import com.example.newproject.ui.theme.BalanceVisibility
import com.example.newproject.ui.theme.CardStringLight
import com.example.newproject.ui.theme.CardStringNormal
import com.example.newproject.ui.theme.CashInGreen
import com.example.newproject.ui.theme.DarkBackground
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NormalText
import com.example.newproject.ui.theme.SendPink

@SuppressLint("DefaultLocale")
@Composable
fun BalanceCard(cashBalance: Double, tokenBalance: Double) {
    var isBalanceHidden by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
    ) {
        // 1. 使用 Image 當作背景，它會根據原始比例撐開 Box 的高度
        Image(
            painter = painterResource(R.mipmap.bg_balance_card),
            contentDescription = null,
            contentScale = ContentScale.Companion.FillWidth,
            modifier = Modifier.Companion.fillMaxWidth()
        )

        // 2. 讓 Column 充滿整個 Box 的大小，並把 padding 設定在這裡
        Column(
            modifier = Modifier.Companion
                .matchParentSize()
                .padding(horizontal = 15.dp, vertical = 15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ── 第一行：BALANCE 標題 + 眼睛 + Cash In 按鈕 ──
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                // 左側：BALANCE 標題 + 鎖圖示
                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    Text(
                        text = stringResource(R.string.balance_title),
                        color = CardStringNormal,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                }


                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    Box(
                        modifier = Modifier.Companion
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(BalanceVisibility)
                            .clickable {}
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = stringResource(R.string.balance_toggle_desc),
                            tint = if (isBalanceHidden) NeonCyan else Color.Companion.Gray,
                            modifier = Modifier.Companion
                                .size(18.dp)
                                .clickable { isBalanceHidden = !isBalanceHidden }
                        )
                    }
                    Spacer(modifier = Modifier.Companion.width(15.dp))
                    // 右側：Cash In 綠色膠囊按鈕
                    Box(
                        modifier = Modifier.Companion
                            .neonGlow(
                                color = CashInGreen,
                                alpha = 0.6f,
                                glowRadius = 15.dp,
                                borderRadius = 8.dp
                            )
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                            .background(CashInGreen)
                            .clickable { /* TODO: Cash In */ }
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.mipmap.ic_cash_in),
                                contentDescription = stringResource(R.string.balance_cash_in),
                                tint = Color.Companion.Unspecified, // 若圖片本身為黑色則會顯示原貌，若需強制塗成黑色請改為 Color.Black
                                modifier = Modifier.Companion.size(14.dp)
                            )
                            Spacer(modifier = Modifier.Companion.width(4.dp))
                            Text(
                                text = stringResource(R.string.balance_cash_in),
                                color = Color.Companion.Black,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Companion.Bold
                            )
                        }
                    }
                }
            }

            // ── 第二行：大金額 + Send 粉紫色膠囊按鈕 ──
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Text(
                    text = if (isBalanceHidden) "PHP ••••••" else "PHP ${
                        String.format(
                            "%,.0f",
                            cashBalance
                        )
                    }",
                    color = CardStringLight,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Companion.ExtraBold
                )

                // Send 粉紫色膠囊按鈕
                Box(
                    modifier = Modifier.Companion
                        .neonGlow(
                            color = SendPink,
                            alpha = 0.6f,
                            glowRadius = 15.dp,
                            borderRadius = 8.dp
                        )
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                        .background(SendPink)
                        .clickable { /* TODO: Send */ }
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.mipmap.ic_balance_send),
                            contentDescription = stringResource(R.string.balance_send),
                            tint = Color.Companion.Unspecified,
                            modifier = Modifier.Companion.size(14.dp)
                        )
                        Spacer(modifier = Modifier.Companion.width(4.dp))
                        Text(
                            text = stringResource(R.string.balance_send),
                            color = Color.Companion.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Companion.Bold
                        )
                    }
                }
            }

            // ── 第三行：Token 金幣 + 數量 + Balance Switch 按鈕 ──
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                // 左側：金幣圓圈 + Token 數量
                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    Icon(
                        painter = painterResource(R.mipmap.ic_balance_coin),
                        contentDescription = stringResource(id = R.string.balance_coin),
                        tint = Color.Companion.Unspecified,

                        modifier = Modifier.Companion
                            .neonGlow(color = BalanceGold, alpha = 0.7f, glowRadius = 10.dp)
                            .size(50.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(10.dp))
                    Text(
                        text = if (isBalanceHidden) "••••" else String.format(
                            "%,.0f",
                            tokenBalance
                        ),
                        color = CardStringNormal,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Companion.Bold
                    )
                }

                // 右側：Balance Switch 深色膠囊按鈕
                Box(
                    modifier = Modifier.Companion
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                        .background(BalanceSwitchBackground)
                        .border(
                            width = 1.dp,
                            color = BalanceVisibility,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp)
                        )
                        .clickable { /* TODO: Balance Switch */ }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                        Icon(
                            painter = painterResource(R.mipmap.ic_switch_balance),
                            tint = NormalText,
                            contentDescription = stringResource(R.string.balance_switch),
                            modifier = Modifier.Companion.size(18.dp)
                        )
                        Spacer(modifier = Modifier.Companion.width(4.dp))
                        Text(
                            text = stringResource(R.string.balance_switch),
                            color = NormalText,
                            fontSize = 14.sp
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
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(DarkBackground)
                .padding(16.dp)
        ) {
            BalanceCard(cashBalance = 12345.0, tokenBalance = 500.0)
        }
    }
}