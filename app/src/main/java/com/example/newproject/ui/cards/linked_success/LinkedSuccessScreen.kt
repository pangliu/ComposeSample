package com.example.newproject.ui.cards.linked_success

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
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
import com.example.newproject.R
import com.example.newproject.ui.cards.components.VoucherTicket
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.neonPink

private val CardBg = Color(0xFF0A1628)

@Composable
fun LinkedSuccessScreen(
    onSetupPrimary: () -> Unit = {},
    onEditNickname: () -> Unit = {},
    onNotNow: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg.page)
            .paint(
                painter = painterResource(R.mipmap.bg_sub_page),
                contentScale = ContentScale.FillBounds
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
                    .neonGlow(colors.accent.primary, alpha = 0.5f, glowRadius = 20.dp, borderRadius = 50.dp)
                    .background(
                        color = colors.bg.page,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(2.dp, colors.accent.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = colors.accent.primary,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Card Linked\nSuccessfully!",
                color = colors.accent.primary,
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
                    .neonGlow(colors.accent.primary, alpha = 0.2f, glowRadius = 12.dp, borderRadius = 16.dp)
                    .background(CardBg, RoundedCornerShape(16.dp))
                    .border(1.5.dp, colors.accent.primary.copy(0.5f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VoucherTicket(amount = stringResource(R.string.linked_success_voucher_amount))

                Text(
                    text = "Congratulations!\nYou've received a\n₱50 Voucher!",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                HorizontalDivider(color = colors.accent.primary.copy(0.15f), thickness = 0.5.dp)

                ActionButton(
                    label = stringResource(R.string.linked_success_setup_primary),
                    onClick = onSetupPrimary
                )

                ActionButton(
                    label = stringResource(R.string.linked_success_not_now),
                    onClick = onNotNow
                )
            }
        }
    }
}


@Composable
private fun ActionButton(label: String, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(1.5.dp, colors.accent.primary.copy(0.7f), RoundedCornerShape(24.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = colors.accent.primary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun LinkedSuccessPreview() {
    MaterialTheme {
        LinkedSuccessScreen()
    }
}
