package com.qpay.xcash.ui.home.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.theme.LocalAppColors

private val BannerShape = RoundedCornerShape(12.dp)
private val ButtonShape = RoundedCornerShape(50)

// 通知頁底部廣告 Banner：左側插圖 + Limited Offer / Ad Banner 文字 + Claim Free 按鈕
@Composable
fun NotificationAdBanner(
    onClaimClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current.notification.banner

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(BannerShape)
            .background(colors.background)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: 設計稿為卡片插圖，待提供圖檔後替換
        Icon(
            imageVector = Icons.Filled.CreditCard,
            contentDescription = null,
            tint = colors.illustrationTint,
            modifier = Modifier.size(40.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.notifications_banner_sub_title),
                color = colors.subText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.notifications_banner_title),
                color = colors.titleText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .clip(ButtonShape)
                .background(colors.buttonBackground)
                .border(1.dp, colors.buttonBorder, ButtonShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClaimClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.notifications_banner_claim),
                color = colors.buttonText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
