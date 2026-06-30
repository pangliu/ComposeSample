package com.example.newproject.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
annotation class ThemePreview

@Composable
fun PreviewThemeWrapper(
    colors: AppColors = LocalAppColors.current,
    content: @Composable () -> Unit
) {
    AppTheme(colors = colors) {
        content()
    }
}
