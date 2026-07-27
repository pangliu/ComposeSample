package com.qpay.xcash.ui.avatar

import android.net.Uri
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.UploadAvatarErrorDialog
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

private const val MIN_ZOOM = 1f
private const val MAX_ZOOM = 3f
private const val DEFAULT_ZOOM = (MIN_ZOOM + MAX_ZOOM) / 2f
private val SLIDER_TRACK_HEIGHT = 6.dp
private val SLIDER_THUMB_SIZE = 20.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAvatarScreen(
    imageUri: Uri,
    onCancel: () -> Unit,
    onChoose: (imageUri: Uri, zoomScale: Float) -> Unit,
    viewModel: EditAvatarViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current
    val uiState by viewModel.uiState.collectAsState()
    var zoomScale by remember { mutableFloatStateOf(DEFAULT_ZOOM) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is EditAvatarNavigationEvent.UploadSuccess -> onChoose(imageUri, zoomScale)
            }
        }
    }

    UploadAvatarErrorDialog(
        isVisible = uiState.showUploadError,
        onCancel = { viewModel.dismissUploadError() },
        onTryAgain = { viewModel.uploadUserImage(imageUri) }
    )

    LoadingDialog(isShowing = uiState.isUploading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg.page)
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(scaleX = zoomScale, scaleY = zoomScale)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(0.82f)
                    .border(3.dp, colors.editAvatar.circleBorder, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.edit_avatar_hint),
            color = colors.editAvatar.hintText,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = stringResource(R.string.edit_avatar_zoom_out_desc),
                tint = colors.editAvatar.hintText,
                modifier = Modifier.size(18.dp)
            )
            Slider(
                value = zoomScale,
                onValueChange = { zoomScale = it },
                valueRange = MIN_ZOOM..MAX_ZOOM,
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(SLIDER_THUMB_SIZE)
                            .clip(CircleShape)
                            .background(colors.editAvatar.sliderThumb)
                    )
                },
                track = { sliderState ->
                    val fraction = ((sliderState.value - sliderState.valueRange.start) /
                        (sliderState.valueRange.endInclusive - sliderState.valueRange.start))
                        .coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(SLIDER_TRACK_HEIGHT)
                            .clip(RoundedCornerShape(SLIDER_TRACK_HEIGHT / 2))
                            .background(colors.editAvatar.sliderTrackBackground)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(SLIDER_TRACK_HEIGHT / 2))
                                .background(colors.editAvatar.sliderTrackFill)
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.edit_avatar_zoom_in_desc),
                tint = colors.editAvatar.hintText,
                modifier = Modifier.size(18.dp)
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = 40.dp, horizontal = 30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.edit_avatar_cancel),
                color = colors.editAvatar.cancelText,
                fontSize = 20.sp,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onCancel
                )
            )
            Text(
                text = stringResource(R.string.edit_avatar_choose),
                style = TextStyle(brush = colors.editAvatar.chooseText),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { viewModel.uploadUserImage(imageUri) }
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun EditAvatarScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        EditAvatarScreen(imageUri = Uri.EMPTY, onCancel = {}, onChoose = { _, _ -> })
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun EditAvatarScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        EditAvatarScreen(imageUri = Uri.EMPTY, onCancel = {}, onChoose = { _, _ -> })
    }
}
