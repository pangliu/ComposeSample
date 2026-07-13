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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
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
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.profile.security.dialog.SecurityPinDialog
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.profile.components.FullyVerifiedBadge
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.lemonYellow
import com.qpay.xcash.ui.theme.neonBlueLight
import com.qpay.xcash.ui.theme.neonMint
import com.qpay.xcash.ui.theme.neonPurpleLight

private val CardBackground = Color(0xFF0E1A2E)

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
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(R.mipmap.bg_sub_page),
                    contentScale = ContentScale.FillBounds
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
    Text(
        text = title,
        color = Color.White,
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .neonGlow(colors.accent.primary, alpha = 0.35f, glowRadius = 10.dp, borderRadius = 16.dp)
            .background(CardBackground, RoundedCornerShape(16.dp))
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    listOf(colors.accent.primary.copy(alpha = 0.4f), colors.accent.secondary.copy(alpha = 0.8f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(72.dp),
//                .background(
//                    Brush.radialGradient(
//                        listOf(neonPurple.copy(alpha = 0.5f), neonCyan.copy(alpha = 0.3f))
//                    ),
//                    CircleShape
//                )
//                .border(
//                    width = 1.5.dp,
//                    color = neonCyan,
//                    shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_girl),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(72.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.profile_hi_name, userName),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = stringResource(R.string.profile_xcash_id, xcashId),
                color = colors.text.body,
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
            .neonGlow(colors.accent.secondary, alpha = 0.4f, glowRadius = 10.dp, borderRadius = 16.dp)
            .background(CardBackground, RoundedCornerShape(16.dp))
            .border(1.5.dp, colors.accent.secondary.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
//        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.mipmap.ic_security),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
//                .neonGlow(neonPurple, alpha = 0.6f, glowRadius = 20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.security_score_label),
                color = colors.accent.secondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(R.string.security_score_prefix),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = "$score",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.security_score_max),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.mipmap.ic_gold_coin),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.mipmap.ic_gift),
                contentDescription = null,
                tint = Color.Unspecified,
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
            .neonGlow(neonBlueLight, alpha = 0.3f, glowRadius = 8.dp, borderRadius = 14.dp)
            .background(CardBackground, RoundedCornerShape(14.dp))
            .border(1.5.dp, neonBlueLight.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
    ) {
        // Title row
        Text(
            text = stringResource(R.string.security_checklist_title),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    colors.accent.primary.copy(alpha = 0.08f),
                    RoundedCornerShape(topStart = 13.dp, topEnd = 13.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
        HorizontalDivider(color = colors.accent.primary.copy(alpha = 0.2f), thickness = 0.5.dp)

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
        color = colors.accent.primary.copy(alpha = 0.1f),
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
            modifier = Modifier
                .size(50.dp),
//                .background(neonCyan.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
//                .border(1.dp, neonCyan.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (icon) {
                is ChecklistIcon.Vector -> Icon(
                    imageVector = icon.imageVector,
                    contentDescription = null,
                    tint = colors.accent.primary,
                    modifier = Modifier.size(22.dp)
                )
                is ChecklistIcon.Resource -> Icon(
                    painter = painterResource(icon.resId),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                color = colors.text.body,
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
            tint = Color.White.copy(alpha = 0.4f),
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
                    color = colors.text.body,
                    fontSize = 11.sp
                )
            }
            val isLast = index == steps.lastIndex
            Text(
                text = step,
                color = if (isLast) neonBlueLight else colors.text.body,
                fontSize = 11.sp,
                fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1422)
@Composable
private fun SecurityCenterPreview() {
    MaterialTheme {
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
