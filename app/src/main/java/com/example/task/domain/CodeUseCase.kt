package com.example.task.domain

import android.content.Context

interface CodeUseCase {
    suspend fun codeReg(
        code: String,
        onFailed: (String) -> Unit,
        onSuccess: () -> Unit,
        onLoading: (Boolean) -> Unit,
        context: Context
    )
    suspend fun checkCode(
        code: String,
        onFailed: (String) -> Unit,
        onSuccess: () -> Unit,
        onLoading: (Boolean) -> Unit,
        context: Context
    )
}