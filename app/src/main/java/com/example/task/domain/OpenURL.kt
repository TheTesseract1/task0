package com.example.task.domain

import android.content.Context
import android.content.Intent
import android.net.Uri

object URLActions {

    fun openURL(context: Context, url: String, onError: () -> Unit) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            onError()
        }
    }
}
