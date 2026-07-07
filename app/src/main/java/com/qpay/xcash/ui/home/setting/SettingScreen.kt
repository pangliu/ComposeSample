package com.qpay.xcash.ui.home.setting

import com.qpay.xcash.ui.Routes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.NeonSwitch
import com.qpay.xcash.ui.components.RowIcon
import com.qpay.xcash.ui.components.RowIconImage
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.home.dialog.DeleteAccountDialog
import com.qpay.xcash.ui.home.dialog.LogoutDialog
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.ThemeMode
import com.qpay.xcash.ui.theme.neonCyan
import com.qpay.xcash.ui.theme.neonRed

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    currentTheme: ThemeMode = ThemeMode.BLACK_GOLD,
    onThemeChange: (ThemeMode) -> Unit = {},
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    SettingScreenContent(
        userEmail = uiState.userEmail,
        currentTheme = currentTheme,
        onThemeChange = onThemeChange,
        onBack = onBack,
        onNavigate = onNavigate,
        onLogout = { viewModel.logout() },
        onDeleteAccount = { viewModel.deleteAccount() }
    )
}

@Composable
private fun SettingScreenContent(
    userEmail: String = "",
    currentTheme: ThemeMode = ThemeMode.BLACK_GOLD,
    onThemeChange: (ThemeMode) -> Unit = {},
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    var systemAlertsOn by remember { mutableStateOf(true) }
    var promoNoteOn by remember { mutableStateOf(false) }
    var txAlertsOn by remember { mutableStateOf(true) }
    var hideBalanceOn by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showThemeMenu by remember { mutableStateOf(false) }

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
        contentColor = colors.text.onPrimary
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
                    titleColor = colors.setting.appearanceSection.title,
                    borderColor = colors.setting.appearanceSection.border
                ) {
                    DropdownRow(
                        icon = RowIcon.Vector(Icons.Outlined.Palette),
                        label = stringResource(R.string.setting_app_theme),
                        value = stringResource(currentTheme.labelRes),
                        iconColor = colors.setting.themeDropdown.icon,
                        chipColor = colors.setting.themeDropdown.menuButton,
                        onClick = { showThemeMenu = true },
                        menuContent = {
                            DropdownMenu(
                                offset = DpOffset(x = 0.dp, y = 3.dp),
                                expanded = showThemeMenu,
                                onDismissRequest = { showThemeMenu = false },
                                containerColor = colors.bg.surface,
                                shape = RoundedCornerShape(5.dp),
                                border = BorderStroke(1.dp, colors.accent.primary.copy(0.6f))
                            ) {
                                DropdownMenuItem(
                                    modifier = Modifier
                                        .heightIn(max = 36.dp)
                                        .padding(horizontal = 5.dp)
                                        .background(
                                            color = if (currentTheme == ThemeMode.NEON)
                                                colors.accent.primary.copy(0.2f)
                                            else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp),
                                        ),
                                    text = {
                                        Text(
                                            text = stringResource(R.string.setting_theme_neon_cyber),
                                            fontSize = 14.sp,
                                            color = Color.White,
                                        )
                                    },
                                    onClick = {
                                        showThemeMenu = false
                                        onThemeChange(ThemeMode.NEON)
                                    }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier
                                        .heightIn(max = 36.dp)
                                        .padding(horizontal = 5.dp)
                                        .background(
                                            color = if (currentTheme == ThemeMode.BLACK_GOLD)
                                                colors.accent.primary.copy(0.2f)
                                            else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp),
                                        ),
                                    text = {
                                        Text(
                                            text = stringResource(R.string.setting_theme_black_gold),
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                    },
                                    onClick = {
                                        showThemeMenu = false
                                        onThemeChange(ThemeMode.BLACK_GOLD)
                                    }
                                )
                            }
                        }
                    )
                    SectionDivider(colors.setting.appearanceSection.border)
                    ToggleRow(
                        icon = RowIcon.Vector(Icons.Outlined.VisibilityOff),
                        label = stringResource(R.string.setting_hide_balance),
                        checked = hideBalanceOn,
                        onCheckedChange = { hideBalanceOn = it },
                        iconColor = colors.setting.hideBalanceToggle.icon,
                        switchColor = colors.setting.hideBalanceToggle.switch
                    )
                    SectionDivider(colors.setting.appearanceSection.border)
                    DropdownRow(
                        icon = RowIcon.Vector(Icons.Outlined.Language),
                        label = stringResource(R.string.setting_language),
                        value = stringResource(R.string.setting_language_english),
                        iconColor = colors.setting.languageDropdown.icon,
                        chipColor = colors.setting.languageDropdown.menuButton
                    )
                }

                // ── Notifications ─────────────────────────────────────────
                SectionCard(
                    title = stringResource(R.string.setting_section_notifications),
                    titleColor = colors.setting.notificationsSection.title,
                    borderColor = colors.setting.notificationsSection.border
                ) {
                    ToggleRow(
                        icon = RowIcon.Vector(Icons.Outlined.NotificationsNone),
                        label = stringResource(R.string.setting_system_alerts),
                        checked = systemAlertsOn,
                        onCheckedChange = { systemAlertsOn = it },
                        iconColor = colors.setting.systemAlertsToggle.icon,
                        switchColor = colors.setting.systemAlertsToggle.switch
                    )
                    SectionDivider(colors.setting.notificationsSection.border)
                    ToggleRow(
                        icon = RowIcon.Vector(Icons.Outlined.CardGiftcard),
                        label = stringResource(R.string.setting_promo_notifications),
                        checked = promoNoteOn,
                        onCheckedChange = { promoNoteOn = it },
                        iconColor = colors.setting.promoNotificationsToggle.icon,
                        switchColor = colors.setting.promoNotificationsToggle.switch
                    )
                    SectionDivider(colors.setting.notificationsSection.border)
                    ToggleRow(
                        icon = RowIcon.Vector(Icons.AutoMirrored.Outlined.List),
                        label = stringResource(R.string.setting_transaction_alerts),
                        checked = txAlertsOn,
                        onCheckedChange = { txAlertsOn = it },
                        iconColor = colors.setting.transactionAlertsToggle.icon,
                        switchColor = colors.setting.transactionAlertsToggle.switch
                    )
                }

                // ── App Info & Support ────────────────────────────────────
                SectionCard(
                    title = stringResource(R.string.setting_section_app_info),
                    titleColor = colors.setting.appInfoSection.title,
                    borderColor = colors.setting.appInfoSection.border
                ) {
                    NavRow(
                        icon = RowIcon.Vector(Icons.Outlined.Description),
                        label = stringResource(R.string.setting_update_log),
                        iconTint = colors.setting.updateLogNav.icon,
                        onClick = { onNavigate(Routes.UPDATE_LOG) }
                    )
                    SectionDivider(colors.setting.appInfoSection.border)
                    NavRow(
                        icon = RowIcon.Vector(Icons.Outlined.Storage),
                        label = stringResource(R.string.setting_clear_cache),
                        subtitle = stringResource(R.string.setting_cache_size),
                        iconTint = colors.setting.clearCacheNav.icon
                    )
                }

                // ── Account ───────────────────────────────────────────────
                SectionCard(
                    title = stringResource(R.string.setting_section_account),
                    titleColor = colors.setting.accountSection.title,
                    borderColor = colors.setting.accountSection.border
                ) {
                    NavRow(
                        icon = RowIcon.Vector(Icons.Outlined.Delete),
                        label = stringResource(R.string.setting_delete_account),
                        iconTint = colors.setting.deleteAccountNav.icon,
//                        labelColor = neonRed,
                        onClick = { showDeleteDialog = true }
                    )
                    SectionDivider(colors.setting.accountSection.border)
                    NavRow(
                        icon = RowIcon.Vector(Icons.AutoMirrored.Outlined.ExitToApp),
                        label = stringResource(R.string.setting_logout),
                        iconTint = colors.setting.logoutNav.icon,
//                        labelColor = neonRed,
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
    titleColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalAppColors.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(borderColor, alpha = 0.25f, glowRadius = 8.dp, borderRadius = 14.dp)
                else Modifier
            )
            .background(
                if (colors.effect.enableGlow) colors.bg.surface else Color.Transparent,
                RoundedCornerShape(14.dp)
            )
            .border(1.5.dp, borderColor.copy(0.5f), RoundedCornerShape(14.dp))
            .padding(top = 12.dp, bottom = 5.dp)
    ) {
        GradientText(
            text = title,
            color = titleColor,
            brush = colors.gradient.goldShimmer,
            fontSize = 16.sp,
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

private val ThemeMode.labelRes: Int
    get() = when (this) {
        ThemeMode.NEON -> R.string.setting_theme_neon_cyber
        ThemeMode.BLACK_GOLD -> R.string.setting_theme_black_gold
    }

// ── Row variants ──────────────────────────────────────────────────────────────

@Composable
private fun DropdownRow(
    icon: RowIcon,
    label: String,
    value: String,
    iconColor: Color,
    chipColor: Color,
    onClick: () -> Unit = {},
    menuContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIconImage(icon = icon, tint = iconColor)
        Spacer(Modifier.width(12.dp))
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Box {
            Row(
                modifier = Modifier
                    .background(
                        color = chipColor.copy(0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = chipColor.copy(0.6f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(value, color = chipColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = chipColor,
                    modifier = Modifier.size(14.dp)
                )
            }
            menuContent()
        }
    }
}

@Composable
private fun ToggleRow(
    icon: RowIcon,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconColor: Color = neonCyan,
    switchColor: Color = neonCyan
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIconImage(icon = icon, tint = iconColor)
        Spacer(Modifier.width(12.dp))
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        NeonSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            activeColor = switchColor,
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
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
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

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun SettingScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SettingScreenContent(currentTheme = ThemeMode.NEON)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SettingScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        SettingScreenContent(currentTheme = ThemeMode.BLACK_GOLD)
    }
}
