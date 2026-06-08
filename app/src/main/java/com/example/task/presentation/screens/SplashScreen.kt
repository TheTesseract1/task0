package com.example.task.presentation.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test.Accent
import com.example.test.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val sharedPrefs = context.getSharedPreferences("newUsers", Context.MODE_PRIVATE)
        val registration = sharedPrefs.getString("registration", null)
        val token = sharedPrefs.getString("token", null)

        delay(1000L)
        if (registration == null) {
            navController.navigate("welcome")
        } else {
            if (token == null) {
                navController.navigate("registration")
            } else {
                navController.navigate("main")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Accent),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.home), // Placeholder for splash icon
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Color.White
        )
    }
}
