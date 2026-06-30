package com.example.newproject.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppColors = staticCompositionLocalOf { NeonColors }

@Composable
fun AppTheme(
    colors: AppColors = NeonColors,
    assets: AppAssets = NeonAssets,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppAssets provides assets,
    ) {
        content()
    }
}
