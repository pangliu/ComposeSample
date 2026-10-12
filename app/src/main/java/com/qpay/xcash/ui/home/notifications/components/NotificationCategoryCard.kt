package com.qpay.xcash.ui.home.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.NotificationType
import com.qpay.xcash.ui.theme.LocalAppColors

private val CardShape = RoundedCornerShape(12.dp)
private const val MAX_BADGE_COUNT = 99

fun NotificationType.categoryIcon(): ImageVector = when (this) {
    NotificationType.SYSTEM -> Icons.Outlined.Shield
    NotificationType.TRANSACTION -> Icons.AutoMirrored.Outlined.CompareArrows
    NotificationType.PROMOTIONS -> Icons.Outlined.Face
    NotificationType.TODO -> Icons.Outlined.Speed
}

fun NotificationType.categoryLabelRes(): Int = when (this) {
    NotificationType.SYSTEM -> R.string.notifications_category_system
    NotificationType.TRANSACTION -> R.string.notifications_category_transaction
    NotificationType.PROMOTIONS -> R.string.notifications_category_promotions
    NotificationType.TODO -> R.string.notifications_category_todo
}

// 通知類別卡片：icon + 類別名稱，右上角顯示數量 badge（0 則不顯示）
@Composable
fun NotificationCategoryCard(
    type: NotificationType,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current.notification.categoryCard
    val label = stringResource(type.categoryLabelRes())

    Box(
        modifier = modifier
            .height(92.dp)
            .clip(CardShape)
            .background(colors.background)
            .then(colors.border?.let { Modifier.border(1.dp, it, CardShape) } ?: Modifier)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = type.categoryIcon(),
                contentDescription = null,
                tint = colors.iconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = label,
                color = colors.labelText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }

        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 6.dp, end = 6.dp)
                    .defaultMinSize(minWidth = 16.dp, minHeight = 16.dp)
                    .clip(CircleShape)
                    .background(colors.badgeBackground)
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (count > MAX_BADGE_COUNT) stringResource(R.string.notifications_badge_overflow)
                    else count.toString(),
                    color = colors.badgeText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
