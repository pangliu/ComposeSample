package com.qpay.xcash.utils

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricHelper {

    fun isAvailable(context: Context): Boolean {
        val manager = BiometricManager.from(context)
        val result = manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        Log.d("BiometricHelper", "canAuthenticate(BIOMETRIC_WEAK) = $result")

        if (result == BiometricManager.BIOMETRIC_SUCCESS) return true

        @Suppress("DEPRECATION")
        val fm = context.getSystemService(Context.FINGERPRINT_SERVICE) as? FingerprintManager
        val hwDetected = fm?.isHardwareDetected == true
        val enrolled = fm?.hasEnrolledFingerprints() == true
        Log.d("BiometricHelper", "FingerprintManager: hwDetected=$hwDetected, enrolled=$enrolled")

        return hwDetected && enrolled
    }

    fun showPrompt(
        activity: FragmentActivity,
        title: String,
        subtitle: String,
        negativeText: String = "",
        // true 時允許在沒有生物辨識（或辨識失敗）時，退回手機系統鎖屏 PIN/圖案/密碼驗證
        allowDeviceCredential: Boolean = false,
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                    errorCode != BiometricPrompt.ERROR_USER_CANCELED
                ) {
                    onError(errString.toString())
                }
            }
            override fun onAuthenticationFailed() {}
        }

        val authenticators = if (allowDeviceCredential) {
            BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        } else {
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        }

        val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(authenticators)

        // Android 規定 DEVICE_CREDENTIAL 不能跟 setNegativeButtonText 同時設定（退回系統驗證本身就取代了取消鈕）
        if (!allowDeviceCredential) {
            promptInfoBuilder.setNegativeButtonText(negativeText)
        }

        BiometricPrompt(activity, ContextCompat.getMainExecutor(activity), callback)
            .authenticate(promptInfoBuilder.build())
    }
}
