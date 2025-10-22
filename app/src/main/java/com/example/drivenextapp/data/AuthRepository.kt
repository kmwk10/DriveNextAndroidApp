package com.example.drivenextapp.data

import android.content.Context
import com.example.drivenextapp.util.PrefsManager

object AuthRepository {

    private var prefs: PrefsManager? = null

    fun init(context: Context) {
        prefs = PrefsManager(context)
        // Добавляем тестовых пользователей при первом запуске
        RegisterRepository.addTestUsers()

        // Пробуем восстановить текущего пользователя по токену
        val token = prefs?.getAccessToken()
        if (!token.isNullOrBlank()) {
            // если в users есть пользователь с таким email - устанавливаем его как currentUser
            RegisterRepository.findUserByEmail(token)?.let { user ->
                RegisterRepository.setCurrentUser(user)
            }
        }
    }

    fun login(email: String, password: String): Boolean {
        val success = RegisterRepository.loginUser(email, password)
        if (success) {
            // Сохраняем токен (пока email вместо токена)
            prefs?.saveAccessToken(email)
        }
        return success
    }

    fun logout() {
        prefs?.clearAccessToken()
        RegisterRepository.logout()
    }

    fun getCurrentUser(): RegisterData? {
        return RegisterRepository.currentUser
    }
}
