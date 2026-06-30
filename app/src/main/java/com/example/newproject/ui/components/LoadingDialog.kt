package com.example.newproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.alpha
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.darkBackground

@Composable
fun LoadingDialog(
    isShowing: Boolean,
    onDismissRequest: () -> Unit = {}
) {
    if (isShowing) {
        Popup(
            onDismissRequest = { onDismissRequest() },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            LoadingDialogContent()
        }
    }
}

@Composable
fun LoadingDialogContent() {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .alpha(0.8f)
            .fillMaxSize()
            .background(
                color = darkBackground
            )
            // Intercept all touches
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = colors.bg.page,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            CircularProgressIndicator(
                color = colors.accent.secondary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun LoadingDialogPreview() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadingDialogContent()
        }
    }
}
