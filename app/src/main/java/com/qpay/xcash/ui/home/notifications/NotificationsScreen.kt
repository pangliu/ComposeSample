package com.qpay.xcash.ui.home.notifications

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.NotificationResponse
import com.qpay.xcash.network.model.response.NotificationType
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.home.notifications.components.NotificationCard
import com.qpay.xcash.ui.home.notifications.dialog.NotificationSettingsDialog
import com.qpay.xcash.ui.home.notifications.dialog.NotificationSettingsState
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }
    var notificationSettings by remember { mutableStateOf(NotificationSettingsState()) }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    val colors = LocalAppColors.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        LoadingDialog(isShowing = uiState.isLoading)
        NotificationsContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onBack = onBack,
            onSettingsClick = { showSettingsDialog = true },
            onTabSelected = viewModel::setTab,
            onCategorySelected = viewModel::setCategoryFilter
        )

        if (showSettingsDialog) {
            NotificationSettingsDialog(
                settings = notificationSettings,
                onSettingsChange = { notificationSettings = it },
                onDismiss = { showSettingsDialog = false }
            )
        }
    }
}

@Composable
private fun NotificationsContent(
    uiState: NotificationsUiState,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onSettingsClick: () -> Unit,
    onTabSelected: (NotificationTab) -> Unit,
    onCategorySelected: (NotificationCategoryFilter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        NotificationsTopBar(onBack = onBack, onSettingsClick = onSettingsClick)
        NotificationTabRow(selectedTab = uiState.selectedTab, onTabSelected = onTabSelected)
        Spacer(modifier = Modifier.height(12.dp))
        NotificationFilterRow(selectedCategory = uiState.selectedCategory, onCategorySelected = onCategorySelected)
        Spacer(modifier = Modifier.height(8.dp))

        val filtered = uiState.filteredNotifications
        if (filtered.isEmpty() && !uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.notifications_empty),
                    color = LocalAppColors.current.text.body,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { notification ->
                    NotificationCard(notification = notification)
                }
            }
        }
    }
}

@Composable
private fun NotificationsTopBar(
    onBack: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Icon(
            painter = painterResource(R.mipmap.ic_back),
            contentDescription = stringResource(R.string.common_back_desc),
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(40.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onBack() }
        )
        Text(
            text = stringResource(R.string.notifications_title),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
        Icon(
//            imageVector = Icons.Default.Settings,
            painter = painterResource(R.mipmap.ic_notify_settings),
            contentDescription = stringResource(R.string.notifications_settings_desc),
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(25.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onSettingsClick() }
        )
    }
}

private val notificationTabs = listOf(
    NotificationTab.ALL to R.string.notifications_tab_all,
    NotificationTab.UNREAD to R.string.notifications_tab_unread,
    NotificationTab.READ to R.string.notifications_tab_read
)

@Composable
private fun NotificationTabRow(
    selectedTab: NotificationTab,
    onTabSelected: (NotificationTab) -> Unit
) {
    val colors = LocalAppColors.current
    Row(modifier = Modifier.fillMaxWidth()) {
        notificationTabs.forEach { (tab, labelRes) ->
            val isSelected = tab == selectedTab
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTabSelected(tab) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(labelRes),
                    color = if (isSelected) Color.White else colors.text.body,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(1.5.dp)
                        .background(
                            if (isSelected) colors.accent.primary else Color.Transparent,
                            RoundedCornerShape(1.dp)
                        )

                )
            }
        }
    }
}

private val notificationFilters = listOf(
    NotificationCategoryFilter.ALL to R.string.notifications_filter_all,
    NotificationCategoryFilter.SYSTEM to R.string.notifications_filter_system,
    NotificationCategoryFilter.ACTIVITY to R.string.notifications_filter_activity,
    NotificationCategoryFilter.PROMO to R.string.notifications_filter_promo
)

@Composable
private fun NotificationFilterRow(
    selectedCategory: NotificationCategoryFilter,
    onCategorySelected: (NotificationCategoryFilter) -> Unit
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        notificationFilters.forEach { (filter, labelRes) ->
            val isSelected = filter == selectedCategory
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isSelected) colors.accent.primary else Color.Transparent,
                        RoundedCornerShape(50)
                    )
                    .border(1.dp, colors.accent.primary.copy(alpha = 0.6f), RoundedCornerShape(50))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCategorySelected(filter) }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(labelRes),
                    color = if (isSelected) colors.bg.page else colors.text.body,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

private val previewNotifications = listOf(
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
        isRead = true,
        createdAt = System.currentTimeMillis() - 2 * 60 * 60_000
    )
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun NotificationsScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        NotificationsContent(
            uiState = NotificationsUiState(isLoading = false, notifications = previewNotifications),
            paddingValues = PaddingValues(),
            onBack = {},
            onSettingsClick = {},
            onTabSelected = {},
            onCategorySelected = {}
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun NotificationsScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        NotificationsContent(
            uiState = NotificationsUiState(isLoading = false, notifications = previewNotifications),
            paddingValues = PaddingValues(),
            onBack = {},
            onSettingsClick = {},
            onTabSelected = {},
            onCategorySelected = {}
        )
    }
}
