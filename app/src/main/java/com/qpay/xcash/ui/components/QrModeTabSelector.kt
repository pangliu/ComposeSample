package com.qpay.xcash.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.silverGray

// 內圈膠囊形狀（左右全圓角）
private val innerCapsuleShape = RoundedCornerShape(50)

enum class QrMode { SCAN_QR, MY_QR }

/**
 * SCAN QR / MY QR 切換 Tab 元件。
 *
 * - 外框：linearGradient(navyDark, indigoDark) 漸層邊框
 * - 選中 Tab（內圈）：左右全圓角膠囊形，neonCyan 邊框 + 半透明填色 + 光暈
 */
@Composable
fun QrModeTabSelector(
    selectedMode: QrMode,
    onModeChange: (QrMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(
                        color = colors.accent.secondary,
                        alpha = 0.55f,
                        glowRadius = 12.dp,
                        borderRadius = 22.dp)
                else Modifier
            )
            .background(
                color = colors.bg.page,
                shape = RoundedCornerShape(22.dp))
            .border(
                width = 1.5.dp,
                brush = colors.scanPay.qrTabBorder,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(5.dp),                 // 外框與內圈之間的留白
        verticalAlignment = Alignment.CenterVertically
    ) {
        QrTab(
            label = stringResource(R.string.scan_pay_tab_scan_qr),
            isSelected = selectedMode == QrMode.SCAN_QR,
            onClick = { onModeChange(QrMode.SCAN_QR) }
        )
        QrTab(
            label = stringResource(R.string.scan_pay_tab_my_qr),
            isSelected = selectedMode == QrMode.MY_QR,
            onClick = { onModeChange(QrMode.MY_QR) }
        )
    }
}

@Composable
private fun RowScope.QrTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .then(
                if (isSelected) Modifier
                    // neonGlow 在 border/background 之前，光暈才能延伸到外框之外
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(colors.accent.primary, alpha = 0.55f, glowRadius = 12.dp, borderRadius = 22.dp)
                        else Modifier
                    )
                    .background(
                        brush = colors.scanPay.qrTabSelectedFill, shape = innerCapsuleShape)
//                    .border(1.dp, colors.accent.primary, innerCapsuleShape)
                else Modifier
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else silverGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun QrModeTabSelectorScanPreviewNeon() {
    AppTheme(colors = NeonColors) {
        QrModeTabSelector(
            selectedMode = QrMode.SCAN_QR,
            onModeChange = {},
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun QrModeTabSelectorScanPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        QrModeTabSelector(
            selectedMode = QrMode.SCAN_QR,
            onModeChange = {},
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun QrModeTabSelectorMyQrPreviewNeon() {
    AppTheme(colors = NeonColors) {
        QrModeTabSelector(
            selectedMode = QrMode.MY_QR,
            onModeChange = {},
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun QrModeTabSelectorMyQrPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        QrModeTabSelector(
            selectedMode = QrMode.MY_QR,
            onModeChange = {},
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
