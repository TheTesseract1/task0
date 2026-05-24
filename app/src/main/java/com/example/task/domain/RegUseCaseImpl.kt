package com.example.task.domain

import android.content.Context
import com.example.task.data.AuthModel
import com.example.task.data.RegisterModel

class RegUseCaseImpl: RegUseCase {

    private val EMPTY_FIELDS = "Заполните все поля"
    private val INVALID_EMAIL = "Invalid Email"
    private val PASSWORD_DIDNT_MATCH = "Пароли не соответствуют"

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
                || birthDay.isEmpty() || gender.isEmpty() || email.isEmpty()
            ) {
                onFailed(EMPTY_FIELDS)
                return
            }

            if (!validateEmail(email)) {
                onFailed(INVALID_EMAIL)
                return
            }

            if (!validatePassword(password1)) {
                onFailed("Invalid password")
                return
            }

            if (password1 != password2) {
                onFailed(PASSWORD_DIDNT_MATCH)
            }

            val response = NetworkRepository.reg(
                RegisterModel(
                    email,
                    password1
                )
            )
            if (response != false) {
                onSuccess()
            }

        } catch (e: Exception) {
            onFailed("Network error")
        } finally {
            onLoading(false)
        }
    }

    fun validateEmail(email: String): Boolean {
        if (!email.any{ it == '@'}) return false
        if (!email.any{ it == '.'}) return false
        if (email.all{it.isDigit() || it.isLowerCase() || it == '.' || it == '@'})
            return   true
        return false
    }

    fun validatePassword(password: String): Boolean {
        if (password.length < 8) return false
        if (!password.any{it.isDigit()}) return false
        if (password.any{it == ' '}) return false
        if (!password.any{it.isLowerCase()}) return false
        if (!password.any{it.isUpperCase()}) return false
        if (!password.any{it.isLetterOrDigit()}) return false
        return true
    }
}