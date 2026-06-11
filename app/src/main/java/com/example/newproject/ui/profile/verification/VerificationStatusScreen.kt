package com.example.newproject.ui.profile.verification

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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.newproject.R
import com.example.newproject.ui.components.SubPageTopBar
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonDarkPurple
import com.example.newproject.ui.theme.neonMint
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.welcomeBackground

private val CardBg = Color(0xFF0E1A2E)
private val neonOrange = Color(0xFFFF8C42)

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
    val status: VerifyStatus
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
    Scaffold(
        containerColor = welcomeBackground,
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
    Text(
        text = text,
        color = Color.White,
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .neonGlow(neonCyan, alpha = 0.35f, glowRadius = 10.dp, borderRadius = 16.dp)
            .background(CardBg, RoundedCornerShape(16.dp))
            .border(
                1.5.dp,
                Brush.linearGradient(
                    listOf(neonCyan.copy(0.4f), neonPurple.copy(0.8f))
                ),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.mipmap.ic_girl),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(72.dp)
        )

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
                color = normalText,
                fontSize = 13.sp
            )
            if (isVerified) {
                FullyVerifiedBadge()
            } else {
                VerificationPendingBadge()
            }
        }
    }
}

@Composable
private fun VerificationPendingBadge(modifier: Modifier = Modifier) {
    val iconSize = 32.dp
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .padding(start = iconSize / 2)
                .border(
                    1.5.dp,
                    Brush.horizontalGradient(listOf(neonDarkPurple.copy(0.7f), neonPurple)),
                    RoundedCornerShape(50)
                )
                .padding(start = iconSize / 2 + 10.dp, end = 12.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.verification_pending_badge),
                color = neonPurple,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(CardBg, androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.mipmap.ic_shield_check),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
private fun FullyVerifiedBadge(modifier: Modifier = Modifier) {
    val iconSize = 32.dp
    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        Row(
            modifier = Modifier
                .padding(start = iconSize / 2)
                .border(
                    1.5.dp,
                    Brush.horizontalGradient(listOf(neonPurple.copy(0.7f), neonCyan.copy(0.9f))),
                    RoundedCornerShape(50)
                )
                .padding(start = iconSize / 2 + 10.dp, end = 12.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.profile_fully_verified),
                color = neonPurple,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(CardBg, androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.mipmap.ic_shield_check),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
private fun VerificationChecklistCard(modifier: Modifier = Modifier) {
    val items = listOf(
        VerifyItem(
            icon = VerifyIcon.Resource(R.mipmap.ic_verify_id),
            title = stringResource(R.string.verification_item_id),
            steps = listOf(
                stringResource(R.string.verification_step_submit_doc),
                stringResource(R.string.verification_step_review_pending)
            ),
            totalSegments = 3,
            completedSegments = 2,
            status = VerifyStatus.PENDING
        ),
        VerifyItem(
            icon = VerifyIcon.Vector(Icons.Outlined.Face),
            title = stringResource(R.string.verification_item_face),
            steps = listOf(
                stringResource(R.string.verification_step_capture),
                stringResource(R.string.verification_step_liveness),
                stringResource(R.string.verification_step_verified)
            ),
            completedSegments = 3,
            status = VerifyStatus.VERIFIED
        ),
        VerifyItem(
            icon = VerifyIcon.Resource(R.mipmap.ic_phone),
            title = stringResource(R.string.verification_item_phone),
            steps = listOf(
                stringResource(R.string.verification_step_otp_sent),
                stringResource(R.string.verification_step_code_entered),
                stringResource(R.string.verification_step_verified)
            ),
            completedSegments = 3,
            status = VerifyStatus.VERIFIED
        ),
        VerifyItem(
            icon = VerifyIcon.Vector(Icons.Outlined.Email),
            title = stringResource(R.string.verification_item_email),
            steps = listOf(
                stringResource(R.string.verification_step_link_sent),
                stringResource(R.string.verification_step_link_clicked),
                stringResource(R.string.verification_step_verified)
            ),
            completedSegments = 3,
            status = VerifyStatus.VERIFIED
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .neonGlow(neonCyan, alpha = 0.2f, glowRadius = 8.dp, borderRadius = 16.dp)
            .background(CardBg, RoundedCornerShape(16.dp))
            .border(
                1.5.dp,
                Brush.linearGradient(listOf(neonCyan.copy(0.3f), neonPurple.copy(0.4f))),
                RoundedCornerShape(16.dp)
            )
    ) {
        items.forEachIndexed { index, item ->
            VerifyItemRow(item = item)
            if (index < items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = neonCyan.copy(0.08f),
                    thickness = 0.5.dp
                )
            }
        }
    }
}

@Composable
private fun VerifyItemRow(item: VerifyItem) {
    val statusColor = when (item.status) {
        VerifyStatus.VERIFIED -> neonMint
        VerifyStatus.PENDING -> neonOrange
        VerifyStatus.ACTION_REQUIRED -> neonOrange
    }
    val barColor = when (item.status) {
        VerifyStatus.VERIFIED -> neonCyan
        VerifyStatus.PENDING -> neonOrange
        VerifyStatus.ACTION_REQUIRED -> neonOrange
    }

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
                    tint = neonCyan,
                    modifier = Modifier.size(32.dp)
                )
                is VerifyIcon.Resource -> Icon(
                    painter = painterResource(icon.resId),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            SegmentedProgressBar(
                totalSegments = item.totalSegments,
                completedSegments = item.completedSegments,
                activeColor = barColor
            )
            Spacer(Modifier.height(4.dp))
            StepsText(steps = item.steps, statusColor = statusColor)
        }
    }
}

@Composable
private fun SegmentedProgressBar(
    totalSegments: Int,
    completedSegments: Int,
    activeColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(totalSegments) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(7.dp)
                    .background(
                        if (index < completedSegments) activeColor else activeColor.copy(0.12f),
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
private fun StepsText(steps: List<String>, statusColor: Color) {
    val annotated = buildAnnotatedString {
        steps.forEachIndexed { idx, step ->
            if (idx > 0) {
                withStyle(SpanStyle(color = normalText)) { append(" > ") }
            }
            if (idx == steps.lastIndex) {
                withStyle(SpanStyle(color = statusColor, fontWeight = FontWeight.Bold)) {
                    append(step)
                }
            } else {
                withStyle(SpanStyle(color = normalText)) { append(step) }
            }
        }
    }
    Text(text = annotated, fontSize = 11.sp)
}

@Composable
private fun ActionRequiredButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .neonGlow(neonOrange, alpha = 0.5f, glowRadius = 12.dp, borderRadius = 12.dp)
            .background(Color.Transparent, RoundedCornerShape(12.dp))
            .border(2.dp, neonOrange, RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {}
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.verification_action_required),
            color = neonOrange,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun VerificationStatusPreview() {
    MaterialTheme {
        VerificationStatusContent()
    }
}
