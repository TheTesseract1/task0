package com.example.task

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.edit
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.task.domain.NotificationReceiver
import com.example.task.presentation.screens.CardPage
import com.example.task.presentation.screens.MainScreen
import com.example.task.presentation.screens.PasswordScreen
import com.example.task.presentation.screens.ProjectCreate
import com.example.task.presentation.screens.ProjectsShowScreen
import com.example.task.presentation.screens.RegistrationScreen
import com.example.task.presentation.screens.SplashScreen
import com.example.task.presentation.screens.WelcomeScreen
import com.example.task.presentation.ui.theme.TaskTheme
import com.example.test.White


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            TaskTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = White)
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "splash_screen"
                        ) {

                            composable(route = "welcome") {
                                WelcomeScreen(navController)
                            }

                            composable("main") {
                                MainScreen(navController)
                            }

                            composable("passwordApp") {
                                PasswordScreen(navController)
                            }

                            composable("cardPage") {
                                CardPage(navController)
                            }

                            composable("projectCreate") {
                                ProjectCreate(navController)
                            }

                            composable(
                                "projectShowScreen/{projectId}",
                                listOf(navArgument("projectId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val projectId = backStackEntry.arguments?.getString("projectId")
                                if (projectId != null) {
                                    ProjectsShowScreen(navController, projectId)
                                }
                            }

                            composable("registration") {
                                RegistrationScreen(navController)
                            }

                            composable("splash_screen") {
                                SplashScreen(navController)
                            }
                        }
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "main",
                "Напоминания",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onStop() {
        super.onStop()

        val sharedPrefs = getSharedPreferences("newUsers", MODE_PRIVATE)
        val isNotifications = sharedPrefs.getString("notifications", null) != null
        val projectName = sharedPrefs.getString("project_name", null)

        if (isNotifications) {
            val intent = Intent(this, NotificationReceiver::class.java).apply {
                putExtra(NotificationReceiver.PROJECT, projectName)
            }
            val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val time = System.currentTimeMillis() + 3000

            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            getSharedPreferences("newUsers", MODE_PRIVATE).edit {
                remove("project_name")
                remove("project_days_past")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val isNotifications = getSharedPreferences("newUsers", MODE_PRIVATE).getString(
            "notifications", null
        ) != null
        if (isFinishing && isNotifications) {
            val intent = Intent(this, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val time = System.currentTimeMillis() + 1000

            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
    }
}
