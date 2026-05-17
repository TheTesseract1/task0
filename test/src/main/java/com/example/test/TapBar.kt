package com.example.test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

enum class BottomBarData(val icon: Int,val text: String) {
    HOME(icon = R.drawable.home, text = "Главная"),
    CATALOG(icon = R.drawable.catalog, text = "Каталог"),
    PROJECT(icon = R.drawable.projects, text = "Проекты"),
    PROFILE(icon = R.drawable.profile, text = "Профиль")
}

@Composable
fun TabBar(
    modifier: Modifier = Modifier,
    bottomBarDataVar: BottomBarData,
    onClick: (BottomBarData) -> Unit = {},
    content: @Composable () -> Unit
) {

    Scaffold(
        containerColor = White,
        bottomBar = {
            Column {
                HorizontalDivider(
                    color = Color(0x4DA0A0A0)
                )
                NavigationBar(
                    containerColor = White,
                    windowInsets = WindowInsets(0.dp)
                )
                {
                    BottomBarData.entries.forEach {
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = Accent,
                                selectedTextColor = Accent
                            ),
                            selected = bottomBarDataVar == it,
                            onClick = { onClick(it) },
                            icon = {
                                Icon(
                                    painter = painterResource(it.icon),
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(it.text)
                            }
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
            innerPadding ->
        Box(
            Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}