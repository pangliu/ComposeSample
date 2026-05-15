package com.example.newproject.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.network.model.response.CreditCardResponse
import com.example.newproject.ui.components.LoadingDialog
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.DarkBackground
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonCyanLight
import com.example.newproject.ui.theme.NeonPurpleLight
import com.example.newproject.ui.theme.SendPink
import com.example.newproject.ui.theme.TokenOrange
import com.example.newproject.ui.theme.WelcomeBackground

@Composable
fun CardsScreen(viewModel: CardsViewModel) {
    val state by viewModel.cardsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCards()
    }

    CardsScreenContent(
        state = state,
        onRefresh = { viewModel.refreshCards() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreenContent(state: CardsState, onRefresh: () -> Unit = {}) {
    val isRefreshing = (state as? CardsState.Success)?.isRefreshing == true
    val pullRefreshState = rememberPullToRefreshState()

    LoadingDialog(isShowing = state is CardsState.Loading)

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullRefreshState,
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                color = NeonCyanLight,
                containerColor = WelcomeBackground
            )
        }
    ) {
        when (state) {
            is CardsState.Loading -> Unit

            is CardsState.Error -> {
                Text(
                    text = state.message,
                    color = SendPink,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is CardsState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Cards Management Title
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

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        // Top Info Card
//                        item {
//                            InfoCard()
//                        }

                        if (state.cards.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CreditCard,
                                        contentDescription = null,
                                        tint = NeonCyan.copy(alpha = 0.4f),
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.cards_empty),
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        } else {
                            itemsIndexed(state.cards) { index, card ->
                                CreditCardItem(card = card, isPrimary = index == 0)
                            }
                        }

                        // Add New Card Button
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            AddNewCardButton()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neonGlow(color = NeonCyanLight, alpha = 0.4f, glowRadius = 15.dp, borderRadius = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(WelcomeBackground)
            .border(2.dp, NeonCyanLight.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
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
fun CreditCardItem(card: CreditCardResponse, isPrimary: Boolean) {
    val glowColor = if (isPrimary) NeonPurpleLight else NeonCyanLight
    val borderColor = if (isPrimary) NeonPurpleLight else NeonCyanLight

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neonGlow(color = glowColor, alpha = 0.6f, glowRadius = 15.dp, borderRadius = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(WelcomeBackground)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
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
                            .background(NeonPurpleLight.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .border(1.dp, NeonPurpleLight, RoundedCornerShape(12.dp))
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
                        .border(1.dp, TokenOrange, RoundedCornerShape(8.dp))
                        .clickable { /* TODO: Manage */ }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cards_manage_btn),
                        color = TokenOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun AddNewCardButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .neonGlow(color = NeonCyanLight, alpha = 0.4f, glowRadius = 15.dp, borderRadius = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(WelcomeBackground)
            .border(2.dp, NeonCyanLight, RoundedCornerShape(24.dp))
            .clickable { /* TODO: Add New Card */ }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = NeonCyanLight,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.cards_add_new_btn),
                color = NeonCyanLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun CardsScreenPreview() {
    MaterialTheme {
        CardsScreenContent(
            state = CardsState.Success(
                cards = listOf(
                    CreditCardResponse(1, "Visa", "Text / Caption", "1234", "bank"),
                    CreditCardResponse(2, "Mastercard", "Text / Caption", "1234", "bank")
                )
            )
        )
    }
}
