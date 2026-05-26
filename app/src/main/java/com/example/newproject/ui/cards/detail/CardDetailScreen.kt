package com.example.newproject.ui.cards.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newproject.R
import com.example.newproject.network.model.response.CreditCardResponse
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonCyanLight
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.sendPink
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun CardDetailScreen(
    viewModel: CardDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateBack.collect { onBack() }
    }

    CardDetailContent(
        uiState = uiState,
        onBack = onBack,
        onNicknameChange = { viewModel.updateNicknameInput(it) },
        onNicknameDone = { viewModel.saveNickname() },
        onSetPrimary = { viewModel.togglePrimary() },
        onShowUnlinkDialog = { viewModel.showUnlinkDialog() },
        onHideUnlinkDialog = { viewModel.hideUnlinkDialog() },
        onConfirmUnlink = { viewModel.unlinkCard() }
    )
}

@Composable
fun CardDetailContent(
    uiState: CardDetailUiState,
    onBack: () -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onNicknameDone: () -> Unit = {},
    onSetPrimary: () -> Unit = {},
    onShowUnlinkDialog: () -> Unit = {},
    onHideUnlinkDialog: () -> Unit = {},
    onConfirmUnlink: () -> Unit = {}
) {
    // 計算卡號後四碼供 UnlinkDialog 使用
    val last4 = uiState.card?.cardNumber
        ?.replace(" ", "")?.replace("-", "")
        ?.let { if (it.length >= 4) it.takeLast(4) else it }
        ?: ""

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = welcomeBackground,
            contentColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // TopBar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.card_detail_back_desc),
                            tint = Color.White
                        )
                    }
                    Text(
                        text = stringResource(R.string.card_detail_title),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                uiState.card?.let { card ->
                    CardFaceView(
                        card = card,
                        onEdit = { /* TODO: edit card */ }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Card Nickname
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cards_card_nickname),
                            color = normalText,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        NicknameField(
                            value = uiState.nicknameInput,
                            onValueChange = onNicknameChange,
                            onDone = onNicknameDone
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.1f),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Set as Primary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.card_detail_set_primary),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Switch(
                            checked = uiState.isPrimary,
                            onCheckedChange = { onSetPrimary() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color.Gray,
                                checkedBorderColor = neonCyan,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Unlink Card 按鈕 → 觸發確認 Dialog
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .border(
                                width = 1.5.dp,
                                color = sendPink,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .neonGlow(
                                color = sendPink,
                                alpha = 0.5f,
                                glowRadius = 24.dp,
                                borderRadius = 24.dp
                            )
                            .background(
                                color = welcomeBackground,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onShowUnlinkDialog() }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.card_detail_unlink_btn),
                            color = sendPink,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // Unlink 確認 Dialog overlay（疊在 Scaffold 上層）
        UnlinkCardDialog(
            last4 = last4,
            isVisible = uiState.showUnlinkDialog,
            onConfirm = onConfirmUnlink,
            onDismiss = onHideUnlinkDialog
        )
    }
}

@Composable
private fun CardFaceView(card: CreditCardResponse, onEdit: () -> Unit) {
    val cleanNumber = card.cardNumber.replace(" ", "").replace("-", "")
    val last4 = if (cleanNumber.length >= 4) cleanNumber.takeLast(4) else cleanNumber

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .aspectRatio(1.586f)
            .neonGlow(neonPurpleLight, alpha = 0.5f, glowRadius = 20.dp, borderRadius = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1A0C3E), Color(0xFF080D2A)))
            )
            .border(
                1.5.dp,
                Brush.linearGradient(listOf(neonPurple, neonCyanLight, neonPurple)),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        // Top row: icon + edit button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onEdit() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stringResource(R.string.card_detail_edit_btn),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Masked card number
        Text(
            text = "**** **** **** $last4",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 2.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        // Card network logo
        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            CardNetworkLogo(cardType = card.cardType)
        }
    }
}

@Composable
private fun CardNetworkLogo(cardType: String) {
    when {
        cardType.contains("visa", ignoreCase = true) -> {
            Text(
                text = "VISA",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }
        cardType.contains("master", ignoreCase = true) -> {
            Canvas(modifier = Modifier.size(44.dp, 28.dp)) {
                val radius = size.height / 2
                val centerY = size.height / 2
                drawCircle(color = Color(0xFFEB001B), radius = radius, center = Offset(size.width * 0.38f, centerY))
                drawCircle(color = Color(0xFFF79E1B), radius = radius, center = Offset(size.width * 0.62f, centerY))
            }
        }
        else -> {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun NicknameField(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, neonCyanLight, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            singleLine = true,
            cursorBrush = SolidColor(neonCyanLight),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onDone() })
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = neonCyanLight,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun CardDetailScreenPreview() {
    MaterialTheme {
        CardDetailContent(
            uiState = CardDetailUiState(
                card = CreditCardResponse(1, "Mastercard", "My Main Card", "5353", "gcash", isPrimary = true),
                nicknameInput = "My Main Card",
                showUnlinkDialog = false
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun CardDetailScreenWithDialogPreview() {
    MaterialTheme {
        CardDetailContent(
            uiState = CardDetailUiState(
                card = CreditCardResponse(1, "Mastercard", "My Main Card", "5353", "gcash", isPrimary = true),
                nicknameInput = "My Main Card",
                showUnlinkDialog = true
            )
        )
    }
}
