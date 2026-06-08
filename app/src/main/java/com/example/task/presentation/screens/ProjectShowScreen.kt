package com.example.task.presentation.screens

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.task.data.ProjectModel
import com.example.task.domain.NetworkRepository
import com.example.test.R
import com.example.test.Black
import com.example.test.Caption

@Composable
fun ProjectsShowScreen(
    navController: NavController,
    projectId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var project by remember { mutableStateOf<ProjectModel?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { errorMessage = "" },
            title = { Text(text = errorMessage) },
            confirmButton = {}
        )
    }

    LaunchedEffect(projectId) {
        isLoading = true
        val token = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString("token", null)
        if (token != null) {
            val result = NetworkRepository.getProjectById(projectId.toInt(), token)
            if (result != null) {
                project = result
            } else {
                errorMessage = "Не удалось загрузить проект"
            }
        }
        isLoading = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            project?.let { proj ->
                LazyColumn(modifier = Modifier.padding(horizontal = 20.dp)) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        Icon(
                            painter = painterResource(R.drawable.icon_chevron_left),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).clickable { navController.popBackStack() },
                            tint = Color.Unspecified
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            text = proj.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W700,
                            color = Black
                        )
                        Spacer(Modifier.height(24.dp))
                        Row {
                            Text(text = "Категория: ", color = Caption, fontWeight = FontWeight.W400)
                            Text(text = proj.category, color = Black, fontWeight = FontWeight.W500)
                        }
                        Spacer(Modifier.height(8.dp))
                        Row {
                            Text(text = "Период: ", color = Caption, fontWeight = FontWeight.W400)
                            Text(text = "${proj.startDate} - ${proj.endDate}", color = Black, fontWeight = FontWeight.W500)
                        }
                        Spacer(Modifier.height(24.dp))
                        Text(text = "Описание:", color = Caption, fontWeight = FontWeight.W400)
                        Spacer(Modifier.height(8.dp))
                        Text(text = proj.descriptionSource, color = Black, fontWeight = FontWeight.W400)
                    }
                }
            }
        }
    }
}
