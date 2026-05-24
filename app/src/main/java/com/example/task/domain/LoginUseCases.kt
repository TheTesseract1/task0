package com.example.task.domain

import android.content.Context

interface LoginUseCases {

    suspend fun login(
        email: String,
        password: String,
        onFailed: (String) -> Unit,
        onSuccess: () -> Unit,
        onLoading: (Boolean) -> Unit,
        context: Context
    )
}