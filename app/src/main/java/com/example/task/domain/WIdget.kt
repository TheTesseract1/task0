package com.example.task.domain

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.task.MainActivity
import com.example.task.R

class Widget: AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val preference = context.getSharedPreferences("newUsers", Context.MODE_PRIVATE)
            val token = preference.getString(
                "token",
                null
            )

            val projectName = preference.getString(
                "project_name",
                null
            )

            val projectDaysPast = preference.getString(
                "project_days_past",
                null
            )

            val views = RemoteViews(context.packageName, R.layout.widget)

            if (token != null) {
                if (projectName != null) {
                    views.setTextViewText(R.id.widget_name, projectName)
                    if (projectDaysPast != null) {
                        views.setTextViewText(R.id.widget_data, projectDaysPast)
                    } else {
                        views.setTextViewText(R.id.widget_data, "Неизвестно")
                    }
                } else {
                    views.setTextViewText(R.id.widget_name, "Зайдите")
                    views.setTextViewText(R.id.widget_data, "в любой проект")
                }
            } else {
                views.setTextViewText(R.id.widget_name, "Войдите")
                views.setTextViewText(R.id.widget_data, "")
            }

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,appWidgetId,intent, PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

    }

}