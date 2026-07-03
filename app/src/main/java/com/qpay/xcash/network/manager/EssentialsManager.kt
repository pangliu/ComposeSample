package com.qpay.xcash.network.manager

import android.content.Context
import android.content.SharedPreferences
import com.qpay.xcash.ui.home.essential.EssentialItem
import com.qpay.xcash.ui.home.essential.ESSENTIALS_DISPLAY_COUNT
import com.qpay.xcash.ui.home.essential.allEssentialItems
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EssentialsManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_MY_MENU = "my_menu_labels"
    }

    fun save(items: List<EssentialItem>) {
        prefs.edit()
            .putString(KEY_MY_MENU, items.joinToString(",") { it.label })
            .apply()
    }

    fun load(): List<EssentialItem> {
        val raw = prefs.getString(KEY_MY_MENU, null)
            ?: return allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT)
        return raw.split(",")
            .mapNotNull { label -> allEssentialItems.find { it.label == label } }
            .ifEmpty { allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT) }
    }
}
