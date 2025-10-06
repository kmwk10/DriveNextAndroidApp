package com.example.drivenextapp.util

import android.util.Patterns

object ValidationUtils {
    fun isEmailValid(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    // Минимум 8 символов и хотя бы одна цифра.
    fun isPasswordStrong(password: String): Boolean {
        if (password.length < 8) return false
        return password.any { it.isDigit() }
    }

    fun arePasswordsMatching(password: String, confirm: String): Boolean =
        password == confirm
}
