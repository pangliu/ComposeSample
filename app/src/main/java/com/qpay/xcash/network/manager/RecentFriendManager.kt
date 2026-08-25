package com.qpay.xcash.network.manager

import android.content.Context
import android.content.SharedPreferences
import com.qpay.xcash.network.model.response.FriendResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_RECENT_FRIEND_COUNT = 3

@Singleton
class RecentFriendManager @Inject constructor(
    @ApplicationContext context: Context,
    moshi: Moshi
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val listType = Types.newParameterizedType(List::class.java, FriendResponse::class.java)
    private val adapter = moshi.adapter<List<FriendResponse>>(listType)

    companion object {
        private const val KEY_RECENT_FRIENDS = "recent_viewed_friends_json"
    }

    // 記錄最近查看的好友，最多保留 MAX_RECENT_FRIEND_COUNT 筆；已存在則移到最新，超過上限時覆蓋最舊的一筆
    fun save(friend: FriendResponse) {
        val updated = (load().filterNot { it.id == friend.id } + friend)
            .takeLast(MAX_RECENT_FRIEND_COUNT)
        prefs.edit().putString(KEY_RECENT_FRIENDS, adapter.toJson(updated)).apply()
    }

    fun load(): List<FriendResponse> {
        val json = prefs.getString(KEY_RECENT_FRIENDS, null) ?: return emptyList()
        return try {
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
