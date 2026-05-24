package com.example.task.domain

import android.content.Context
import androidx.core.content.edit

class CodeUseCaseImpl: CodeUseCase{
    override suspend fun checkCode(
        code: String,
        onFailed: (String) -> Unit,
        onSuccess: () -> Unit,
        onLoading: (Boolean) -> Unit,
        context: Context
    ) {
        try {
            onLoading(true)
            if (context.getSharedPreferences("newUsers",Context.MODE_PRIVATE).getString(
                    "code",
                    null
                ) == code) {
                onSuccess()
            } else {
                onFailed("Code didn't match")
            }
        } catch (e: Exception) {
            onFailed("Network error")
        } finally {
            onLoading(false)
        }
    }

    override suspend fun codeReg(
        code: String,
        onFailed: (String) -> Unit,
        onSuccess: () -> Unit,
        onLoading: (Boolean) -> Unit,
        context: Context
    ) {
        try {
            onLoading(true)
            if (code.length != 4) {
                onFailed("Code didn't valid")
            }
            context.getSharedPreferences("newUsers",Context.MODE_PRIVATE).edit{
                putString("code",code)
            }
            onSuccess()
        } catch (e: Exception) {
            onFailed("Network error")
        } finally {
            onLoading(false)
        }
    }
}