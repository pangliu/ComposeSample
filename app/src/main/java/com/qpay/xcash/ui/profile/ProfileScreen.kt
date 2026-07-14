package com.qpay.xcash.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.profile.components.FullyVerifiedBadge
import com.qpay.xcash.ui.profile.components.LogoutConfirmDialog
import com.qpay.xcash.ui.profile.dialog.InviteFriendsDialog
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

sealed class ProfileIcon {
    data class Vector(val imageVector: ImageVector) : ProfileIcon()
    data class Resource(@DrawableRes val resId: Int) : ProfileIcon()
}

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                    .show()

                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    LoadingDialog(isShowing = uiState.isLoggingOut)

    ProfileScreenContent(
        uiState = uiState,
        onLogout = { viewModel.logout() },
        onNavigate = onNavigate
    )
}

@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState = ProfileUiState(),
    onLogout: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val colors = LocalAppColors.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showInviteDialog by remember { mutableStateOf(false) }

    if (showInviteDialog) {
        InviteFriendsDialog(onDismiss = { showInviteDialog = false })
    }

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg.page)
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        GradientText(
            text = stringResource(R.string.profile_title),
            color = Color.White,
            brush = colors.gradient.goldShimmer,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 0.dp),
            textAlign = TextAlign.Center
        )

        ProfileSectionHeader(stringResource(R.string.profile_section_identity))
        IdentityCard(uiState, onClick = { onNavigate(Routes.VERIFICATION_STATUS) })

        ProfileSectionHeader(stringResource(R.string.profile_section_social))
        SocialRewardsCard(
            inviteCode = uiState.inviteCode,
            badgeCount = uiState.badgeCount,
            onInviteFriends = { showInviteDialog = true }
        )

        ProfileSectionHeader(stringResource(R.string.profile_section_account))
        ProfileMenuCard(borderColor = colors.profile.menuAccountBorder) {
            ProfileMenuItem(
                icon = ProfileIcon.Resource(R.mipmap.ic_profile_setting),
                label = stringResource(R.string.profile_edit),
                iconTint = colors.profile.menuAccountIconTint,
                onClick = { onNavigate(Routes.PROFILE_EDIT) }
            )
            HorizontalDivider(
                color = colors.profile.menuAccountBorder.copy(alpha = 0.15f),
                thickness = 0.5.dp
            )
            ProfileMenuItem(
                icon = ProfileIcon.Resource(R.mipmap.ic_credit_card_setting),
                iconTint = colors.profile.menuAccountIconTint,
                label = stringResource(R.string.profile_recurring)
            )
            HorizontalDivider(
                color = colors.profile.menuAccountBorder.copy(alpha = 0.15f),
                thickness = 0.5.dp
            )
            ProfileMenuItem(
                icon = ProfileIcon.Resource(R.mipmap.ic_calendar),
                label = stringResource(R.string.profile_transaction_history),
                iconTint = colors.profile.menuAccountIconTint,
                onClick = { onNavigate(Routes.TRANSACTION_HISTORY) }
            )
        }

        ProfileSectionHeader(stringResource(R.string.profile_section_security))
        ProfileMenuCard(borderColor = colors.profile.menuSecurityBorder) {
            ProfileMenuItem(
                icon = ProfileIcon.Vector(Icons.Outlined.Lock),
                label = stringResource(R.string.profile_security_center),
                iconTint = colors.profile.menuSecurityIconTint,
                onClick = { onNavigate(Routes.SECURITY_CENTER) }
            )
        }

        ProfileSectionHeader(stringResource(R.string.profile_section_support))
        ProfileMenuCard(borderColor = colors.profile.menuSupportBorder) {
            ProfileMenuItem(
                icon = ProfileIcon.Vector(Icons.AutoMirrored.Outlined.HelpOutline),
                iconTint = colors.profile.menuSupportIconTint,
                label = stringResource(R.string.profile_help_center)
            )
            HorizontalDivider(
                color = colors.profile.menuSupportBorder.copy(alpha = 0.15f),
                thickness = 0.5.dp
            )
            ProfileMenuItem(
                icon = ProfileIcon.Vector(Icons.Outlined.Security),
                iconTint = colors.profile.menuSupportIconTint,
                label = stringResource(R.string.profile_terms)
            )
        }

        Spacer(Modifier.height(4.dp))

        LogoutButton(
            enabled = !uiState.isLoggingOut,
            onClick = { showLogoutDialog = true }
        )

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ProfileSectionHeader(title: String) {
    val colors = LocalAppColors.current
//    Text(
//        text = title,
//        color = colors.profile.sectionHeaderText,
//        fontWeight = FontWeight.Bold,
//        fontSize = 16.sp
//    )
    GradientText(
        text = title,
        brush = colors.profile.sectionHeaderGradient,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun IdentityCard(uiState: ProfileUiState, onClick: () -> Unit = {}) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(
                        colors.accent.primary,
                        alpha = 0.35f,
                        glowRadius = 10.dp,
                        borderRadius = 16.dp
                    )
                else Modifier
            )
            .background(colors.bg.page, RoundedCornerShape(16.dp))
            .border(
                width = 1.5.dp,
                brush = colors.profile.identityCardBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(90.dp),
//                .clip(CircleShape)
//                .background(
//                    Brush.radialGradient(listOf(neonPurple.copy(alpha = 0.7f), neonCyan.copy(alpha = 0.4f)))
//                )
//                .border(1.5.dp, neonCyan, CircleShape),
            contentAlignment = Alignment.Center
        ) {
//            Icon(
//                imageVector = Icons.Default.Person,
//                contentDescription = stringResource(R.string.profile_avatar_desc),
//                tint = Color.White,
//                modifier = Modifier.size(42.dp)
//            )
            Icon(
                modifier = Modifier
                    .size(90.dp),
                tint = Color.Unspecified,
                contentDescription = null,
                painter = painterResource(R.drawable.ic_friend_female),
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = stringResource(R.string.profile_hi_name, uiState.userName.ifEmpty { "---" }),
                color = colors.profile.userNameText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = stringResource(R.string.profile_xcash_id, uiState.xcashId.ifEmpty { "---" }),
                color = colors.profile.xcashIdText,
                fontSize = 13.sp
            )
            if (uiState.isVerified) {
                FullyVerifiedBadge(text = stringResource(R.string.profile_fully_verified))
            }
        }
    }
}

@Composable
private fun SocialRewardsCard(
    inviteCode: String,
    badgeCount: Int,
    onInviteFriends: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(
                        color = colors.accent.primary,
                        alpha = 0.3f,
                        glowRadius = 8.dp,
                        borderRadius = 14.dp
                    )
                else Modifier
            )
            .background(colors.bg.page, RoundedCornerShape(14.dp))
            .border(1.5.dp, colors.profile.socialCardBorder, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左側：Invite & Earn
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.profile_invite_earn),
                    color = colors.profile.socialTitleText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(R.mipmap.ic_gift),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(R.string.profile_g_code, inviteCode),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(
                                colors.accent.primary,
                                alpha = 0.2f,
                                glowRadius = 8.dp,
                                borderRadius = 14.dp
                            )
                        else Modifier
                    )
                    .background(
                        brush = colors.profile.inviteButtonFill,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = colors.profile.inviteButtonBorder,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onInviteFriends
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.profile_invite_friends),
                    color = colors.profile.inviteButtonText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 分隔線
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .width(1.dp)
                .height(100.dp)
                .align(Alignment.CenterVertically)
                .background(colors.profile.socialDivider)
        )

        // 右側：My Badges
        Column(
            modifier = Modifier.weight(0.7f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_my_badges),
                color = colors.profile.socialTitleText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    painter = painterResource(R.mipmap.ic_trophy),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp)
                )
                Icon(
                    painter = painterResource(R.mipmap.ic_star),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp)
                )
                Icon(
                    painter = painterResource(R.mipmap.ic_rocket),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp)
                )
            }
            Text(
                text = stringResource(R.string.profile_earned_badges, badgeCount),
                color = colors.text.body,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ProfileMenuCard(
    borderColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(
                        borderColor,
                        alpha = 0.3f,
                        glowRadius = 8.dp,
                        borderRadius = 14.dp
                    )
                else Modifier
            )
            .background(colors.bg.page, RoundedCornerShape(14.dp))
            .border(1.5.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
        content = content
    )
}

@Composable
private fun ProfileMenuItem(
    icon: ProfileIcon,
    label: String,
    iconTint: Color = Color.White,
    onClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is ProfileIcon.Vector -> Icon(
                imageVector = icon.imageVector,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )

            is ProfileIcon.Resource -> Icon(
                painter = painterResource(icon.resId),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            label,
            color = colors.profile.menuItemText,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = colors.profile.menuChevron,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun LogoutButton(enabled: Boolean, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(
                        color = colors.accent.secondary,
                        alpha = 0.6f,
                        glowRadius = 16.dp,
                        borderRadius = 26.dp
                    )
                else Modifier
            )
            .background(
                brush = colors.profile.logoutButtonFill,
                shape = RoundedCornerShape(10.dp)
            )
            .then(
                colors.profile.logoutButtonBorder?.let {
                    Modifier.border(1.5.dp, it, RoundedCornerShape(10.dp))
                } ?: Modifier
            )
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.profile_logout),
            color = colors.profile.logoutButtonText,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            letterSpacing = 2.sp
        )
    }
}


private val previewUiState = ProfileUiState(
    isLoadingUserInfo = false,
    userName = "Bruce Banner",
    xcashId = "0917-123-4567",
    inviteCode = "G12345",
    badgeCount = 8,
    isVerified = true
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun ProfileScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        ProfileScreenContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun ProfileScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        ProfileScreenContent(uiState = previewUiState)
    }
}
