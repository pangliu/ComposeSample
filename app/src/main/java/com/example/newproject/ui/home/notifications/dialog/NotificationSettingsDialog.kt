package com.example.newproject.ui.home.notifications.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.newproject.R
import com.example.newproject.ui.components.NeonSwitch
import com.example.newproject.ui.theme.AppTheme
import com.example.newproject.ui.theme.BlackGoldColors
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.NeonColors
import com.example.newproject.ui.theme.neonBlueLight
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonDarkBlue
import com.example.newproject.ui.theme.neonPurpleLight

data class NotificationSettingsState(
    val systemAlerts: Boolean = true,
    val promoNotifications: Boolean = false,
    val transactionAlerts: Boolean = true
)

@Composable
fun NotificationSettingsDialog(
    settings: NotificationSettingsState,
    onSettingsChange: (NotificationSettingsState) -> Unit,
    onDismiss: () -> Unit,
) {
    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() }
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            NotificationSettingsDialogContent(
                settings = settings,
                onSettingsChange = onSettingsChange,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
fun NotificationSettingsDialogContent(
    settings: NotificationSettingsState,
    onSettingsChange: (NotificationSettingsState) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = LocalAppColors.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.bg.surface, RoundedCornerShape(16.dp))
                    .border(
                        1.5.dp,
                        colors.accent.primary.copy(alpha = 0.6f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.notifications_dialog_title),
                    color = colors.text.onPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                NotificationSettingRow(
                    iconRes = R.mipmap.ic_notify_setting_sys,
                    label = stringResource(R.string.notifications_dialog_system_alerts),
                    checked = settings.systemAlerts,
                    onCheckedChange = { onSettingsChange(settings.copy(systemAlerts = it)) },
                    switchColor = neonCyan,
                    iconColor = neonCyan
                )
                Spacer(modifier = Modifier.height(8.dp))
                NotificationSettingRow(
                    iconRes = R.mipmap.ic_notify_setting_gift,
                    label = stringResource(R.string.notifications_dialog_promo_notifications),
                    checked = settings.promoNotifications,
                    onCheckedChange = { onSettingsChange(settings.copy(promoNotifications = it)) },
                    switchColor = neonDarkBlue,
                    iconColor = neonPurpleLight
                )
                Spacer(modifier = Modifier.height(8.dp))
                NotificationSettingRow(
                    iconRes = R.mipmap.ic_notify_setting_alter,
                    label = stringResource(R.string.notifications_dialog_transaction_alerts),
                    checked = settings.transactionAlerts,
                    onCheckedChange = { onSettingsChange(settings.copy(transactionAlerts = it)) },
                    switchColor = neonDarkBlue,
                    iconColor = neonPurpleLight
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(1.5.dp, neonPurpleLight.copy(alpha = 0.6f), CircleShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.notifications_dialog_cancel_desc),
                tint = neonPurpleLight,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun NotificationSettingRow(
    iconRes: Int,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    switchColor: Color = LocalAppColors.current.accent.primary,
    iconColor: Color
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            color = colors.text.onPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        NeonSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            activeColor = switchColor,
            showLabel = true
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun NotificationSettingsDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            NotificationSettingsDialogContent(
                settings = NotificationSettingsState(),
                onSettingsChange = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun NotificationSettingsDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            NotificationSettingsDialogContent(
                settings = NotificationSettingsState(),
                onSettingsChange = {},
                onDismiss = {}
            )
        }
    }
}
