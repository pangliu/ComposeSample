package com.qpay.xcash.ui.home.notifications

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.NotificationResponse
import com.qpay.xcash.network.model.response.NotificationType
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.home.notifications.components.NotificationAdBanner
import com.qpay.xcash.ui.home.notifications.components.NotificationCategoryCard
import com.qpay.xcash.ui.home.notifications.dialog.NotificationSettingsDialog
import com.qpay.xcash.ui.home.notifications.dialog.NotificationSettingsState
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

// 2×2 類別卡片排列順序（依設計稿）
private val categoryRows = listOf(
    listOf(NotificationType.SYSTEM, NotificationType.TRANSACTION),
    listOf(NotificationType.PROMOTIONS, NotificationType.TODO)
)

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
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                    .show()

                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    val colors = LocalAppColors.current
    Scaffold(
        containerColor = colors.notification.pageBackground,
        contentColor = colors.text.onPrimary
    ) { paddingValues ->
        LoadingDialog(isShowing = uiState.isLoading)
        NotificationsContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onBack = onBack,
            onSettingsClick = { showSettingsDialog = true },
            onTabSelected = viewModel::setTab,
            // TODO: 類別通知列表頁尚未設計，待補上路由後導向
            onCategoryClick = {},
            // TODO: Banner 活動內容尚未定義
            onClaimClick = {}
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
    onCategoryClick: (NotificationType) -> Unit,
    onClaimClick: () -> Unit
) {
    val colors = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SubPageTopBar(
            title = stringResource(R.string.notifications_title),
            onBack = onBack,
            showNotifySettings = true,
            onNotifySettingsClick = onSettingsClick,
            titleBrush = colors.notification.titleBrush ?: colors.gradient.goldShimmer
        )
        NotificationTabRow(selectedTab = uiState.selectedTab, onTabSelected = onTabSelected)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            categoryRows.forEach { rowTypes ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    rowTypes.forEach { type ->
                        NotificationCategoryCard(
                            type = type,
                            count = uiState.countOf(type),
                            onClick = { onCategoryClick(type) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

//            Spacer(Modifier.height(4.dp))

            NotificationAdBanner(onClaimClick = onClaimClick)
        }
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
    val tabColors = LocalAppColors.current.notification.tab
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
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
                    color = if (isSelected) tabColors.activeText else tabColors.inactiveText,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .height(2.dp)
                        .then(
                            if (isSelected) Modifier.background(tabColors.indicator, RoundedCornerShape(1.dp))
                            else Modifier
                        )
                )
            }
        }
    }
}

private val previewNotifications = listOf(
    NotificationResponse("1", NotificationType.SYSTEM, "Security Check Passed", "", isRead = false, createdAt = 0L),
    NotificationResponse("2", NotificationType.SYSTEM, "App Updated", "", isRead = false, createdAt = 0L),
    NotificationResponse("3", NotificationType.TRANSACTION, "Payment Sent", "", isRead = false, createdAt = 0L),
    NotificationResponse("4", NotificationType.TRANSACTION, "Money Received", "", isRead = true, createdAt = 0L),
    NotificationResponse("5", NotificationType.TODO, "Complete Your Profile", "", isRead = false, createdAt = 0L),
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1423)
@Composable
private fun NotificationsScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        NotificationsContent(
            uiState = NotificationsUiState(isLoading = false, notifications = previewNotifications),
            paddingValues = PaddingValues(),
            onBack = {},
            onSettingsClick = {},
            onTabSelected = {},
            onCategoryClick = {},
            onClaimClick = {}
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF0C0906)
@Composable
private fun NotificationsScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        NotificationsContent(
            uiState = NotificationsUiState(isLoading = false, notifications = previewNotifications),
            paddingValues = PaddingValues(),
            onBack = {},
            onSettingsClick = {},
            onTabSelected = {},
            onCategoryClick = {},
            onClaimClick = {}
        )
    }
}
