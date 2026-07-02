package com.example.newproject.ui.home.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.network.model.response.NotificationResponse
import com.example.newproject.network.model.response.NotificationType
import com.example.newproject.ui.theme.AppTheme
import com.example.newproject.ui.theme.BlackGoldColors
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.NeonColors
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPink
import com.example.newproject.ui.theme.neonPurple
import java.util.concurrent.TimeUnit

private val CardBg = Color(0xFF0D1829)

@Composable
fun formatRelativeTime(ts: Long): String {
    val diff = System.currentTimeMillis() - ts
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        minutes < 1 -> stringResource(R.string.notifications_time_just_now)
        minutes < 60 -> stringResource(R.string.notifications_time_minutes_ago, minutes.toInt())
        hours < 24 -> stringResource(R.string.notifications_time_hours_ago, hours.toInt())
        days < 2 -> stringResource(R.string.notifications_time_yesterday)
        days < 7 -> stringResource(R.string.notifications_time_days_ago, days.toInt())
        else -> stringResource(R.string.notifications_time_last_week)
    }
}

private fun iconAndColorFor(type: NotificationType): Pair<Int, Color> = when (type) {
    NotificationType.PROMO -> R.mipmap.ic_notify_gift to neonPink
    NotificationType.SYSTEM -> R.mipmap.ic_notify_security to neonPurple
    NotificationType.ACTIVITY -> R.mipmap.ic_notify_rocket to neonCyan
}

@Composable
fun NotificationCard(notification: NotificationResponse) {
    val colors = LocalAppColors.current
    val (iconRes, accentColor) = iconAndColorFor(notification.type)
    val contentAlpha = if (notification.isRead) 0.55f else 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(contentAlpha)
            .background(CardBg, RoundedCornerShape(12.dp))
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
//        if (!notification.isRead) {
//            Box(
//                modifier = Modifier
//                    .padding(top = 6.dp, end = 6.dp)
//                    .size(7.dp)
//                    .background(accentColor, CircleShape)
//            )
//        } else {
//            Spacer(modifier = Modifier.width(13.dp))
//        }

//        Box(
//            modifier = Modifier
//                .size(40.dp)
//                .background(accentColor.copy(alpha = 0.18f), CircleShape)
//                .border(1.dp, accentColor.copy(alpha = 0.5f), CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                painter = painterResource(id = iconRes),
//                contentDescription = notification.title,
//                tint = Color.Unspecified,
//                modifier = Modifier.size(20.dp)
//            )
//        }
        Icon(
            painter = painterResource(iconRes),
            contentDescription = notification.title,
            tint = Color.Unspecified,
            modifier = Modifier.size(40.dp).align(Alignment.CenterVertically)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = notification.message,
                color = colors.text.body,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
//            Spacer(Modifier.height(6.dp))
//            Text(
//                text = formatRelativeTime(notification.createdAt),
//                color = colors.text.body.copy(alpha = 0.7f),
//                fontSize = 10.sp
//            )
        }
    }
}

private val previewCardNotifications = listOf(
    NotificationResponse(
        id = "1",
        type = NotificationType.PROMO,
        title = "Double Rewards Weekend",
        message = "Earn 2x tokens on every scan & pay transaction this weekend only.",
        isRead = false,
        createdAt = System.currentTimeMillis() - 5 * 60_000
    ),
    NotificationResponse(
        id = "2",
        type = NotificationType.SYSTEM,
        title = "Security Check Passed",
        message = "Your recent login was verified successfully from a new device.",
        isRead = false,
        createdAt = System.currentTimeMillis() - 15 * 60_000
    ),
    NotificationResponse(
        id = "3",
        type = NotificationType.ACTIVITY,
        title = "Payment Sent",
        message = "Your payment of PHP 500.00 to John Cruz was completed successfully.",
        isRead = false,
        createdAt = System.currentTimeMillis() - 32 * 60_000
    ),
    NotificationResponse(
        id = "4",
        type = NotificationType.PROMO,
        title = "Cash In Bonus Unlocked",
        message = "Cash in PHP 1,000 or more today and receive a free reward voucher.",
        isRead = true,
        createdAt = System.currentTimeMillis() - 2 * 60 * 60_000
    ),
    NotificationResponse(
        id = "5",
        type = NotificationType.SYSTEM,
        title = "App Updated",
        message = "We've improved app performance and fixed several minor bugs.",
        isRead = true,
        createdAt = System.currentTimeMillis() - 25 * 60 * 60_000
    ),
    NotificationResponse(
        id = "6",
        type = NotificationType.ACTIVITY,
        title = "Weekly Summary Ready",
        message = "Check out your spending summary and quest progress from last week.",
        isRead = true,
        createdAt = System.currentTimeMillis() - 8 * 24 * 60 * 60_000
    )
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun NotificationCardPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            previewCardNotifications.forEach { notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun NotificationCardPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            previewCardNotifications.forEach { notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}
