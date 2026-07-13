package com.qpay.xcash.ui.cards.detail

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.NeonSwitch
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.silverGray

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
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    // 計算卡號後四碼供 UnlinkDialog 使用
    val last4 = uiState.card?.cardNumber
        ?.replace(" ", "")?.replace("-", "")
        ?.let { if (it.length >= 4) it.takeLast(4) else it }
        ?: ""
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = colors.bg.page,
            contentColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        assets.subPageBackground?.let {
                            Modifier.paint(
                                painter = painterResource(it),
                                contentScale = ContentScale.FillBounds
                            )
                        } ?: Modifier
                    )
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                SubPageTopBar(
                    title = stringResource(R.string.card_detail_title),
                    onBack = onBack
                )

                Spacer(modifier = Modifier.height(16.dp))

                uiState.card?.let { card ->
                    CardFaceView(
                        card = card,
                        isPrimary = uiState.isPrimary,
                        onManage = { /* TODO: manage card */ }
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
                            color = Color.White,
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
                        NeonSwitch(
                            checked = uiState.isPrimary,
                            onCheckedChange = { onSetPrimary() },
                            activeColor = colors.accent.primary,
                            showLabel = true
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Unlink Card 按鈕 → 觸發確認 Dialog
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp)
                            .border(
                                width = 1.5.dp,
                                color = colors.cardDetail.unlinkBorder,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .background(
                                brush = colors.cardDetail.unlinkBackground,
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
                            color = colors.cardDetail.unlinkText,
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
private fun CardFaceView(card: CreditCardResponse, isPrimary: Boolean, onManage: () -> Unit) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val cleanNumber = card.cardNumber.replace(" ", "").replace("-", "")
    val last4 = if (cleanNumber.length >= 4) cleanNumber.takeLast(4) else cleanNumber
    val cardDetailBg = if(isPrimary) assets.cardDetailBg else assets.cardSecondaryBg

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .aspectRatio(assets.cardDetailAspectRatio)
//            .clip(RoundedCornerShape(16.dp))
            .paint(
                painter = painterResource(cardDetailBg),
                contentScale = ContentScale.FillBounds
            )
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top row: 卡種 logo + Primary 徽章
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardNetworkLogo(cardType = card.cardType)

                if (isPrimary) {
                    Box(
                        modifier = Modifier
                            .background(
                                colors.cards.primaryBadgeBackground,
                                colors.cards.primaryBadgeShape
                            )
                            .border(
                                width = 1.dp,
                                brush = colors.cards.primaryBadgeBorder,
                                colors.cards.primaryBadgeShape
                            )
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        GradientText(
                            text = stringResource(R.string.cards_primary_badge),
                            brush = colors.cards.primaryBadgeText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Masked card number
            Text(
                text = "**** **** **** $last4",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )

            // 將 bottom row 推至卡片底部
            Spacer(modifier = Modifier.weight(1f))

            // Bottom row: 卡片暱稱 / 姓名 + Manage 按鈕
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.cards_card_nickname),
                        color = silverGray,
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
                        .border(1.dp, colors.cards.manageButtonBorder, RoundedCornerShape(8.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onManage() }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    GradientText(
                        text = stringResource(R.string.cards_manage_btn),
                        brush = colors.cards.manageButtonText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun CardNetworkLogo(cardType: String) {
    when {
        cardType.contains("visa", ignoreCase = true) -> {
            Image(
                painter = painterResource(R.drawable.ic_visa_card),
                contentDescription = null,
                modifier = Modifier.size(width = 40.dp, height = 26.dp)
            )
        }
        cardType.contains("master", ignoreCase = true) -> {
            Image(
                painter = painterResource(R.drawable.ic_master_card),
                contentDescription = null,
                modifier = Modifier.size(width = 40.dp, height = 26.dp)
            )
        }
        else -> {
            // TODO: 其他卡種之後補圖片
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
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.cardDetail.nicknameBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(color = colors.cardDetail.nicknameText, fontSize = 16.sp),
            singleLine = true,
            cursorBrush = SolidColor(colors.cardDetail.nicknameCursor),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onDone() })
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = colors.cardDetail.nicknameEditIcon,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun CardDetailScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        CardDetailContent(
            uiState = CardDetailUiState(
                card = CreditCardResponse(1, "Mastercard", "My Main Card", "5353", "gcash", isPrimary = true),
                nicknameInput = "My Main Card",
                isPrimary = true,
                showUnlinkDialog = false
            )
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun CardDetailScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        CardDetailContent(
            uiState = CardDetailUiState(
                card = CreditCardResponse(1, "Mastercard", "My Main Card", "5353", "gcash", isPrimary = true),
                nicknameInput = "My Main Card",
                isPrimary = true,
                showUnlinkDialog = false
            )
        )
    }
}

@Preview(name = "Neon Dialog", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun CardDetailScreenWithDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        CardDetailContent(
            uiState = CardDetailUiState(
                card = CreditCardResponse(1, "Mastercard", "My Main Card", "5353", "gcash", isPrimary = true),
                nicknameInput = "My Main Card",
                isPrimary = true,
                showUnlinkDialog = true
            )
        )
    }
}

@Preview(name = "Black Gold Dialog", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun CardDetailScreenWithDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        CardDetailContent(
            uiState = CardDetailUiState(
                card = CreditCardResponse(1, "Mastercard", "My Main Card", "5353", "gcash", isPrimary = true),
                nicknameInput = "My Main Card",
                isPrimary = true,
                showUnlinkDialog = true
            )
        )
    }
}
