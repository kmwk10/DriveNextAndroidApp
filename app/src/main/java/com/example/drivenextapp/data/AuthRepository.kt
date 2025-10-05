package com.example.drivenextapp.data

object AuthRepository {
    fun checkCredentials(email: String, password: String): Boolean {
        // Пока тестовые данные
        return email == "test@example.com" && password == "123456"

        // В будущем здесь будет обращение к серверу или БД
    }
}