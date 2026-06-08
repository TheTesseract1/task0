package com.example.task.presentation.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.example.task.domain.GlanceWidgetUpdater
import com.example.task.domain.LoginUseCasesImpl
import com.example.task.domain.WidgetUpdater
import com.example.test.R
import com.example.test.*
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
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

    LazyColumn(
        Modifier.padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(59.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.drawable.hello),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Добро пожаловать!",
                    fontWeight = FontWeight.W700,
                    fontSize = 24.sp,
                    color = Black
                )
            }
            Spacer(modifier = Modifier.height(23.dp))
            Text(
                text = "Войдите, чтобы пользоваться функциями приложения",
                fontWeight = FontWeight.W400,
                fontSize = 15.sp,
                color = Black
            )
            Spacer(modifier = Modifier.height(64.dp))
            InputBase(
                text = "Вход по E-mail",
                textPlaceHolder = "example@mail.com",
                value = email,
                onValueChange = {
                    email = it
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            InputBase(
                text = "Пароль",
                textPlaceHolder = "Введите пароль",
                isPassword = true,
                value = password,
                onValueChange = {
                    password = it
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator()
            } else {
                BigButton(
                    Modifier,
                    if (email.isNotEmpty() && password.isNotEmpty()) ButtonStyle.Primary else ButtonStyle.Inactive,
                    {
                        val loginUseCase = LoginUseCasesImpl()
                        scope.launch {
                            loginUseCase.login(
                                email = email,
                                password = password,
                                context = context,
                                onLoading = {
                                    isLoading = it
                                },
                                onFailed = {
                                    errorMessage = it
                                },
                                onSuccess = {
                                    context.getSharedPreferences("newUsers", Context.MODE_PRIVATE).edit {
                                        remove("cards")
                                    }
                                    WidgetUpdater.update(context)
                                    scope.launch {
                                        GlanceWidgetUpdater.update(context)
                                    }
                                    navController.navigate("passwordApp")
                                }
                            )
                        }
                    },
                    "Далее"
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    color = Accent,
                    text = "Зарегестрироваться",
                    fontWeight = FontWeight.W400,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        context.getSharedPreferences("newUsers", Context.MODE_PRIVATE).edit{
                            putString("registration","123")
                        }
                        navController.navigate("registration")
                    }
                )
                Spacer(Modifier.height(30.dp))
                Text(
                    color = Accent,
                    text = "Пропустить авторизацию",
                    fontWeight = FontWeight.W400,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        context.getSharedPreferences("newUsers", Context.MODE_PRIVATE).edit{
                            putString("notToken","true")
                        }
                        navController.navigate("main")
                    }
                )
            }
            Spacer(modifier = Modifier.height(59.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    color = Placeholder,
                    text = "Или войдите с помощью",
                    fontWeight = FontWeight.W400,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LoginWithButton(
                modifier = Modifier,
                buttonStyle = ButtonStyle.LoginWidth,
                text = "Войти с VK",
                onClick = {},
                icon = painterResource(R.drawable.vkh4_logo)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoginWithButton(
                modifier = Modifier,
                buttonStyle = ButtonStyle.LoginWidth,
                text = "Войти с Yandex",
                onClick = {},
                icon = painterResource(R.drawable.icon_yandex)
            )
        }
    }
}