package com.example.newproject.network.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.newproject.ui.theme.AppColors
import com.example.newproject.ui.theme.BlackGoldColors
import com.example.newproject.ui.theme.NeonColors
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "app_theme"
        private const val THEME_NEON = "neon"
        private const val THEME_BLACK_GOLD = "black_gold"
    }

    fun save(colors: AppColors) {
        val value = if (colors === BlackGoldColors) THEME_BLACK_GOLD else THEME_NEON
        prefs.edit().putString(KEY_THEME, value).apply()
    }

    fun load(): AppColors {
        return when (prefs.getString(KEY_THEME, THEME_BLACK_GOLD)) {
            THEME_BLACK_GOLD -> BlackGoldColors
            else -> NeonColors
        }
    }
}
