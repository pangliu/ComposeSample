package com.qpay.xcash.ui.cards.linked_success

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun LinkedSuccessScreen(
    onSetupPrimary: () -> Unit = {},
    onEditNickname: () -> Unit = {},
    onNotNow: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg.page)
            .then(
                assets.subPageBackground?.let {
                    Modifier.paint(
                        painter = painterResource(it),
                        contentScale = ContentScale.FillBounds
                    )
                } ?: Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            // Checkmark circle
            Spacer(Modifier.height(100.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(colors.accent.primary, alpha = 0.5f, glowRadius = 20.dp, borderRadius = 50.dp)
                        else Modifier
                    )
                    .background(
                        color = colors.bg.page,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(2.dp, colors.linkedSuccess.checkCircleBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = colors.linkedSuccess.checkIcon,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.linked_success_title),
                style = TextStyle(brush = colors.linkedSuccess.titleText),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(Modifier.height(24.dp))

            // Info card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(colors.accent.primary, alpha = 0.2f, glowRadius = 12.dp, borderRadius = 16.dp)
                        else Modifier
                    )
                    .background(colors.linkedSuccess.cardBackground, RoundedCornerShape(16.dp))
                    .border(1.5.dp, colors.linkedSuccess.cardBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(assets.voucherTicketBg),
                        contentDescription = null
                    )
                    GradientText(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.linked_success_voucher_amount),
                        brush = colors.cards.voucherAmountText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = stringResource(R.string.linked_success_congrats),
                    color = colors.linkedSuccess.congratsText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                HorizontalDivider(color = colors.linkedSuccess.divider, thickness = 0.5.dp)

                ActionButton(
                    label = stringResource(R.string.linked_success_setup_primary),
                    backgroundBrush = colors.linkedSuccess.setupPrimaryBackground,
                    borderBrush = colors.linkedSuccess.setupPrimaryBorder,
                    textBrush = colors.linkedSuccess.setupPrimaryText,
                    onClick = onSetupPrimary
                )

                ActionButton(
                    label = stringResource(R.string.linked_success_not_now),
                    backgroundBrush = colors.linkedSuccess.notNowBackground,
                    borderBrush = colors.linkedSuccess.notNowBorder,
                    textBrush = colors.linkedSuccess.notNowText,
                    onClick = onNotNow
                )
            }
        }
    }
}


@Composable
private fun ActionButton(
    label: String,
    backgroundBrush: Brush,
    borderBrush: Brush,
    textBrush: Brush,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(backgroundBrush, RoundedCornerShape(24.dp))
            .border(1.5.dp, borderBrush, RoundedCornerShape(24.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        GradientText(
            text = label,
            brush = textBrush,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun LinkedSuccessPreviewNeon() {
    AppTheme(colors = NeonColors) {
        LinkedSuccessScreen()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun LinkedSuccessPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        LinkedSuccessScreen()
    }
}
