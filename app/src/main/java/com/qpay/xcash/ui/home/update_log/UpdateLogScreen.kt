package com.qpay.xcash.ui.home.update_log

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.UpdateLogResponse
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun UpdateLogScreen(
    viewModel: UpdateLogViewModel,
    onBack: () -> Unit
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

    val colors = LocalAppColors.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        LoadingDialog(isShowing = uiState.isLoading)
        UpdateLogContent(
            logs = uiState.logs,
            paddingValues = paddingValues,
            onBack = onBack
        )
    }
}

@Composable
private fun UpdateLogContent(
    logs: List<UpdateLogResponse>,
    paddingValues: PaddingValues,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SubPageTopBar(
            title = stringResource(R.string.setting_update_log),
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(logs) { log ->
                UpdateLogCard(log = log)
            }
        }
    }
}

@Composable
private fun UpdateLogCard(log: UpdateLogResponse) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(
                        colors.updateLog.cardBorder,
                        alpha = 0.2f,
                        glowRadius = 12.dp,
                        borderRadius = 12.dp
                    )
                else Modifier
            )
            .background(colors.updateLog.cardBackground, RoundedCornerShape(12.dp))
            .border(
                width = 1.5.dp,
                color = colors.updateLog.cardBorder.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GradientText(
                text = log.date,
                color = colors.updateLog.titleText,
                brush = colors.updateLog.titleGradient,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.size(12.dp))
            GradientText(
                text = log.title,
                color = colors.updateLog.titleText,
                brush = colors.updateLog.titleGradient,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            color = colors.updateLog.divider.copy(alpha = 0.9f),
            thickness = 0.5.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {}
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = log.message,
                color = colors.updateLog.messageText,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.updateLog.chevronIcon,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private val previewLogs = listOf(
    UpdateLogResponse(
        date = "2026-06-18",
        title = "v2.3.0 Release",
        message = "New split bill feature, improved QR scan performance, and various bug fixes."
    ),
    UpdateLogResponse(
        date = "2026-05-01",
        title = "v2.2.0 Release",
        message = "Added transaction history export, fixed login crash on Android 12."
    )
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun UpdateLogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        UpdateLogContent(
            logs = previewLogs,
            paddingValues = PaddingValues(),
            onBack = {}
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun UpdateLogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        UpdateLogContent(
            logs = previewLogs,
            paddingValues = PaddingValues(),
            onBack = {}
        )
    }
}
