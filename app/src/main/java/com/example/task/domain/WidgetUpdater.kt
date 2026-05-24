package com.example.task.domain

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

object WidgetUpdater {

    fun update(context: Context) {
        val intent = Intent(context, Widget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

        intent.putExtra("random", System.currentTimeMillis())
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context,Widget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        context.sendBroadcast(intent)
    }
}