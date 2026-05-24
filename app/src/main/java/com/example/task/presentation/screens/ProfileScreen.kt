package com.example.task.presentation.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity.MODE_PRIVATE
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.task.domain.NetworkRepository.deleteToken
import com.example.task.domain.NetworkRepository.userInfoGet
import com.example.task.domain.NetworkRepository.userTokensGet
import com.example.test.Accent
import com.example.test.Black
import com.example.test.Caption
import com.example.test.Error
import com.example.test.InputBg
import com.example.test.R
import com.example.test.White
import kotlinx.coroutines.launch
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.core.net.toUri
import com.example.task.domain.GlanceWidgetUpdater
import com.example.task.domain.URLActions
import com.example.task.domain.WidgetUpdater
import java.io.File


@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }

    var isNotifications by remember { mutableStateOf(true) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var isNotToken by remember { mutableStateOf(false) }

    val userPolicyOfConfidence = "https://avatars.mds.yandex.net/i?id=f0e5e2479006e0343b1e058f3032553a27b177e9-7555196-images-thumbs&n=13"

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
        isNotifications = if (context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
                "notifications",
                null
            ) != null) true else false
        isLoading = true
        try {
            val notT = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
                "notToken",
                null
            )
            isNotToken = if (notT == "true") true else false

            if (!isNotToken) {

                token = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
                    "token",
                    null
                )!!
                val info = userInfoGet(token)
                email = info!!.email
                name = info.middleName
                isLoading = false
            } else {
                name="Авторизуйтесь"
                email="чтобы видеть данные профиля"
                isLoading=false
            }
        } catch (e: Exception) {
            errorMessage = "Connection error"
            name = "Эдуард"
            email = "+7 967 078-58-37"
            isLoading = false
        }
    }

    LazyColumn(
        modifier.padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator()
            } else {
                Text(
                    text = name,
                    fontWeight = FontWeight.W700,
                    fontSize = 24.sp,
                    color = Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator()
            } else {
                Text(
                    text = email,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    color = Caption
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            RawContainer(R.drawable.note, "Мои заказы")
            Spacer(modifier = Modifier.height(32.dp))
            RawContainer(
                R.drawable.settings,
                "Уведомления",
                true,
                isNotifications,
            )
            { isNotifications = it
                if (it) {
                    context.getSharedPreferences("newUsers", MODE_PRIVATE).edit {
                        putString("notifications","true")
                    }
                } else {
                    context.getSharedPreferences("newUsers", MODE_PRIVATE).edit {
                        remove("notifications")
                    }
                }
            }
            Spacer(modifier = Modifier.height(194.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Политика конфиденциальности",
                    fontWeight = FontWeight.W500,
                    fontSize = 15.sp,
                    color = Caption,
                    modifier = Modifier.clickable {
                        URLActions.openURL(
                            context,
                            userPolicyOfConfidence,
                            { errorMessage = "Не удалось открыть URL" }
                        )
                    }

                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Пользовательское соглашение",
                    fontWeight = FontWeight.W500,
                    fontSize = 15.sp,
                    color = Caption,
                    modifier = Modifier.clickable {
                        try {
                            val inputStream = context.resources.openRawResource(com.example.test.R.raw.test)

                            val file = File(context.cacheDir, "test.pdf")
                            inputStream.use{ input ->
                                file.outputStream().use{
                                    input.copyTo(it)
                                }
                            }

                            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                            } else {
                                Uri.fromFile(file)
                            }

                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                if (!isNotToken) {
                    Text(
                        text = "Выход",
                        fontWeight = FontWeight.W500,
                        fontSize = 15.sp,
                        color = Error,
                        modifier = Modifier.clickable {
                            var correctIdTokenToDel = ""
                            scope.launch {
                                val tokens = userTokensGet(token)
                                if (tokens !== null) {
                                    tokens.forEach{
                                        if (it.token == token) {
                                            correctIdTokenToDel = it.id
                                        }
                                    }
                                }
                                deleteToken(correctIdTokenToDel,token)
                                navController.navigate("welcome")
                                context.getSharedPreferences("newUsers", Context.MODE_PRIVATE).edit {
                                    remove("token")
                                    remove("code")
                                    remove("notifications")
                                    remove("project_name")
                                    remove("project_days_past")
                                }
                                WidgetUpdater.update(context)
                                scope.launch {
                                    GlanceWidgetUpdater.update(context)
                                }
                            }
                        }
                    )
                } else {
                    Text(
                        text = "Войти в личный кабинет",
                        fontWeight = FontWeight.W500,
                        fontSize = 15.sp,
                        color = Accent,
                        modifier = Modifier.clickable {
                            scope.launch {
                                navController.navigate("welcome")
                                context.getSharedPreferences("newUsers", Context.MODE_PRIVATE).edit {
                                    remove("notToken")
                                }
                                WidgetUpdater.update(context)
                                scope.launch {
                                    GlanceWidgetUpdater.update(context)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun RawContainer(
    icon: Int,
    text: String,
    switcher: Boolean = false,
    isChecked: Boolean = false,
    onSwitcherChange: (Boolean) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(size = 32.dp),
                painter = painterResource(icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                fontWeight = FontWeight.W600,
                fontSize = 17.sp,
                color = Black
            )
        }
        if (switcher) {
            Switch(
                modifier = Modifier.width(48.dp)
                    .height(28.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = White,
                    uncheckedThumbColor = White,
                    uncheckedBorderColor = Color.Transparent,
                    checkedTrackColor = Accent,
                    uncheckedTrackColor = InputBG
                ),
                thumbContent = {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(Color.White, CircleShape)
                            .size(24.dp)
                    )
                },
                checked = isChecked,
                onCheckedChange = { v ->
                    onSwitcherChange(v)
                }
            )
        }
    }
}