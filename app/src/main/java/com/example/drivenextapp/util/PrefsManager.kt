package com.example.drivenextapp.util

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "drive_next_prefs"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_ACCESS_TOKEN = "access_token"
    }

    // Проверка первого запуска
    fun isFirstLaunch(): Boolean {
        val firstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        if (firstLaunch) {
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
        }
        return firstLaunch
    }

    // Сохраняем access token
    fun saveAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    // Получаем access token
    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    // Проверка валидности токена
    fun isAccessTokenValid(): Boolean {
        val token = getAccessToken()
        // Здесь можно добавить реальную проверку, например, по сроку действия
        return !token.isNullOrEmpty()
    }

    // Удаляем токен (например, при выходе)
    fun clearAccessToken() {
        prefs.edit().remove(KEY_ACCESS_TOKEN).apply()
    }
}
