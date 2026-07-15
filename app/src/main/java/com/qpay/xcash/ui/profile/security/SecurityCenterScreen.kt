package com.qpay.xcash.ui.profile.security

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.components.gradientTint
import com.qpay.xcash.ui.profile.security.dialog.SecurityPinDialog
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.profile.components.FullyVerifiedBadge
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.neonBlueLight

sealed class ChecklistIcon {
    data class Vector(val imageVector: ImageVector) : ChecklistIcon()
    data class Resource(@DrawableRes val resId: Int) : ChecklistIcon()
}

@Composable
fun SecurityCenterScreen(
    viewModel: SecurityCenterViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    SecurityCenterContent(uiState = uiState, onBack = onBack)
}

@Composable
fun SecurityCenterContent(
    uiState: SecurityCenterUiState = SecurityCenterUiState(),
    onBack: () -> Unit = {}
) {
    var showPinDialog by remember { mutableStateOf(false) }

    if (showPinDialog) {
        SecurityPinDialog(onDismiss = { showPinDialog = false })
    }

    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    assets.subPageBackground?.let {
                        Modifier.paint(
                            painter = painterResource(it),
                            contentScale = ContentScale.FillBounds
                        )
                    } ?: Modifier
                )
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            SubPageTopBar(
                title = stringResource(R.string.profile_security_center),
                onBack = onBack
            )

            // ── Identity & Status ─────────────────────────────────────────
            SectionHeader(
                title = stringResource(R.string.security_section_identity),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))
            IdentityCard(
                userName = uiState.userName.ifEmpty { "Bruce Banner" },
                xcashId = uiState.xcashId.ifEmpty { "0917-123-4567" },
                isVerified = uiState.isVerified,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // ── Security Score ────────────────────────────────────────────
            SecurityScoreCard(
                score = uiState.securityScore,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(20.dp))

            // ── Security Checklist ────────────────────────────────────────
            ChecklistCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                onPinClick = { showPinDialog = true }
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current
    GradientText(
        text = title,
        color = colors.security.sectionHeaderText,
        brush = colors.gradient.goldShimmer,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = modifier
    )
}

@Composable
private fun IdentityCard(
    userName: String,
    xcashId: String,
    isVerified: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow) {
                    Modifier.neonGlow(colors.accent.primary, alpha = 0.35f, glowRadius = 10.dp, borderRadius = 16.dp)
                } else Modifier
            )
            .background(colors.security.cardBackground, RoundedCornerShape(16.dp))
            .border(1.5.dp, colors.security.identityCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(72.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(assets.securityAvatar),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(72.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.profile_hi_name, userName),
                color = colors.security.userNameText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = stringResource(R.string.profile_xcash_id, xcashId),
                color = colors.security.xcashIdText,
                fontSize = 13.sp
            )
            if (isVerified) {
                FullyVerifiedBadge(text = stringResource(R.string.profile_fully_verified))
            }
        }
    }
}

@Composable
private fun SecurityScoreCard(score: Int, modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow) {
                    Modifier.neonGlow(colors.accent.secondary, alpha = 0.4f, glowRadius = 10.dp, borderRadius = 16.dp)
                } else Modifier
            )
            .background(colors.security.cardBackground, RoundedCornerShape(16.dp))
            .border(1.5.dp, colors.security.scoreCardBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Icon(
            painter = painterResource(R.mipmap.ic_security),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
                .gradientTint(colors.security.scoreIconGradient)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            GradientText(
                text = stringResource(R.string.security_score_label),
                color = colors.security.scoreLabelText,
                brush = colors.security.scoreLabelGradient,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(R.string.security_score_prefix),
                    color = colors.security.scoreValueText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = "$score",
                    color = colors.security.scoreValueText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.security_score_max),
                    color = colors.security.scoreValueText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row {
            Icon(
                painter = painterResource(R.mipmap.ic_gold_coin),
                contentDescription = null,
                tint = colors.security.scoreTrailingIconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.mipmap.ic_gift),
                contentDescription = null,
                tint = colors.security.scoreTrailingIconTint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ChecklistCard(modifier: Modifier = Modifier, onPinClick: () -> Unit = {}) {
    val colors = LocalAppColors.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow) {
                    Modifier.neonGlow(neonBlueLight, alpha = 0.3f, glowRadius = 8.dp, borderRadius = 14.dp)
                } else Modifier
            )
            .background(colors.security.cardBackground, RoundedCornerShape(14.dp))
            .border(1.5.dp, colors.security.checklistCardBorder, RoundedCornerShape(14.dp))
    ) {
        // Title row
        GradientText(
            text = stringResource(R.string.security_checklist_title),
            color = colors.security.checklistTitleText,
            brush = colors.security.checklistTitleGradient,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    colors.security.checklistTitleBackground,
                    RoundedCornerShape(topStart = 13.dp, topEnd = 13.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
        HorizontalDivider(color = colors.security.checklistHeaderDivider, thickness = 0.5.dp)

        ChecklistItem(
            icon = ChecklistIcon.Resource(R.mipmap.ic_lock),
            title = stringResource(R.string.security_item_pin),
            subtitle = stringResource(R.string.security_item_pin_subtitle),
            statusSteps = listOf("Set", "Confirmed", "Active"),
            onClick = onPinClick
        )
        ChecklistDivider()
        ChecklistItem(
            icon = ChecklistIcon.Resource(R.mipmap.ic_print),
            title = stringResource(R.string.security_item_biometric),
            subtitle = stringResource(R.string.security_item_biometric_subtitle),
            statusSteps = listOf("Setup", "Enabled", "Active")
        )
        ChecklistDivider()
        ChecklistItem(
            icon = ChecklistIcon.Resource(R.mipmap.ic_pc),
            title = stringResource(R.string.security_item_login_activity),
            subtitle = stringResource(R.string.security_item_login_activity_subtitle)
        )
        ChecklistDivider()
        ChecklistItem(
            icon = ChecklistIcon.Resource(R.mipmap.ic_gps),
            title = stringResource(R.string.security_item_location),
            subtitle = stringResource(R.string.security_item_location_subtitle)
        )
        ChecklistDivider()
        ChecklistItem(
            icon = ChecklistIcon.Resource(R.mipmap.ic_phone),
            title = stringResource(R.string.security_item_alerts),
            subtitle = stringResource(R.string.security_item_alerts_subtitle),
            statusSteps = listOf("Enable", "Setup", "Active")
        )
    }
}

@Composable
private fun ChecklistDivider() {
    val colors = LocalAppColors.current
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = colors.security.checklistDivider,
        thickness = 0.5.dp
    )
}

@Composable
private fun ChecklistItem(
    icon: ChecklistIcon,
    title: String,
    subtitle: String,
    statusSteps: List<String>? = null,
    onClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon container
        Box(
            modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            when (icon) {
                is ChecklistIcon.Vector -> Icon(
                    imageVector = icon.imageVector,
                    contentDescription = null,
                    tint = colors.accent.primary,
                    modifier = Modifier
                        .size(22.dp)
                        .gradientTint(colors.security.itemIconGradient)
                )
                is ChecklistIcon.Resource -> Icon(
                    painter = painterResource(icon.resId),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.gradientTint(colors.security.itemIconGradient)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = colors.security.itemTitleText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                color = colors.security.itemSubtitleText,
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (statusSteps != null) {
                Spacer(Modifier.height(3.dp))
                StatusStepsRow(steps = statusSteps)
            }
        }

        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = null,
            tint = colors.security.chevron,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun StatusStepsRow(steps: List<String>) {
    val colors = LocalAppColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        steps.forEachIndexed { index, step ->
            if (index > 0) {
                Text(
                    text = " > ",
                    color = colors.security.statusStepText,
                    fontSize = 11.sp
                )
            }
            val isLast = index == steps.lastIndex
            Text(
                text = step,
                color = if (isLast) colors.security.statusStepActiveText else colors.security.statusStepText,
                fontSize = 11.sp,
                fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun SecurityCenterPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SecurityCenterContent(
            uiState = SecurityCenterUiState(
                userName = "Bruce Banner",
                xcashId = "0917-123-4567",
                isVerified = true,
                securityScore = 95
            )
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SecurityCenterPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        SecurityCenterContent(
            uiState = SecurityCenterUiState(
                userName = "Bruce Banner",
                xcashId = "0917-123-4567",
                isVerified = true,
                securityScore = 95
            )
        )
    }
}
