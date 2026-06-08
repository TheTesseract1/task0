package com.example.task.data

import android.content.Context
import androidx.core.content.edit

class PreferenceManager(context: Context) {
    private val sharedPrefs = context.getSharedPreferences("newUsers", Context.MODE_PRIVATE)

    var token: String?
        get() = sharedPrefs.getString("token", null)
        set(value) = sharedPrefs.edit { putString("token", value) }

    var notifications: Boolean
        get() = sharedPrefs.getString("notifications", null) != null
        set(value) = sharedPrefs.edit {
            if (value) putString("notifications", "true") else remove("notifications")
        }

    var notToken: Boolean
        get() = sharedPrefs.getString("notToken", null) == "true"
        set(value) = sharedPrefs.edit { putString("notToken", value.toString()) }

    var code: String?
        get() = sharedPrefs.getString("code", null)
        set(value) = sharedPrefs.edit { putString("code", value) }

    fun clearAuth() {
        sharedPrefs.edit {
            remove("token")
            remove("code")
            remove("notifications")
            remove("project_name")
            remove("project_days_past")
        }
    }
}
