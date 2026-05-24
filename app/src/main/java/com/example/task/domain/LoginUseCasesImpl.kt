package com.example.task.domain

import android.content.Context
import com.example.task.data.AuthModel

class LoginUseCasesImpl : LoginUseCases {

    override suspend fun login(
        email: String,
        password: String,
        onFailed: (String) -> Unit,
        onSuccess: () -> Unit,
        onLoading: (Boolean) -> Unit,
        context: Context
    ) {
        onLoading(true)
        try {
            val response = NetworkRepository.login(AuthModel(email, password))
            if (response != null) {
                // Тут можно сохранить токен в LocalData или SharedPreferences
                onSuccess()
            } else {
                onFailed("Неверный логин или пароль")
            }
        } catch (e: Exception) {
            onFailed("Ошибка сети: ${e.message}")
        } finally {
            onLoading(false)
        }
    }
}
