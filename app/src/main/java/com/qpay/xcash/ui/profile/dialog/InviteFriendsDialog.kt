package com.qpay.xcash.ui.profile.dialog

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.RowIcon
import com.qpay.xcash.ui.components.RowIconImage
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.LocalAppColors
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrColors
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import com.qpay.xcash.ui.theme.neonPurpleLight

private val DialogBg = Color(0xFF0D1B2E)
private val qrCodeUrl = "http://xcash.io/pay?account=hank_001&to=hank&name=hank+liu"
@Composable
fun InviteFriendsDialog(
    onDismiss: () -> Unit,
    onShareWhatsApp: () -> Unit = {},
    onShareTelegram: () -> Unit = {},
    onShareMessenger: () -> Unit = {},
    onSystemShare: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Dialog card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .neonGlow(colors.accent.primary, alpha = 0.35f, glowRadius = 12.dp, borderRadius = 20.dp)
                    .background(DialogBg, RoundedCornerShape(20.dp))
                    .border(1.5.dp, colors.accent.primary.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title
                Text(
                    text = stringResource(R.string.invite_dialog_title),
                    color = colors.accent.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // QR Code
                val qrPainter = rememberQrCodePainter(
                    data = qrCodeUrl,
                    shapes = QrShapes(
                        ball = QrBallShape.circle(),
                        darkPixel = QrPixelShape.roundCorners(),
                        frame = QrFrameShape.roundCorners(.25f)
                    ),
                    colors = QrColors(
                        dark = QrBrush.solid(colors.accent.primary),
                        light = QrBrush.solid(Color.Transparent)
                    )
                )
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .border(2.dp, colors.accent.secondary.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = qrPainter,
                        contentDescription = stringResource(R.string.invite_dialog_qr_desc),
                        modifier = Modifier.size(120.dp)
                    )
                }

                Text(
                    text = stringResource(R.string.invite_dialog_scan_to_invite),
                    color = colors.text.body,
                    fontSize = 12.sp
                )

                HorizontalDivider(color = colors.accent.primary.copy(alpha = 0.2f))

                // Share via messengers
                Text(
                    text = stringResource(R.string.invite_dialog_share_link),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RowIconImage(
                        icon = RowIcon.Resource(R.mipmap.ic_invite_wc),
                        tint = Color.Unspecified,
                        size = 36.dp,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onShareWhatsApp
                        )
                    )
                    RowIconImage(
                        icon = RowIcon.Resource(R.mipmap.ic_invite_tg),
                        tint = Color.Unspecified,
                        size = 54.dp,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onShareTelegram
                        )
                    )
                    RowIconImage(
                        icon = RowIcon.Resource(R.mipmap.ic_invite_msg),
                        tint = Color.Unspecified,
                        size = 36.dp,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onShareMessenger
                        )
                    )
                }

                HorizontalDivider(color = colors.accent.primary.copy(alpha = 0.2f))

                // System share button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onSystemShare
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.invite_dialog_system_share),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.size(8.dp))
                    RowIconImage(
//                        imageVector = Icons.Default.Share,
//                        contentDescription = null,
                        icon = RowIcon.Resource(R.mipmap.ic_invite_share),
                        tint = Color.Unspecified,
                        modifier = Modifier.size(25.dp)
                    )
                }

                // Tagline
                Text(
                    text = stringResource(R.string.invite_dialog_tagline),
                    color = colors.accent.secondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // Close button outside the card
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.5.dp, neonPurpleLight.copy(alpha = 1f), CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onDismiss
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.invite_dialog_close_desc),
                    tint = neonPurpleLight,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun InviteFriendsDialogPreview() {
    InviteFriendsDialog(onDismiss = {})
}
