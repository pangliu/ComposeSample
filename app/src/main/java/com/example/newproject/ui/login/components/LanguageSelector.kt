package com.example.newproject.ui.login.components

import android.graphics.BlurMaskFilter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.AppTheme
import com.example.newproject.ui.theme.BlackGoldColors
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.darkBackground
import com.example.newproject.ui.theme.neonCyanLight
import com.example.newproject.ui.theme.neonPurple
import androidx.compose.runtime.withFrameNanos
import kotlinx.coroutines.delay

private const val ANIM_ENTER_MS = 200
private const val ANIM_EXIT_MS = 150

@Composable
fun LanguageSelector(
    selectedLanguage: String,
    languages: List<String>,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    var expanded by remember { mutableStateOf(false) }
    var popupVisible by remember { mutableStateOf(false) }
    // AnimatedVisibility 用這個 state 控制動畫，與 popupVisible 分離
    // 開啟時：先讓 Popup 以 false 渲染一幀，再切 true 觸發 enter 動畫
    var animatedVisible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val triggerHeightPx = with(density) { 40.dp.roundToPx() }

    LaunchedEffect(expanded) {
        if (expanded) {
            popupVisible = true
            withFrameNanos {} // 等一幀讓 Popup 先以 false 組合，下一幀才觸發 enter 動畫
            animatedVisible = true
        } else {
            animatedVisible = false
            delay(ANIM_EXIT_MS.toLong())
            popupVisible = false
        }
    }

    Box(modifier = modifier) {
        // ── 觸發列：ic_global + 目前語言 ──
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(40.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded = !expanded }
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                painter = painterResource(R.mipmap.ic_global),
                contentDescription = stringResource(R.string.language_desc),
                tint = colors.accent.secondary,
                modifier = Modifier
                    .size(20.dp)
                    .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.accent.secondary, alpha = 0.8f, glowRadius = 15.dp, borderRadius = 10.dp) else Modifier)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = selectedLanguage,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        // ── 浮動下拉選單 ──
        // Popup 獨立於 layout tree，不影響 LoginScreen 的排版
        if (popupVisible) {
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(0, triggerHeightPx),
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                AnimatedVisibility(
                    visible = animatedVisible,
                    enter = expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = tween(ANIM_ENTER_MS)
                    ) + fadeIn(animationSpec = tween(ANIM_ENTER_MS)),
                    exit = shrinkVertically(
                        shrinkTowards = Alignment.Top,
                        animationSpec = tween(ANIM_EXIT_MS)
                    ) + fadeOut(animationSpec = tween(ANIM_EXIT_MS))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(72.dp)
                            .then(if (colors.effect.enableGlow) Modifier.neonGlow(
                                color = colors.selector.border,
                                alpha = 0.65f,
                                glowRadius = 18.dp,
                                borderRadius = 8.dp,
                                blurStyle = BlurMaskFilter.Blur.OUTER) else Modifier)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Transparent)
                            .border(1.5.dp, colors.selector.border, RoundedCornerShape(8.dp))
                            .padding(vertical = 6.dp, horizontal = 6.dp)
                    ) {
                        languages.forEach { lang ->
                            val isSelected = lang == selectedLanguage
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) colors.selector.selectedBackground else Color.Transparent
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        onLanguageSelected(lang)
                                        expanded = false
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = lang,
                                    color = if (isSelected) Color.White else colors.selector.border,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "Collapsed Neon", showBackground = true, backgroundColor = 0xFF0E1422)
@Composable
fun LanguageSelectorCollapsedPreview() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            LanguageSelector(
                selectedLanguage = "EN",
                languages = listOf("EN", "CN", "JP", "AU"),
                onLanguageSelected = {}
            )
        }
    }
}

@Preview(name = "Collapsed Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
fun LanguageSelectorCollapsedBlackGoldPreview() {
    AppTheme(colors = BlackGoldColors) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            LanguageSelector(
                selectedLanguage = "EN",
                languages = listOf("EN", "CN", "JP", "AU"),
                onLanguageSelected = {}
            )
        }
    }
}

// Popup 無法在 Preview 中渲染，以靜態方式呈現展開外觀
@Preview(name = "Expanded Neon", showBackground = true, backgroundColor = 0xFF0E1422)
@Composable
fun LanguageSelectorExpandedPreview() {
    val languages = listOf("EN", "CN", "JP", "AU")
    AppTheme {
        val colors = LocalAppColors.current
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(40.dp).padding(horizontal = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.mipmap.ic_global),
                        contentDescription = null,
                        tint = neonPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("EN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(72.dp)
                        .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.selector.border, alpha = 0.65f, glowRadius = 18.dp, borderRadius = 20.dp) else Modifier)
                        .clip(RoundedCornerShape(5.dp))
                        .background(darkBackground)
                        .border(1.5.dp, colors.selector.border, RoundedCornerShape(8.dp))
                        .padding(vertical = 0.dp, horizontal = 50.dp)
                ) {
                    languages.forEach { lang ->
                        val isSelected = lang == "EN"
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) colors.selector.selectedBackground else Color.Transparent
                                )
                                .padding(vertical = 5.dp)
                        ) {
                            Text(
                                text = lang,
                                color = if (isSelected) Color.White else colors.selector.border,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Expanded Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
fun LanguageSelectorExpandedBlackGoldPreview() {
    val languages = listOf("EN", "CN", "JP", "AU")
    AppTheme(colors = BlackGoldColors) {
        val colors = LocalAppColors.current
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(40.dp).padding(horizontal = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.mipmap.ic_global),
                        contentDescription = null,
                        tint = colors.accent.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("EN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(72.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(darkBackground)
                        .border(1.5.dp, colors.selector.border, RoundedCornerShape(8.dp))
                        .padding(vertical = 0.dp, horizontal = 50.dp)
                ) {
                    languages.forEach { lang ->
                        val isSelected = lang == "EN"
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) colors.selector.selectedBackground else Color.Transparent
                                )
                                .padding(vertical = 5.dp)
                        ) {
                            Text(
                                text = lang,
                                color = if (isSelected) Color.White else colors.selector.border,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
