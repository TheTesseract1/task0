package com.example.task.domain

import android.content.Context

interface RegUseCase {

    suspend fun reg(
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
    )
}