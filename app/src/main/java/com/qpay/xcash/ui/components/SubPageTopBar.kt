package com.qpay.xcash.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

@Composable
    fun SubPageTopBar(
    title: String,
    onBack: () -> Unit = {},
    showBack: Boolean = true,
    showNotifySettings: Boolean = false,
    onNotifySettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        if (showBack) {
            Icon(
                painter = painterResource(R.mipmap.ic_back),
                contentDescription = stringResource(R.string.common_back_desc),
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .size(40.dp)
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(color = colors.accent.primary, alpha = 0.2f, glowRadius = 12.dp, borderRadius = 12.dp)
                        else Modifier
                    )
                    .gradientTint(colors.gradient.silverShimmer)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onBack() }
            )
        }
        GradientText(
            text = title,
            color = Color.White,
            brush = colors.gradient.silverShimmer,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
        if (showNotifySettings) {
            Icon(
                painter = painterResource(R.mipmap.ic_notify_settings),
                contentDescription = stringResource(R.string.common_notify_settings_desc),
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(40.dp)
                    .gradientTint(colors.gradient.silverShimmer)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onNotifySettingsClick() }
            )
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SubPageTopBarPreviewNeon() {
    AppTheme(colors = NeonColors) {
        SubPageTopBar(title = "Page Title", onBack = {}, showNotifySettings = true)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun SubPageTopBarPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        SubPageTopBar(title = "Page Title", onBack = {}, showNotifySettings = true)
    }
}
