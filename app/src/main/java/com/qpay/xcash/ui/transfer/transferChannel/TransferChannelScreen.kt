package com.qpay.xcash.ui.transfer.transferChannel

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.TransferChannelItem
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.transfer.transferChannel.components.ChannelTile

@Composable
fun TransferChannelScreen(
    onBack: () -> Unit = {},
    onNext: (TransferChannelItem) -> Unit = {},
    viewModel: TransferChannelViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Unit
            }
        }
    }

    TransferChannelContent(
        uiState = uiState,
        onBack = onBack,
        onChannelClick = { viewModel.selectChannel(it.id) },
        onNext = { uiState.selectedChannel?.let(onNext) }
    )

    LoadingDialog(isShowing = uiState.isLoading)
}

@Composable
private fun TransferChannelContent(
    uiState: TransferChannelUiState = TransferChannelUiState(),
    onBack: () -> Unit = {},
    onChannelClick: (TransferChannelItem) -> Unit = {},
    onNext: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val channelColors = colors.transferChannel
    val isChannelSelected = uiState.selectedChannel != null

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = colors.text.body
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            (assets.subPageBackground ?: assets.scanPayBackground)?.let { resId ->
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
            ) {
                SubPageTopBar(
                    title = stringResource(R.string.transfer_channel_title),
                    onBack = onBack,
                    titleBrush = colors.gradient.goldShimmer
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    channelSection(
                        titleRes = R.string.transfer_channel_e_wallet_section,
                        channels = uiState.eWallets,
                        selectedChannelId = uiState.selectedChannelId,
                        onChannelClick = onChannelClick
                    )
                    channelSection(
                        titleRes = R.string.transfer_channel_bank_section,
                        channels = uiState.banks,
                        selectedChannelId = uiState.selectedChannelId,
                        onChannelClick = onChannelClick
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .height(52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isChannelSelected) channelColors.nextButtonEnabledFill
                            else SolidColor(channelColors.nextButtonDisabledFill)
                        )
                        .clickable(
                            enabled = isChannelSelected,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onNext() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.transfer_channel_next_button),
                        color = if (isChannelSelected) channelColors.nextButtonEnabledText
                        else channelColors.nextButtonDisabledText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.channelSection(
    titleRes: Int,
    channels: List<TransferChannelItem>,
    selectedChannelId: String?,
    onChannelClick: (TransferChannelItem) -> Unit
) {
    if (channels.isEmpty()) return

    item(key = "section_$titleRes", span = { GridItemSpan(maxLineSpan) }) {
        Text(
            text = stringResource(titleRes),
            color = LocalAppColors.current.transferChannel.sectionTitleText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
        )
    }

    items(channels, key = { it.id }) { channel ->
        ChannelTile(
            channel = channel,
            isSelected = channel.id == selectedChannelId,
            onClick = { onChannelClick(channel) }
        )
    }
}

private val previewUiState = TransferChannelUiState(
    isLoadingChannels = false,
    eWallets = listOf(
        TransferChannelItem(id = "xcash", name = "Xcash"),
        TransferChannelItem(id = "gcash", name = "GCash"),
        TransferChannelItem(id = "maya", name = "Maya"),
        TransferChannelItem(id = "gotyme", name = "GoTyme"),
        TransferChannelItem(id = "grab", name = "Grab"),
        TransferChannelItem(id = "qrph", name = "QRPh"),
        TransferChannelItem(id = "shopee_pay", name = "ShopeePay"),
    ),
    banks = listOf(
        TransferChannelItem(id = "bdo_business", name = "BDO Business"),
        TransferChannelItem(id = "uno_digital_bank", name = "UNO Digital Bank"),
        TransferChannelItem(id = "komo", name = "Komo"),
        TransferChannelItem(id = "metrobank", name = "Metrobank"),
        TransferChannelItem(id = "tayocash", name = "TayoCash"),
        TransferChannelItem(id = "landbank", name = "LANDBANK"),
        TransferChannelItem(id = "coin_ph", name = "Coins.ph"),
        TransferChannelItem(id = "star_pay", name = "StarPay"),
        TransferChannelItem(id = "union_digital_bank", name = "UnionDigital Bank"),
        TransferChannelItem(id = "cimb_bank", name = "CIMB Bank"),
        TransferChannelItem(id = "rcbc", name = "RCBC"),
    ),
    selectedChannelId = "gotyme"
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun TransferChannelScreenPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        TransferChannelContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun TransferChannelScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        TransferChannelContent(uiState = previewUiState)
    }
}
