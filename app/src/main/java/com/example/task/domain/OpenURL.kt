package com.example.task.domain

import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

object URLActions {

    fun openURL(context: Context,url: String, onError: () -> Unit ) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = url.toUri()
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            onError()
        }
    }
}