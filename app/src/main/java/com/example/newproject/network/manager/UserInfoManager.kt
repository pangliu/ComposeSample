package com.example.newproject.network.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.newproject.network.model.response.UserInfoResponse
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoManager @Inject constructor(
    @ApplicationContext context: Context,
    moshi: Moshi
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val adapter = moshi.adapter(UserInfoResponse::class.java)

    companion object {
        private const val KEY_USER_INFO = "user_info_json"
    }

    private fun loadFromPrefs(): UserInfoResponse? {
        val json = prefs.getString(KEY_USER_INFO, null) ?: return null
        return try { adapter.fromJson(json) } catch (e: Exception) { null }
    }

    private val _userInfoFlow = MutableStateFlow(loadFromPrefs())
    val userInfoFlow: StateFlow<UserInfoResponse?> = _userInfoFlow.asStateFlow()

    fun save(userInfo: UserInfoResponse) {
        prefs.edit().putString(KEY_USER_INFO, adapter.toJson(userInfo)).apply()
        _userInfoFlow.value = userInfo
    }

    fun clear() {
        prefs.edit().remove(KEY_USER_INFO).apply()
        _userInfoFlow.value = null
    }
}
