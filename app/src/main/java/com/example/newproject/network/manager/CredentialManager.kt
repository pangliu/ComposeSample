package com.example.newproject.network.manager

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs by lazy {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            "secure_credential_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    companion object {
        private const val KEY_PHONE = "biometric_phone"
        private const val KEY_PASSWORD = "biometric_password"
    }

    fun saveCredentials(phone: String, password: String) {
        prefs.edit().putString(KEY_PHONE, phone).putString(KEY_PASSWORD, password).apply()
    }

    fun getPhone(): String? = prefs.getString(KEY_PHONE, null)
    fun getPassword(): String? = prefs.getString(KEY_PASSWORD, null)
    fun hasCredentials(): Boolean = getPhone() != null && getPassword() != null

    fun clearCredentials() {
        prefs.edit().clear().apply()
    }
}
