package com.qpay.xcash.ui.transfer.transferChannel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.TransferChannelItem
import com.qpay.xcash.ui.theme.LocalAppColors

private val TileShape = RoundedCornerShape(10.dp)

// 渠道方塊：imageUrl 為空或載入中顯示預設白框（含渠道名稱），載入失敗顯示 loading failed
@Composable
fun ChannelTile(
    channel: TransferChannelItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current.transferChannel

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.9f)
            .clip(TileShape)
            .then(
                if (isSelected) Modifier.border(2.dp, colors.tileSelectedBorder, TileShape)
                else Modifier
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        if (channel.imageUrl.isEmpty()) {
            PlaceholderTile(name = channel.name)
        } else {
            SubcomposeAsyncImage(
                model = channel.imageUrl,
                contentDescription = channel.name,
                contentScale = ContentScale.Crop,
                loading = { PlaceholderTile(name = channel.name) },
                error = { FailedTile() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun PlaceholderTile(name: String) {
    val colors = LocalAppColors.current.transferChannel
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.tilePlaceholderBackground)
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = colors.tilePlaceholderText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FailedTile() {
    val colors = LocalAppColors.current.transferChannel
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.tileFailedBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.transfer_channel_loading_failed_dots),
            color = colors.tileFailedText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.transfer_channel_loading_failed),
            color = colors.tileFailedText,
            fontSize = 10.sp
        )
    }
}
