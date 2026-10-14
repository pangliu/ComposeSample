package com.qpay.xcash.ui.transfer.result

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.aspectRatio
import com.qpay.xcash.ui.components.channelIconResByName
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.TransferResultColors
import com.qpay.xcash.ui.transfer.wallet.TransferResultInfo
import com.qpay.xcash.ui.transfer.wallet.WalletTransferUiState
import com.qpay.xcash.ui.transfer.wallet.WalletTransferViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale", "StringFormatInvalid")
@Composable
fun TransferResultScreen(
    onDone: () -> Unit = {},
    onTransactionHistoryClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onTryAgainClick: () -> Unit = {},
    onReloadWalletClick: () -> Unit = {},
    viewModel: WalletTransferViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 交易已完成，系統返回鍵等同 Done，不可回到 ConfirmTransferScreen 重複送出
    BackHandler { onDone() }

    TransferResultContent(
        uiState = uiState,
        onDone = onDone,
        onTransactionHistoryClick = onTransactionHistoryClick,
        onAddFriendClick = onAddFriendClick,
        onTryAgainClick = onTryAgainClick,
        onReloadWalletClick = onReloadWalletClick,
        onShareClick = { result ->
            val shareText = context.getString(
                R.string.transfer_result_share_text,
                String.format("%,.2f", result.amount.toDouble()),
                result.recipientName,
                result.transactionId
            )
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(sendIntent, null))
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
private fun TransferResultContent(
    uiState: WalletTransferUiState = WalletTransferUiState(),
    onDone: () -> Unit = {},
    onTransactionHistoryClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onTryAgainClick: () -> Unit = {},
    onReloadWalletClick: () -> Unit = {},
    onShareClick: (TransferResultInfo) -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val resultColors = colors.transferResult
    val result = uiState.transferResult ?: return
    val isSuccess = result.isSuccess

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = colors.text.body
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            val backgroundRes = if (isSuccess) assets.successPageBackground else assets.scanPayBackground
            backgroundRes?.let { resId ->
                Image(
                    painter = painterResource(resId),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(48.dp))

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            if (isSuccess) resultColors.statusIconBackground else resultColors.failIconBackground,
                            CircleShape
                        )
                        .border(
                            1.5.dp,
                            if (isSuccess) resultColors.successIconBorder else resultColors.failIconBorder,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = stringResource(R.string.transfer_result_status_icon_desc),
                        tint = if (isSuccess) resultColors.successIconTint else resultColors.failIconTint,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(
                        if (isSuccess) R.string.transfer_result_success_title else R.string.transfer_result_failed_title
                    ),
                    color = resultColors.titleText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(
                        R.string.transfer_result_amount,
                        String.format("%,.2f", result.amount.toDouble())
                    ),
                    color = if (isSuccess) resultColors.successAmountText else resultColors.failAmountText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                val dateText = remember(result.timestampMillis) {
                    SimpleDateFormat("yyyy/M/d HH:mm", Locale.getDefault()).format(Date(result.timestampMillis))
                }
                // 猴子裝飾（Neon 成功時才有）疊在日期文字右側，zIndex 讓它蓋在下方收款卡片之上
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            if (isSuccess) R.string.transfer_result_success_subtitle else R.string.transfer_result_failed_subtitle,
                            dateText
                        ),
                        color = if (isSuccess) resultColors.subtitleText else resultColors.failSubtitleText,
                        fontSize = 12.sp
                    )
                    val monkeyRes = assets.transferResultMonkeyIcon
                    if (isSuccess && monkeyRes != null) {
                        Image(
                            painter = painterResource(monkeyRes),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .offset(x = 8.dp, y = 12.dp)
                                .height(46.dp)
                        )
                    }
                }

                if (!isSuccess && result.failureReason.isNotEmpty()) {
                    Text(
                        text = result.failureReason,
                        color = resultColors.failureReasonText,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                AccountCard(
                    iconRes = assets.confirmTransferAccountIcon,
                    name = stringResource(R.string.transfer_result_wallet_label),
                    subText = maskAccountNumber(uiState.selfPhoneNumber),
                    colors = resultColors,
                    cardBackground = resultColors.payCardBackground,
                    cardBorder = resultColors.payCardBorder
                )

                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = stringResource(R.string.transfer_result_transfer_arrow_desc),
                    tint = resultColors.transferArrowTint,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(24.dp)
                )

                // 收款方 icon 依名稱對應渠道 logo（與 ConfirmTransferScreen 一致），對應不到時沿用預設 icon
                AccountCard(
                    iconRes = assets.confirmTransferAccountIcon,
                    channelIconRes = channelIconResByName(result.recipientName),
                    name = result.recipientName,
                    subText = maskAccountNumber(result.recipientAccountNumber),
                    colors = resultColors,
                    cardBackground = resultColors.toCardBackground,
                    cardBorder = resultColors.toCardBorder
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.transfer_result_balance_after_label),
                        color = resultColors.balanceLabelText,
                        fontSize = 13.sp
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(
                                R.string.transfer_result_balance_value,
                                String.format("%,.0f", result.balanceAfter)
                            ),
                            color = resultColors.balanceValueText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.transfer_result_transaction_history),
                            color = resultColors.transactionHistoryLinkText,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onTransactionHistoryClick() }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.transfer_result_ref_no, result.transactionId),
                    color = resultColors.refNoText,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                // 空白區塊：Neon 成功時左下角放星點、右下角放椰子樹；Black Gold 兩者皆為 null，等同原本的 Spacer
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    val starsRes = assets.transferResultStarsBackground
                    val treeRes = assets.transferResultTreeIcon
                    if (isSuccess && starsRes != null) {
                        Image(
                            painter = painterResource(starsRes),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .width(80.dp)
                        )
                    }
                    if (isSuccess && treeRes != null) {
                        Image(
                            painter = painterResource(treeRes),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 4.dp)
                                .height(54.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(resultColors.addFriendButtonFill)
                        .border(1.dp, resultColors.addFriendButtonBorder, RoundedCornerShape(10.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onAddFriendClick() },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = resultColors.addFriendButtonText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.transfer_result_add_friend_button),
                        color = resultColors.addFriendButtonText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isSuccess) {
                        ResultActionButton(
                            text = stringResource(R.string.transfer_result_done_button),
                            fill = resultColors.doneButtonFill,
                            border = resultColors.doneButtonBorder,
                            textColor = resultColors.doneButtonText,
                            onClick = onDone,
                            modifier = Modifier.weight(1f)
                        )
                        ResultActionButton(
                            text = stringResource(R.string.transfer_result_share_button),
                            fill = resultColors.shareButtonFill,
                            border = resultColors.shareButtonBorder,
                            textColor = resultColors.shareButtonText,
                            onClick = { onShareClick(result) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        ResultActionButton(
                            text = stringResource(R.string.transfer_result_try_again_button),
                            fill = resultColors.tryAgainButtonFill,
                            border = resultColors.tryAgainButtonBorder,
                            textColor = resultColors.tryAgainButtonText,
                            onClick = onTryAgainClick,
                            modifier = Modifier.weight(1f)
                        )
                        ResultActionButton(
                            text = stringResource(R.string.transfer_result_reload_wallet_button),
                            fill = resultColors.reloadWalletButtonFill,
                            border = resultColors.reloadWalletButtonBorder,
                            textColor = resultColors.reloadWalletButtonText,
                            onClick = onReloadWalletClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountCard(
    iconRes: Int,
    name: String,
    subText: String,
    colors: TransferResultColors,
    cardBackground: Color,
    cardBorder: Brush,
    @DrawableRes channelIconRes: Int? = null  // 有值時改顯示渠道 logo（橫向長方形，與渠道方塊同比例）
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBackground)
            .border(1.dp, cardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (channelIconRes != null) {
            Image(
                painter = painterResource(channelIconRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(40.dp)
                    .aspectRatio(1.9f)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.cardIconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = name,
                color = colors.cardNameText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            if (subText.isNotEmpty()) {
                Text(
                    text = subText,
                    color = colors.cardSubText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ResultActionButton(
    text: String,
    fill: Brush,
    border: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(fill)
            .border(1.dp, border, RoundedCornerShape(10.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

// 保留前 3 碼與後 3 碼，中間以 * 遮罩（例：09123456687 → 091*****687）
// 已遮罩過的帳號（例：GeneralTransfer 選卡帶入的 "**** 1234"）原樣顯示，避免重複遮罩
private fun maskAccountNumber(value: String): String {
    if (value.length <= 6 || value.contains('*')) return value
    return value.take(3) + "*".repeat(value.length - 6) + value.takeLast(3)
}

private val previewSuccessResult = TransferResultInfo(
    isSuccess = true,
    amount = 500L,
    recipientName = "I. Torres",
    recipientAccountNumber = "09123456687",
    balanceAfter = 10166.0,
    transactionId = "01234567891234"
)

private val previewFailedResult = previewSuccessResult.copy(
    isSuccess = false,
    recipientName = "BDO Unibank",
    recipientAccountNumber = "**** 1234",
    failureReason = "Insufficient available balance."
)

private val previewUiState = WalletTransferUiState(
    selfPhoneNumber = "09123456789",
    transferResult = previewSuccessResult
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun TransferResultScreenPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        TransferResultContent(uiState = previewUiState)
    }
}

@Preview(name = "Neon - Failed", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun TransferResultScreenPreviewNeonFailed() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        TransferResultContent(uiState = previewUiState.copy(transferResult = previewFailedResult))
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun TransferResultScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        TransferResultContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold - Failed", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun TransferResultScreenPreviewBlackGoldFailed() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        TransferResultContent(uiState = previewUiState.copy(transferResult = previewFailedResult))
    }
}
