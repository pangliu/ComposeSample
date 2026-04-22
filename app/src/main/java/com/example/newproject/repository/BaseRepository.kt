package com.example.newproject.repository

import com.example.newproject.network.SessionManager
import com.example.newproject.network.model.BaseResponse
import com.example.newproject.network.model.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository(
    private val sessionManager: SessionManager // 🎯 傳入 SessionManager
) {

    /**
     * 包裝所有 API 呼叫，統一在 IO Thread 執行、解析 BaseResponse，並攔截 Error Code
     */
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> BaseResponse<T>
    ): NetworkResult<T> {
        // 強制切換到 IO 執行緒，避免阻塞 Main Thread (UI 執行緒)
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccess()) {
                    NetworkResult.Success(response.result)
                } else {
                    // 🚨 統一攔截 Error Code
                    handleGlobalError(response.code, response.errorMsg)
                    NetworkResult.Error(response.code, response.errorMsg)
                }
            } catch (e: java.lang.Exception) {
                // 攔截網路異常 (例如無網路、Timeout、JSON 解析失敗等)
                e.printStackTrace()
                NetworkResult.Exception(e)
            }
        }
    }

    /**
     * 集中處理特定的錯誤代碼
     */
    private fun handleGlobalError(code: Int, message: String) {
        when (code) {
            401, 1001, 1005 -> {
                // 🎯 只要遇到這三個代碼，就對全局發射 Logout 事件
                println("Global Error: 觸發登出機制 ($code: $message)")
                sessionManager.triggerLogout()
            }
            else -> {
                println("Global Error: Code=$code, Message=$message")
            }
        }
    }
}
