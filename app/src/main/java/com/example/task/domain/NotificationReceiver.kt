package com.example.task.domain

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver: BroadcastReceiver() {

    companion object {
        const val PROJECT = "project_name"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val projectName = intent.getStringExtra(PROJECT)

        val notificationText = if (projectName != null) {
            "Возвращайтесь в проект '$projectName'!"
        } else {
            "Возвращайтесь!"
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }

        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, "main")
            .setSmallIcon(
                com.example.test.R.drawable.eye_off_an_inner_journey___iconsvg_co,
            )
            .setContentTitle("Вы давно не заходили!")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        NotificationManagerCompat.from(context).notify(199,builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Main Channel"
            val descriptionText = "Channel for main notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("main", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}