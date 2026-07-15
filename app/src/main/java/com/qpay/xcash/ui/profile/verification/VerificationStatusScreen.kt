package com.qpay.xcash.ui.profile.verification

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.profile.components.FullyVerifiedBadge
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

private sealed class VerifyIcon {
    data class Vector(val imageVector: ImageVector) : VerifyIcon()
    data class Resource(@DrawableRes val resId: Int) : VerifyIcon()
}

private enum class VerifyStatus { VERIFIED, PENDING, ACTION_REQUIRED }

private data class VerifyItem(
    val icon: VerifyIcon,
    val title: String,
    val steps: List<String>,
    val totalSegments: Int = 3,
    val completedSegments: Int,
    val status: VerifyStatus,
    val iconTint: Color = Color.Unspecified,
    val barBorder: Brush,
    val barFirstFill: Brush,
    val barFill: Color,
    val statusColor: Color
)

@Composable
fun VerificationStatusScreen(
    viewModel: VerificationStatusViewModel,
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    VerificationStatusContent(uiState = uiState, onBack = onBack)
}

@Composable
private fun VerificationStatusContent(
    uiState: VerificationStatusUiState = VerificationStatusUiState(),
    onBack: () -> Unit = {}
) {
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
            SubPageTopBar(
                title = stringResource(R.string.verification_title),
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 32.dp)
            ) {
                SectionHeader(
                    text = stringResource(R.string.profile_section_identity),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(8.dp))
                IdentityCard(
                    userName = uiState.userName.ifEmpty { "Bruce Banner" },
                    xcashId = uiState.xcashId.ifEmpty { "0917-123-4567" },
                    isVerified = uiState.isVerified,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(20.dp))

                SectionHeader(
                    text = stringResource(R.string.verification_checklist_title),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    letterSpacing = true
                )
                Spacer(Modifier.height(10.dp))
                VerificationChecklistCard(modifier = Modifier.padding(horizontal = 16.dp))

                Spacer(Modifier.height(20.dp))

                ActionRequiredButton(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
    letterSpacing: Boolean = false
) {
    val colors = LocalAppColors.current
    GradientText(
        text = text,
        color = colors.verification.sectionHeaderText,
        brush = colors.gradient.goldShimmer,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = if (letterSpacing) 0.5.sp else 0.sp,
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
            .background(colors.verification.cardBackground, RoundedCornerShape(16.dp))
            .border(1.5.dp, colors.verification.identityCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(assets.verificationAvatar),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(72.dp)
        )

        Spacer(Modifier.width(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.profile_hi_name, userName),
                color = colors.verification.userNameText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = stringResource(R.string.profile_xcash_id, xcashId),
                color = colors.verification.xcashIdText,
                fontSize = 13.sp
            )
            if (isVerified) {
                FullyVerifiedBadge(text = stringResource(R.string.profile_fully_verified))
            } else {
                FullyVerifiedBadge(text = stringResource(R.string.verification_pending_badge))
            }
        }
    }
}

@Composable
private fun VerificationChecklistCard(modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current
    val verifiedColor = colors.verification.verifiedItem
    val pendingColor = colors.verification.pendingItem
    val items = listOf(
        VerifyItem(
            icon = VerifyIcon.Resource(R.mipmap.ic_passport),
            title = stringResource(R.string.verification_item_id),
            steps = listOf(
                stringResource(R.string.verification_step_submit_doc),
                stringResource(R.string.verification_step_review_pending)
            ),
            totalSegments = 3,
            completedSegments = 2,
            status = VerifyStatus.PENDING,
            iconTint = pendingColor,
            barBorder = colors.verification.pendingBarBorder,
            barFirstFill = colors.verification.pendingBarFirstFill,
            barFill = pendingColor,
            statusColor = pendingColor
        ),
        VerifyItem(
            icon = VerifyIcon.Resource(R.mipmap.ic_scan_passport),
            title = stringResource(R.string.verification_item_face),
            steps = listOf(
                stringResource(R.string.verification_step_capture),
                stringResource(R.string.verification_step_liveness),
                stringResource(R.string.verification_step_verified)
            ),
            completedSegments = 3,
            status = VerifyStatus.VERIFIED,
            iconTint = verifiedColor,
            barBorder = colors.verification.verifiedBarBorder,
            barFirstFill = colors.verification.verifiedBarFirstFill,
            barFill = verifiedColor,
            statusColor = verifiedColor
        ),
        VerifyItem(
            icon = VerifyIcon.Resource(R.mipmap.ic_profile_edit_phone),
            title = stringResource(R.string.verification_item_phone),
            steps = listOf(
                stringResource(R.string.verification_step_otp_sent),
                stringResource(R.string.verification_step_code_entered),
                stringResource(R.string.verification_step_verified)
            ),
            completedSegments = 3,
            status = VerifyStatus.VERIFIED,
            iconTint = verifiedColor,
            barBorder = colors.verification.verifiedBarBorder,
            barFirstFill = colors.verification.verifiedBarFirstFill,
            barFill = verifiedColor,
            statusColor = verifiedColor
        ),
        VerifyItem(
            icon = VerifyIcon.Resource(R.mipmap.ic_profile_edit_mail),
            title = stringResource(R.string.verification_item_email),
            steps = listOf(
                stringResource(R.string.verification_step_link_sent),
                stringResource(R.string.verification_step_link_clicked),
                stringResource(R.string.verification_step_verified)
            ),
            completedSegments = 3,
            status = VerifyStatus.VERIFIED,
            iconTint = verifiedColor,
            barBorder = colors.verification.verifiedBarBorder,
            barFirstFill = colors.verification.verifiedBarFirstFill,
            barFill = verifiedColor,
            statusColor = verifiedColor,
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow) {
                    Modifier.neonGlow(colors.accent.primary, alpha = 0.2f, glowRadius = 8.dp, borderRadius = 16.dp)
                } else Modifier
            )
            .background(colors.verification.cardBackground, RoundedCornerShape(16.dp))
            .border(1.5.dp, colors.verification.checklistCardBorder, RoundedCornerShape(16.dp))
    ) {
        items.forEachIndexed { index, item ->
            VerifyItemRow(item = item)
            if (index < items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = colors.verification.checklistDivider,
                    thickness = 0.5.dp
                )
            }
        }
    }
}

@Composable
private fun VerifyItemRow(item: VerifyItem) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val icon = item.icon) {
                is VerifyIcon.Vector -> Icon(
                    imageVector = icon.imageVector,
                    contentDescription = null,
                    tint = item.iconTint.takeIf { it != Color.Unspecified } ?: colors.accent.primary,
                    modifier = Modifier.size(32.dp)
                )
                is VerifyIcon.Resource -> Icon(
                    painter = painterResource(icon.resId),
                    contentDescription = null,
                    tint = item.iconTint,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = item.statusColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            SegmentedProgressBar(
                totalSegments = item.totalSegments,
                completedSegments = item.completedSegments,
                borderBrush = item.barBorder,
                firstFillBrush = item.barFirstFill,
                fillColor = item.barFill
            )
            Spacer(Modifier.height(4.dp))
            StepsText(steps = item.steps, statusColor = item.statusColor)
        }
    }
}

@Composable
private fun SegmentedProgressBar(
    totalSegments: Int,
    completedSegments: Int,
    borderBrush: Brush,
    firstFillBrush: Brush,
    fillColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(18.dp)
            .border(1.5.dp, borderBrush, RoundedCornerShape(50))
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            repeat(totalSegments) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            brush = when {
                                index >= completedSegments -> SolidColor(Color.Transparent)
                                index == 0 -> firstFillBrush
                                else -> SolidColor(fillColor)
                            },
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

@Composable
private fun StepsText(steps: List<String>, statusColor: Color) {
    val annotated = buildAnnotatedString {
        steps.forEachIndexed { idx, step ->
            if (idx > 0) {
                withStyle(SpanStyle(color = statusColor)) { append(" > ") }
            }
            if (idx == steps.lastIndex) {
                withStyle(SpanStyle(color = statusColor, fontWeight = FontWeight.Bold)) {
                    append(step)
                }
            } else {
                withStyle(SpanStyle(color = statusColor.copy(0.7f))) { append(step) }
            }
        }
    }
    Text(text = annotated, fontSize = 11.sp)
}

@Composable
private fun ActionRequiredButton(modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.verification.actionButtonFill, RoundedCornerShape(12.dp))
            .border(2.dp, colors.verification.actionButtonBorder, RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {}
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.verification_action_required),
            color = colors.verification.actionButtonText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun VerificationStatusPreviewNeon() {
    AppTheme(colors = NeonColors) {
        VerificationStatusContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun VerificationStatusPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        VerificationStatusContent()
    }
}
