package com.qpay.xcash.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.cards.components.VoucherTicket
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.neonCyanLight
import com.qpay.xcash.ui.theme.neonGreen
import com.qpay.xcash.ui.theme.neonPink
import com.qpay.xcash.ui.theme.neonPurpleLight

private val tokenOrange = Color(0xFFFF8C00)
private val promoBannerCount = 4

@Composable
fun CardsScreen(viewModel: CardsViewModel, onNavigate: (String) -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchCards()
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    CardsScreenContent(
        uiState = uiState,
        onRefresh = { viewModel.refreshCards() },
        onNavigate = onNavigate,
        onCardClick = { card ->
            viewModel.selectCard(card)
            onNavigate(Routes.cardDetail(card.id))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreenContent(uiState: CardsUiState, onRefresh: () -> Unit = {}, onNavigate: (String) -> Unit = {}, onCardClick: (CreditCardResponse) -> Unit = {}) {
    val colors = LocalAppColors.current
    val pullRefreshState = rememberPullToRefreshState()

    LoadingDialog(isShowing = uiState.isLoading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg.page)
    ) {
        Text(
            text = stringResource(R.string.cards_management_title),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            state = pullRefreshState,
            modifier = Modifier.weight(1f),
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullRefreshState,
                    isRefreshing = uiState.isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter),
                    color = neonCyanLight,
                    containerColor = colors.bg.page
                )
            }
        ) {
            if (uiState.cards.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    CardsEmptyState(onNavigate = onNavigate)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    itemsIndexed(uiState.cards) { index, card ->
                        CreditCardItem(
                            card = card,
                            isPrimary = index == 0,
                            onClick = { onCardClick(card) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        AddNewCardButton(onNavigate = onNavigate)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard() {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neonGlow(color = neonCyanLight, alpha = 0.4f, glowRadius = 15.dp, borderRadius = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.bg.page)
            .border(2.dp, neonCyanLight.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.cards_info_title),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.cards_info_body_1),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
            Text(
                text = stringResource(R.string.cards_info_body_2),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun CreditCardItem(card: CreditCardResponse, isPrimary: Boolean, onClick: () -> Unit = {}) {
    val colors = LocalAppColors.current
    val glowColor = if (isPrimary) neonPurpleLight else neonCyanLight
    val borderColor = if (isPrimary) neonPurpleLight else neonCyanLight

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neonGlow(color = glowColor, alpha = 0.6f, glowRadius = 15.dp, borderRadius = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.bg.page)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(20.dp)
    ) {
        Column {
            // Top Row: Logo & Card Type / Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Card Logo (Text fallback)
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (card.cardType.contains("visa", true)) "VISA" else if (card.cardType.contains("master", true)) "MC" else "CARD",
                        color = Color(0xFF00155B), // Visa blue
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }

                if (isPrimary) {
                    Box(
                        modifier = Modifier
                            .background(neonPurpleLight.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .border(1.dp, neonPurpleLight, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cards_primary_badge),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = "VISA/MC",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Middle Row: Masked Card Number
            val cleanNumber = card.cardNumber.replace(" ", "").replace("-", "")
            val last4 = if (cleanNumber.length >= 4) cleanNumber.takeLast(4) else cleanNumber
            val maskedNumber = "**** **** **** $last4"

            Text(
                text = maskedNumber,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Row: Nickname & Caption + Manage Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.cards_card_nickname),
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = card.cardName,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .border(1.dp, tokenOrange, RoundedCornerShape(8.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onClick() }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cards_manage_btn),
                        color = tokenOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun AddNewCardButton(onNavigate: (String) -> Unit = {}) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .neonGlow(color = neonCyanLight, alpha = 0.4f, glowRadius = 15.dp, borderRadius = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(colors.bg.page)
            .border(1.5.dp, neonCyanLight, RoundedCornerShape(24.dp))
            .clickable { onNavigate(Routes.SELECT_CARD_TYPE) }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = neonCyanLight,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.cards_add_new_btn),
                color = neonCyanLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CardsEmptyState(onNavigate: (String) -> Unit = {}) {
    val colors = LocalAppColors.current
    val pagerState = rememberPagerState(pageCount = { promoBannerCount })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 輪播 Banner：horizontal padding 僅為光暈預留空間，不做視覺縮排
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),
            pageSpacing = 16.dp
        ) { page ->
            PromoBannerCard(page = page)
        }

        // 分頁指示點
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(promoBannerCount) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .background(
                            if (pagerState.currentPage == index) colors.accent.primary else Color.White.copy(alpha = 0.3f),
                            CircleShape
                        )
                )
            }
        }

        // Link Your First Card 按鈕：外層提供光暈空間，內層才是可見元件
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .neonGlow(colors.accent.primary, alpha = 0.5f, glowRadius = 12.dp, borderRadius = 26.dp)
                    .background(colors.bg.page, RoundedCornerShape(26.dp))
                    .border(1.5.dp, colors.accent.primary, RoundedCornerShape(26.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onNavigate(Routes.SELECT_CARD_TYPE) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.cards_link_first),
                    color = neonGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = stringResource(R.string.cards_protected),
            color = colors.text.body,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PromoBannerCard(page: Int) {
    val colors = LocalAppColors.current
    // 外層 Box：padding 為光暈預留空間，光暈永遠在此範圍內，不依賴父容器允許 overflow
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        // 內層 Box：實際的卡片外觀
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .neonGlow(colors.accent.primary, alpha = 0.4f, glowRadius = 12.dp, borderRadius = 16.dp)
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF1A1050), Color(0xFF0B1030))),
                    RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(listOf(colors.accent.primary, colors.accent.secondary, colors.accent.primary)),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (page == 0) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    VoucherTicket(amount = stringResource(R.string.cards_empty_voucher_amount))
                    Text(
                        text = stringResource(R.string.cards_empty_promo_text),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun CardsScreenPreview() {
    MaterialTheme {
        CardsScreenContent(
            uiState = CardsUiState(
                isLoadingCards = false,
//                cards = listOf(
//                    CreditCardResponse(1, "Visa", "Text / Caption", "1234", "bank"),
//                    CreditCardResponse(2, "Mastercard", "Text / Caption", "1234", "bank")
//                )
                cards = emptyList()
            )
        )
    }
}
