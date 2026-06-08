package com.example.task.presentation.screens

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.example.task.data.ProjectModel
import com.example.task.domain.GlanceWidgetUpdater
import com.example.task.domain.NetworkRepository
import com.example.task.domain.WidgetUpdater
import com.example.test.R
import com.example.test.Black
import com.example.test.ProjectsCard
import kotlinx.coroutines.launch

@Composable
fun ProjectScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val projects = remember { mutableListOf<ProjectModel>() }
    var token by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isNotToken by remember { mutableStateOf(false) }

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {errorMessage = ""},
            title ={
                Text(text = errorMessage)
            },
            confirmButton ={}
        )
    }

    LaunchedEffect(Unit) {
        isLoading = true
        val notT = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
            "notToken",
            null
        )
        isNotToken = notT == "true"
        if (!isNotToken) {
            token = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
                "token",
                null
            )!!
            val correctProjects = NetworkRepository.projectsGet(token)
            if (correctProjects != null) {
                projects.clear()
                projects.addAll(correctProjects)
            } else {
                errorMessage = "Не удалось загрузить проекты"
            }
            isLoading = false
        } else {
            isLoading = false
        }
    }

    LazyColumn(modifier = modifier.padding(horizontal = 20.dp)) {
        item{
            Spacer(Modifier.height(28.dp))
            Box(
                modifier =Modifier.fillMaxWidth()
            ) {
                Text(
                    "Проекты",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = Black,
                    modifier =  Modifier.align(Alignment.Center)
                )
                if (!isNotToken) {
                    Icon(
                        painter = painterResource(R.drawable.icon_plus),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                            .size(20.dp)
                            .clickable {
                                navController.navigate("projectCreate")
                            },
                        tint = Color.Unspecified
                    )
                }
            }
            Spacer(Modifier.height(36.dp))
        }
        if (isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (isNotToken) {
            item {
                Text(
                    "Войдите в свой профиль чтобы управлять проектами!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = Black
                )
            }
        } else {
            items(
                items = projects,
                key = {project -> project.id}
            ) { project ->
                ProjectsCard(
                    name = project.name,
                    modifier = Modifier,
                    daysPast = project.startDate
                ) {
                    context.getSharedPreferences("newUsers", MODE_PRIVATE).edit {
                        putString("project_name",project.name)
                        putString("project_days_past",project.startDate)
                    }

                    scope.launch {
                        GlanceWidgetUpdater.update(context)
                    }

                    WidgetUpdater.update(context)

                    navController.navigate("projectShowScreen/${project.id}")
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
