package com.qpay.xcash.network

import com.qpay.xcash.network.manager.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager // 🎯 注入剛剛做好的 TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 🌟 動態從 SharedPreferences 中取出最新存好的 Access Token
        val token = tokenManager.getAccessToken()

        // 如果目前本地端沒有存 Token (例如尚未登入)，就原封不動送出請求
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        // 把 Token 塞入 Header 中
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}
