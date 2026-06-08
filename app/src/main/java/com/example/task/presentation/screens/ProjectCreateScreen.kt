package com.example.task.presentation.screens

import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.task.domain.NetworkRepository
import com.example.test.BigButton
import com.example.test.Black
import com.example.test.ButtonStyle
import com.example.test.DefaultSelect
import com.example.test.InputBase
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

@Composable
fun ProjectCreate(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var token by remember { mutableStateOf("") }

    var type by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var dateStart by remember { mutableStateOf("") }
    var dateEnd by remember { mutableStateOf("") }
    var to by remember { mutableStateOf("") }
    var from by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }
    var fileBytes by remember { mutableStateOf(byteArrayOf()) }
    var errorMessage by remember { mutableStateOf("") }


    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { errorMessage = "" },
            title = {
                Text(text = errorMessage)
            },
            confirmButton = {}
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val nameFile = File(it.path ?: "").name
            fileName = nameFile
            fileBytes = context.contentResolver.openInputStream(it)?.readBytes() ?: byteArrayOf()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            fileName = "photo_${System.currentTimeMillis()}.jpg"
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            fileBytes = stream.toByteArray()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        }
    }

    var showChoiceDialog by remember { mutableStateOf(false) }

    if (showChoiceDialog) {
        AlertDialog(
            onDismissRequest = { showChoiceDialog = false },
            title = {
                Text(text = "Выберите источник")
            },
            confirmButton = {
                TextButton(onClick = {
                    showChoiceDialog = false
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) { Text("Галерея") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showChoiceDialog = false
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }) { Text("Камера") }
            },
        )
    }

    LaunchedEffect(Unit) {
        token = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
            "token",
            null
        ) ?: ""
    }

    LazyColumn(modifier = modifier.padding(horizontal = 20.dp)) {
        item {
            Spacer(Modifier.height(28.dp))
            Text(
                text = "Создать проект",
                color = Black,
                fontWeight = FontWeight.W600,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(31.dp))
            DefaultSelect(
                value = type,
                onValueChange = {
                    type = it
                },
                text = "Тип",
                placeholder = "Выберите тип",
                data = listOf("Крутой проект", "Злой проект", "Не указан")
            )
            Spacer(Modifier.height(16.dp))
            InputBase(
                value = name,
                onValueChange = {
                    name = it
                },
                text = "Название проекта",
                textPlaceHolder = "Введите имя"
            )
            Spacer(Modifier.height(16.dp))
            InputBase(
                value = dateStart,
                onValueChange = {
                    dateStart = it
                },
                text = "Дата начала",
                textPlaceHolder = "--.--.----"
            )
            Spacer(Modifier.height(16.dp))
            InputBase(
                value = dateEnd,
                onValueChange = {
                    dateEnd = it
                },
                text = "Дата Окончания",
                textPlaceHolder = "--.--.----"
            )
            Spacer(Modifier.height(16.dp))
            DefaultSelect(
                value = to,
                onValueChange = {
                    to = it
                },
                text = "Кому",
                placeholder = "Выберите кому",
                data = listOf("Маме", "Папе", "Бабушке")
            )
            Spacer(Modifier.height(16.dp))
            InputBase(
                value = from,
                onValueChange = {
                    from = it
                },
                text = "Источник описания",
                textPlaceHolder = "example.com"
            )
            Spacer(Modifier.height(16.dp))
            DefaultSelect(
                value = category,
                onValueChange = {
                    category = it
                },
                text = "Категория",
                placeholder = "Выберите  категорию",
                data = listOf("Популярное", "Женщинам", "Мужчинам", "Детям", "Аксессуары")
            )
            Spacer(Modifier.height(32.dp))
            BigButton(
                buttonStyle = if (fileName.isEmpty() && fileBytes.isEmpty()) ButtonStyle.Primary else ButtonStyle.Inactive,
                onClick = {
                    showChoiceDialog = true
                },
                text = if (fileName.isEmpty() && fileBytes.isEmpty()) "Добавить файл" else "Файл добавлен, $fileName"
            )
            if (fileBytes.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = fileBytes,
                        contentDescription = "Превью",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            BigButton(
                buttonStyle = if (category.isNotEmpty()
                    && from.isNotEmpty()
                    && to.isNotEmpty()
                    && dateEnd.isNotEmpty()
                    && dateStart.isNotEmpty()
                    && name.isNotEmpty()
                    && type.isNotEmpty()
                    && fileName.isNotEmpty()
                    && fileBytes.isNotEmpty()
                ) {
                    ButtonStyle.Primary
                } else {
                    ButtonStyle.Inactive
                },
                onClick = {
                    scope.launch {
                        val success = NetworkRepository.projectsAdd(
                            name,
                            dateStart,
                            dateEnd,
                            from,
                            category,
                            fileBytes,
                            fileName,
                            token
                        )
                        if (success) {
                            errorMessage = "успешно"
                            navController.navigate("main")
                        } else {
                            errorMessage = "ошибка при отправке"
                        }
                    }
                },
                text = "Добавить проект",
            )
        }
    }
}
