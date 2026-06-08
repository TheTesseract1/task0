package com.example.task.domain

import android.content.Context
import androidx.core.content.edit
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
                context.getSharedPreferences("newUsers", Context.MODE_PRIVATE).edit {
                    putString("token", response.token)
                }
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
