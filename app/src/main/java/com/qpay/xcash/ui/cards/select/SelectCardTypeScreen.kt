package com.qpay.xcash.ui.cards.select

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.LocalAppColors

@Composable
fun SelectCardTypeScreen(onBack: () -> Unit, onNavigate: (String) -> Unit = {}) {
    SelectCardTypeContent(onBack = onBack, onNavigate = onNavigate)
}

@Composable
fun SelectCardTypeContent(onBack: () -> Unit = {}, onNavigate: (String) -> Unit = {}) {
    val colors = LocalAppColors.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SubPageTopBar(
                title = stringResource(R.string.select_card_type_title),
                onBack = onBack
            )

            // Card type options
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                cardTypeOptions.forEach { option ->
                    CardTypeItem(option = option, onClick = { onNavigate(Routes.ADD_NEW_CARD) })
                }
            }
        }
    }
}

private data class CardTypeOption(
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int
)

private val cardTypeOptions = listOf(
    CardTypeOption(
        titleRes = R.string.card_type_credit_title,
        subtitleRes = R.string.card_type_credit_subtitle
    ),
    CardTypeOption(
        titleRes = R.string.card_type_debit_title,
        subtitleRes = R.string.card_type_debit_subtitle
    ),
    CardTypeOption(
        titleRes = R.string.card_type_prepaid_title,
        subtitleRes = R.string.card_type_prepaid_subtitle
    ),
    CardTypeOption(
        titleRes = R.string.card_type_credit_title,
        subtitleRes = R.string.card_type_credit2_subtitle
    )
)

@Composable
private fun CardTypeItem(option: CardTypeOption, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    // Outer Box: glow breathing room — prevents glow clipping
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        // Inner Box: actual visible card with neon glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(color = colors.accent.primary, alpha = 0.4f, glowRadius = 12.dp, borderRadius = 12.dp)
                .background(colors.bg.page, RoundedCornerShape(12.dp))
                .border(1.5.dp, colors.accent.primary.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = colors.accent.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(option.titleRes),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(option.subtitleRes),
                        color = colors.text.body,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun SelectCardTypePreview() {
    MaterialTheme {
        SelectCardTypeContent()
    }
}
