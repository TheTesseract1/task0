package com.example.task.domain

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState

object GlanceWidgetUpdater {
    suspend fun update(context: Context) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(GlanceWidget::class.java)

        val userPrefs = context.getSharedPreferences("newUsers", Context.MODE_PRIVATE)
        val token = userPrefs.getString("token", null)
        val projectName = userPrefs.getString("project_name",  null)
        val projectData = userPrefs.getString("project_days_past", null)

        glanceIds.forEach{glanceId ->
            updateAppWidgetState(context,glanceId) { prefs ->
                prefs[WidgetStateKeys.token] = token ?: ""
                prefs[WidgetStateKeys.projectName] = projectName ?: ""
                prefs[WidgetStateKeys.projectData] = projectData ?: ""
            }
            GlanceWidget().update(context, glanceId)
        }

    }
}