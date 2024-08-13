package com.example.studysync.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPrefManager(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_shared_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // Store JWT token
    fun storeToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    // Retrieve JWT token
    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    // Clear token
    fun clearToken() {
        sharedPreferences.edit().remove("jwt_token").apply()
    }
}
