package com.example.newproject.ui.home.setting

import com.example.newproject.ui.Routes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newproject.R
import com.example.newproject.ui.components.NeonSwitch
import com.example.newproject.ui.components.RowIcon
import com.example.newproject.ui.components.RowIconImage
import com.example.newproject.ui.components.SubPageTopBar
import com.example.newproject.ui.home.dialog.DeleteAccountDialog
import com.example.newproject.ui.home.dialog.LogoutDialog
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonDarkBlue
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.neonRed

private val CardBg = Color(0xFF0E1A2E)

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    SettingScreenContent(
        userEmail = uiState.userEmail,
        onBack = onBack,
        onNavigate = onNavigate,
        onLogout = { viewModel.logout() },
        onDeleteAccount = { viewModel.deleteAccount() }
    )
}

@Composable
private fun SettingScreenContent(
    userEmail: String = "",
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    var systemAlertsOn  by remember { mutableStateOf(true) }
    var promoNoteOn     by remember { mutableStateOf(false) }
    var txAlertsOn      by remember { mutableStateOf(true) }
    var hideBalanceOn   by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    if (showDeleteDialog) {
        DeleteAccountDialog(
            userEmail = userEmail,
            onConfirm = {
                showDeleteDialog = false
                onDeleteAccount()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    val colors = LocalAppColors.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SubPageTopBar(title = stringResource(R.string.setting_title), onBack = onBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Appearance & Display ──────────────────────────────────
                SectionCard(
                    title = stringResource(R.string.setting_section_appearance),
                    borderColor = colors.accent.primary
                ) {
                    DropdownRow(
                        icon = RowIcon.Vector(Icons.Outlined.Palette),
                        label = stringResource(R.string.setting_app_theme),
                        value = stringResource(R.string.setting_theme_neon_cyber),
                        borderColor = colors.accent.primary
                    )
                    SectionDivider(colors.accent.primary)
                    ToggleRow(
                        icon = RowIcon.Vector(Icons.Outlined.VisibilityOff),
                        label = stringResource(R.string.setting_hide_balance),
                        checked = hideBalanceOn,
                        onCheckedChange = { hideBalanceOn = it },
                        activeColor = neonPurpleLight
                    )
                    SectionDivider(colors.accent.primary)
                    DropdownRow(
                        icon = RowIcon.Vector(Icons.Outlined.Language),
                        label = stringResource(R.string.setting_language),
                        value = stringResource(R.string.setting_language_english),
                        borderColor = neonPurpleLight
                    )
                }

                // ── Notifications ─────────────────────────────────────────
                SectionCard(
                    title = stringResource(R.string.setting_section_notifications),
                    borderColor = neonDarkBlue
                ) {
                    ToggleRow(
                        icon = RowIcon.Vector(Icons.Outlined.NotificationsNone),
                        label = stringResource(R.string.setting_system_alerts),
                        checked = systemAlertsOn,
                        onCheckedChange = { systemAlertsOn = it },
                        activeColor = colors.accent.primary
                    )
                    SectionDivider(colors.accent.secondary)
                    ToggleRow(
                        icon = RowIcon.Vector(Icons.Outlined.CardGiftcard),
                        label = stringResource(R.string.setting_promo_notifications),
                        checked = promoNoteOn,
                        onCheckedChange = { promoNoteOn = it },
                        activeColor = neonDarkBlue
                    )
                    SectionDivider(colors.accent.secondary)
                    ToggleRow(
                        icon = RowIcon.Vector(Icons.AutoMirrored.Outlined.List),
                        label = stringResource(R.string.setting_transaction_alerts),
                        checked = txAlertsOn,
                        onCheckedChange = { txAlertsOn = it },
                        activeColor = neonDarkBlue
                    )
                }

                // ── App Info & Support ────────────────────────────────────
                SectionCard(
                    title = stringResource(R.string.setting_section_app_info),
                    borderColor = colors.accent.primary
                ) {
                    NavRow(
                        icon = RowIcon.Vector(Icons.Outlined.Description),
                        label = stringResource(R.string.setting_update_log),
                        iconTint = colors.accent.primary,
                        onClick = { onNavigate(Routes.UPDATE_LOG) }
                    )
                    SectionDivider(colors.accent.primary)
                    NavRow(
                        icon = RowIcon.Vector(Icons.Outlined.Storage),
                        label = stringResource(R.string.setting_clear_cache),
                        subtitle = stringResource(R.string.setting_cache_size),
                        iconTint = colors.accent.primary
                    )
                }

                // ── Account ───────────────────────────────────────────────
                SectionCard(
                    title = stringResource(R.string.setting_section_account),
                    borderColor = neonRed
                ) {
                    NavRow(
                        icon = RowIcon.Vector(Icons.Outlined.Delete),
                        label = stringResource(R.string.setting_delete_account),
                        iconTint = neonRed,
                        labelColor = neonRed,
                        onClick = { showDeleteDialog = true }
                    )
                    SectionDivider(neonRed)
                    NavRow(
                        icon = RowIcon.Vector(Icons.AutoMirrored.Outlined.ExitToApp),
                        label = stringResource(R.string.setting_logout),
                        iconTint = neonRed,
                        labelColor = neonRed,
                        onClick = { showLogoutDialog = true }
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ── Section Card ──────────────────────────────────────────────────────────────

@Composable
private fun SectionCard(
    title: String,
    borderColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .neonGlow(borderColor, alpha = 0.25f, glowRadius = 8.dp, borderRadius = 14.dp)
            .background(CardBg, RoundedCornerShape(14.dp))
            .border(1.5.dp, borderColor.copy(0.5f), RoundedCornerShape(14.dp))
            .padding(top = 12.dp, bottom = 5.dp)
    ) {
        Text(
            text = title,
            color = borderColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        content()
    }
}

@Composable
private fun SectionDivider(borderColor: Color) {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = borderColor.copy(0.15f),
        thickness = 0.5.dp
    )
}

// ── Row variants ──────────────────────────────────────────────────────────────

@Composable
private fun DropdownRow(
    icon: RowIcon,
    label: String,
    value: String,
    borderColor: Color,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIconImage(icon = icon, tint = borderColor)
        Spacer(Modifier.width(12.dp))
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .background(color = borderColor.copy(0.2f))
                .border(1.dp, borderColor.copy(0.6f), RoundedCornerShape(5.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(value, color = borderColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = borderColor,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun ToggleRow(
    icon: RowIcon,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = neonCyan
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIconImage(icon = icon, tint = activeColor)
        Spacer(Modifier.width(12.dp))
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        NeonSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            activeColor = activeColor,
            showLabel = true
        )
    }
}

@Composable
private fun NavRow(
    icon: RowIcon,
    label: String,
    subtitle: String? = null,
    iconTint: Color = neonCyan,
    labelColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIconImage(icon = icon, tint = iconTint)
        Spacer(Modifier.width(12.dp))
        Text(label, color = labelColor, fontSize = 14.sp, modifier = Modifier.weight(1f))
        if (subtitle != null) {
            Text(subtitle, color = colors.text.body, fontSize = 13.sp)
            Spacer(Modifier.width(4.dp))
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = labelColor.copy(0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun SettingScreenPreview() {
    MaterialTheme {
        SettingScreenContent()
    }
}
