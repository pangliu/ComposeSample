package com.qpay.xcash.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.ui.theme.neonCyan
import com.qpay.xcash.ui.theme.neonPurple
import com.qpay.xcash.ui.theme.darkSlate

private val TrackWidth  = 58.dp
private val TrackHeight = 28.dp
private val ThumbSize   = 22.dp
private val ThumbPad    = 3.dp

@Composable
fun NeonSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = neonPurple,
    showLabel: Boolean = false
) {
    if (showLabel) {
        NeonLabeledSwitch(checked, onCheckedChange, activeColor)
    } else {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = activeColor,
                checkedTrackColor = activeColor.copy(0.3f),
                checkedBorderColor = activeColor.copy(alpha = 0.7f),
                uncheckedThumbColor = activeColor.copy(alpha = 0.2f),
                uncheckedTrackColor = activeColor.copy(alpha = 0.1f),
                uncheckedBorderColor = activeColor.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
private fun NeonLabeledSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color
) {
    // thumb 從左端 padding 滑到右端
    val thumbTravel = TrackWidth - ThumbSize - ThumbPad * 2
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) thumbTravel else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "thumbOffset"
    )

    Box(
        modifier = Modifier
            .width(TrackWidth)
            .height(TrackHeight)
            .clip(CircleShape)
            .background(if (checked) activeColor.copy(alpha = 0.25f) else activeColor.copy(alpha = 0.08f))
            .border(
                width = 1.5.dp,
                color = if (checked) activeColor.copy(alpha = 0.7f) else activeColor.copy(alpha = 0.25f),
                shape = CircleShape
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onCheckedChange(!checked) }
    ) {
        // ON / OFF 文字顯示在 thumb 的另一側
        Text(
            text = if (checked) "ON" else "OFF",
            color = if (checked) activeColor else activeColor.copy(alpha = 0.5f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(if (checked) Alignment.CenterStart else Alignment.CenterEnd)
                .padding(horizontal = 7.dp)
        )

        // Thumb（滑動的圓形）
        Box(
            modifier = Modifier
                .padding(ThumbPad)
                .offset(x = thumbOffset)
                .size(ThumbSize)
                .clip(CircleShape)
                .background(if (checked) activeColor else activeColor.copy(alpha = 0.3f))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun NeonSwitchPreview() {
    var checkedPurple by remember { mutableStateOf(true) }
    var checkedCyan   by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .background(darkSlate)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NeonSwitch(checked = checkedPurple, onCheckedChange = { checkedPurple = it }, activeColor = neonPurple, showLabel = true)
        NeonSwitch(checked = checkedCyan,   onCheckedChange = { checkedCyan = it },   activeColor = neonCyan,   showLabel = true)
    }
}
