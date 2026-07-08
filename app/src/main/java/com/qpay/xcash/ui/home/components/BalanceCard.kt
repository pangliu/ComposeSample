package com.qpay.xcash.ui.home.components

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.duskIndigo
import com.qpay.xcash.ui.theme.frostWhite
import com.qpay.xcash.ui.theme.lemonYellow
import com.qpay.xcash.ui.theme.deepNavy
import com.qpay.xcash.ui.theme.mistGray

@SuppressLint("DefaultLocale")
@Composable
fun BalanceCard(cashBalance: Double, tokenBalance: Double) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    var isBalanceHidden by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
    ) {
        // 1. 使用 Image 當作背景，它會根據原始比例撐開 Box 的高度
        Image(
            painter = painterResource(assets.balanceCardBackground),
            contentDescription = null,
            contentScale = ContentScale.Companion.FillWidth,
            modifier = Modifier.Companion.fillMaxWidth()
        )

        // 2. 讓 Column 充滿整個 Box 的大小，並把 padding 設定在這裡
        Column(
            modifier = Modifier.Companion
                .matchParentSize()
                .padding(horizontal = 15.dp, vertical = 10.dp),
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
                    GradientText(
                        text = stringResource(R.string.balance_title),
                        color = mistGray,
                        brush = colors.gradient.goldShimmer,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                }


                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    Box(
                        modifier = Modifier.Companion
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(duskIndigo)
                            .clickable {}
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = stringResource(R.string.balance_toggle_desc),
                            tint = if (isBalanceHidden) colors.accent.primary else Color.Companion.Gray,
                            modifier = Modifier.Companion
                                .size(18.dp)
                                .clickable { isBalanceHidden = !isBalanceHidden }
                        )
                    }
                    Spacer(modifier = Modifier.Companion.width(15.dp))
                    // 右側：Cash In 膠囊按鈕
                    Box(
                        modifier = Modifier
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(
                                        color = colors.home.balanceCard.cashInBackground,
                                        alpha = 0.6f,
                                        glowRadius = 15.dp,
                                        borderRadius = 8.dp
                                    )
                                else Modifier
                            )
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                            .background(colors.home.balanceCard.cashInBackground)
                            .border(
                                width = 1.5.dp,
                                brush = colors.gradient.goldShimmer ?: SolidColor(colors.home.balanceCard.cashInBorder),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { /* TODO: Cash In */ }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                            Icon(
                                painter = painterResource(id = assets.cashInIcon),
                                contentDescription = stringResource(R.string.balance_cash_in),
                                tint = if (colors.effect.enableGlow) colors.home.balanceCard.cashInText else Color.Companion.Unspecified,
                                modifier = Modifier.Companion.size(18.dp)
                            )
                            Spacer(modifier = Modifier.Companion.width(4.dp))
                            GradientText(
                                text = stringResource(R.string.balance_cash_in),
                                color = colors.home.balanceCard.cashInText,
                                brush = colors.gradient.goldShimmer,
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
                GradientText(
                    text = if (isBalanceHidden) "PHP ••••••" else "PHP ${
                        String.format(
                            "%,.0f",
                            cashBalance
                        )
                    }",
                    color = frostWhite,
                    brush = colors.gradient.goldShimmer,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Companion.ExtraBold
                )

                // Send 膠囊按鈕
                Box(
                    modifier = Modifier.Companion
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    color = colors.home.balanceCard.sendBackground,
                                    alpha = 0.6f,
                                    glowRadius = 15.dp,
                                    borderRadius = 8.dp
                                )
                            else Modifier
                        )
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                        .background(colors.home.balanceCard.sendBackground)
                        .border(
                            width = 1.5.dp,
                            brush = colors.gradient.silverShimmer ?: SolidColor(colors.home.balanceCard.sendBorder),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { /* TODO: Send */ }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                        Icon(
                            painter = painterResource(id = assets.balanceSendIcon),
                            contentDescription = stringResource(R.string.balance_send),
                            tint = Color.Companion.Unspecified,
                            modifier = Modifier.Companion.size(20.dp)
                        )
                        Spacer(modifier = Modifier.Companion.width(4.dp))
                        GradientText(
                            text = stringResource(R.string.balance_send),
                            color = colors.home.balanceCard.sendText,
                            brush = colors.gradient.silverShimmer,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Companion.Bold
                        )
                    }
                }
            }
//            Spacer(Modifier.height(3.dp))
            // ── 第三行：Token 金幣 + 數量 + Balance Switch 按鈕 ──
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                // 左側：金幣圓圈 + Token 數量
                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    Icon(   
                        painter = painterResource(assets.balanceCoinIcon),
                        contentDescription = stringResource(id = R.string.balance_coin),
                        tint = Color.Companion.Unspecified,

                        modifier = Modifier.Companion
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(color = lemonYellow, alpha = 0.7f, glowRadius = 10.dp)
                                else Modifier
                            )
                            .size(50.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(10.dp))
                    GradientText(
                        text = if (isBalanceHidden) "••••" else String.format(
                            "%,.0f",
                            tokenBalance
                        ),
                        color = mistGray,
                        brush = colors.gradient.goldShimmer,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Companion.Bold
                    )
                }

                // 右側：Balance Switch 深色膠囊按鈕
                val switchShape = if (colors.effect.enableGlow)
                    RoundedCornerShape(10.dp)
                else
                    RoundedCornerShape(50.dp)
                Box(
                    modifier = Modifier.Companion
                        .background(
                            color = colors.home.balanceCard.switchBackground,
                            shape = switchShape
                        )
                        .then(
                            if (colors.effect.enableGlow) Modifier
                            else Modifier.border(
                                width = 1.dp,
                                color = colors.home.balanceCard.switchBorder,
                                shape = switchShape
                            )
                        )
                        .clickable { /* TODO: Balance Switch */ }
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                        Icon(
                            painter = painterResource(R.mipmap.ic_switch_balance),
                            tint = colors.home.balanceCard.switchIcon,
                            contentDescription = stringResource(R.string.balance_switch),
                            modifier = Modifier.Companion.size(18.dp)
                        )
                        Spacer(modifier = Modifier.Companion.width(4.dp))
                        Text(
                            text = stringResource(R.string.balance_switch),
                            color = colors.home.balanceCard.switchText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Companion.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun BalanceCardPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(deepNavy)
                .padding(16.dp)
        ) {
            BalanceCard(cashBalance = 12345.0, tokenBalance = 500.0)
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun BalanceCardPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            BalanceCard(cashBalance = 12345.0, tokenBalance = 500.0)
        }
    }
}