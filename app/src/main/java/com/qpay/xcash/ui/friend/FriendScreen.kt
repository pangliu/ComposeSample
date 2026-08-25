package com.qpay.xcash.ui.friend

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.QrMode
import com.qpay.xcash.ui.components.gradientTint
import com.qpay.xcash.ui.friend.components.FriendTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import io.github.alexzhirkevich.qrose.options.brush

@Composable
fun FriendScreen(
    viewModel: FriendViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateFriendList: () -> Unit,
    onNavigateEmptyList: () -> Unit,
    onNavigateAddFriend: () -> Unit,
    onNavigateQrCode: (QrMode) -> Unit
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is FriendNavigationEvent.ToFriendList -> onNavigateFriendList()
                is FriendNavigationEvent.ToEmptyList -> onNavigateEmptyList()
            }
        }
    }

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        FriendContent(
            paddingValues = paddingValues,
            onBack = onBack,
            onMyListClick = viewModel::onMyListClick,
            onNavigateAddFriend = onNavigateAddFriend,
            onNavigateQrCode = onNavigateQrCode
        )
    }
}

@Composable
private fun FriendContent(
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onMyListClick: () -> Unit,
    onNavigateAddFriend: () -> Unit,
    onNavigateQrCode: (QrMode) -> Unit = {}
) {
    val context = LocalContext.current
    val comingSoon = stringResource(R.string.friend_feature_coming_soon)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        FriendTopBar(title = stringResource(R.string.friend_title), onBack = onBack)

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FriendActionButton(
                icon = Icons.Default.Add,
                label = stringResource(R.string.friend_action_invite),
                onClick = { onNavigateQrCode(QrMode.MY_QR) }
            )
            FriendActionButton(
                icon = Icons.Outlined.QrCode2,
                label = stringResource(R.string.friend_action_qr_code),
                onClick = { onNavigateQrCode(QrMode.SCAN_QR) }
            )
            FriendActionButton(
                icon = Icons.Default.Search,
                label = stringResource(R.string.friend_action_search),
                onClick = onNavigateAddFriend
            )
        }

        Spacer(Modifier.height(28.dp))

        val menuDividerColor = LocalAppColors.current.friend.menuDivider.copy(alpha = 0.15f)
        FriendMenuCard {
            FriendMenuItem(
                icon = Icons.AutoMirrored.Outlined.MenuBook,
                title = stringResource(R.string.friend_third_party_title),
                subtitle = stringResource(R.string.friend_third_party_subtitle),
                showChevron = false,
                onClick = { Toast.makeText(context, comingSoon, Toast.LENGTH_SHORT).show() }
            )
            HorizontalDivider(color = menuDividerColor, thickness = 0.5.dp)
            FriendMenuItem(
                icon = Icons.Outlined.Person,
                title = stringResource(R.string.friend_my_list_title),
                subtitle = stringResource(R.string.friend_my_list_subtitle),
                showChevron = true,
                onClick = onMyListClick
            )
            HorizontalDivider(color = menuDividerColor, thickness = 0.5.dp)
        }
    }
}

@Composable
private fun FriendActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current.friend
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = colors.actionCircleBackground,
                    shape = CircleShape)
                .border(
                    width = 1.dp,
                    brush = colors.actionCircleBorder,
                    shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
//                tint = colors.actionIconTint,
                modifier = Modifier
                    .gradientTint(colors.actionIconTint)
                    .size(24.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            color = colors.actionLabelText,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun FriendMenuCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        content = content
    )
}

@Composable
private fun FriendMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    showChevron: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current.friend
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = colors.menuIconBackground,
                    shape = CircleShape)
                .border(
                    width = 1.dp,
                    brush = colors.actionCircleBorder,
                    shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
//                tint = colors.menuIconTint,
                modifier = Modifier
                    .gradientTint(colors.menuIconTint)
                    .size(18.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = colors.menuTitleText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = colors.menuSubtitleText,
                fontSize = 12.sp
            )
        }

        if (showChevron) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = colors.menuChevron,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun FriendScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        FriendContent(paddingValues = PaddingValues(), onBack = {}, onMyListClick = {}, onNavigateAddFriend = {})
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FriendScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        FriendContent(paddingValues = PaddingValues(), onBack = {}, onMyListClick = {}, onNavigateAddFriend = {})
    }
}
