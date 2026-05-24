package com.example.task.presentation.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.test.BottomBarData
import com.example.test.TabBar
import androidx.core.content.edit


@Composable
fun MainScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("newUsers", Context.MODE_PRIVATE) }
    val initialTab = remember {
        try {
            BottomBarData.valueOf(sharedPrefs.getString("bottomBarData", BottomBarData.HOME.name)!!)
        } catch (e: IllegalArgumentException) {
            BottomBarData.HOME
        }
    }
    var bottomBarDataVar by remember { mutableStateOf(initialTab) }

    TabBar(
        modifier = Modifier,
        bottomBarDataVar = bottomBarDataVar,
        onClick = {
            bottomBarDataVar = it
            sharedPrefs.edit { putString("bottomBarData", it.name) }
        },
    ) {
        when(bottomBarDataVar) {
            BottomBarData.HOME ->  {
                HomeScreen(navController)
            }
            BottomBarData.CATALOG -> {
                CatalogScreen(navController)

            }
            BottomBarData.PROJECT -> {
                ProjectScreen(navController)
            }
            BottomBarData.PROFILE -> {
                ProfileScreen(navController)
            }
        }
    }
}