package com.qpay.xcash.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class RowIcon {
    data class Vector(val imageVector: ImageVector) : RowIcon()
    data class Resource(@DrawableRes val resId: Int) : RowIcon()
}

@Composable
fun RowIconImage(
    icon: RowIcon,
    tint: Color,
    size: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    when (icon) {
        is RowIcon.Vector -> Icon(
            imageVector = icon.imageVector,
            contentDescription = null,
            tint = tint,
            modifier = modifier.size(size)
        )
        is RowIcon.Resource -> Icon(
            painter = painterResource(icon.resId),
            contentDescription = null,
            tint = tint,
            modifier = modifier.size(size)
        )
    }
}
