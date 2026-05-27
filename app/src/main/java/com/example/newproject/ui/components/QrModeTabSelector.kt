package com.example.newproject.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.theme.cardGradientMid
import com.example.newproject.ui.theme.cardGradientStart
import com.example.newproject.ui.theme.darkBackground
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.welcomeBackground

// 外框漸層：cardGradientMid → cardGradientStart
private val outerBorderBrush
    @Composable get() = Brush.linearGradient(
        colors = listOf(
            neonPurple.copy(alpha = 0.8f),
            neonCyan.copy(alpha = 0.4f)))

// 內圈膠囊形狀（左右全圓角）
private val innerCapsuleShape = RoundedCornerShape(50)

enum class QrMode { SCAN_QR, MY_QR }

/**
 * SCAN QR / MY QR 切換 Tab 元件。
 *
 * - 外框：linearGradient(cardGradientMid, cardGradientStart) 漸層邊框
 * - 選中 Tab（內圈）：左右全圓角膠囊形，neonCyan 邊框 + 半透明填色 + 光暈
 */
@Composable
fun QrModeTabSelector(
    selectedMode: QrMode,
    onModeChange: (QrMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .neonGlow(
                color = neonPurple,
                alpha = 0.55f,
                glowRadius = 12.dp,
                borderRadius = 22.dp)
            .background(
                color = welcomeBackground,
                shape = RoundedCornerShape(22.dp))
            .border(
                width = 1.5.dp,
                brush = outerBorderBrush,
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
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .then(
                if (isSelected) Modifier
                    // neonGlow 在 border/background 之前，光暈才能延伸到外框之外
                    .neonGlow(neonCyan, alpha = 0.55f, glowRadius = 12.dp, borderRadius = 22.dp)
                    .background(
                        color = neonCyan, innerCapsuleShape)
//                    .border(1.dp, neonCyan, innerCapsuleShape)
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
            color = if (isSelected) Color.Black else normalText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun QrModeTabSelectorScanPreview() {
    MaterialTheme {
        QrModeTabSelector(
            selectedMode = QrMode.SCAN_QR,
            onModeChange = {},
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun QrModeTabSelectorMyQrPreview() {
    MaterialTheme {
        QrModeTabSelector(
            selectedMode = QrMode.MY_QR,
            onModeChange = {},
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
