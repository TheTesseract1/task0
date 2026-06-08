package com.example.task.domain

import android.content.Context
import com.example.task.data.RegisterModel

class RegUseCaseImpl: RegUseCase {

    private val EMPTY_FIELDS = "Заполните все поля"
    private val INVALID_EMAIL = "Некорректный email"
    private val PASSWORD_DIDNT_MATCH = "Пароли не совпадают"

    override suspend fun reg(
        email: String,
        name: String,
        fatherName: String,
        surname: String,
        birthDay: String,
        gender: String,
        password1: String,
        password2: String,
        onFailed: (String) -> Unit,
        onSuccess: () -> Unit,
        onLoading: (Boolean) -> Unit,
        context: Context
    ) {
        try {
            onLoading(true)
            if (email.isEmpty() || name.isEmpty() || fatherName.isEmpty() || surname.isEmpty()
                || birthDay.isEmpty() || gender.isEmpty()
            ) {
                onFailed(EMPTY_FIELDS)
                return
            }

            if (!validateEmail(email)) {
                onFailed(INVALID_EMAIL)
                return
            }

            if (password1 != password2) {
                onFailed(PASSWORD_DIDNT_MATCH)
                return
            }

            val isSuccess = NetworkRepository.reg(
                RegisterModel(
                    email,
                    password1
                )
            )
            if (isSuccess) {
                onSuccess()
            } else {
                onFailed("Ошибка регистрации")
            }

        } catch (e: Exception) {
            onFailed("Ошибка сети: ${e.message}")
        } finally {
            onLoading(false)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
