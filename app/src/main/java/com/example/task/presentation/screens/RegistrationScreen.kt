package com.example.task.presentation.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.BigButton
import com.example.test.Black
import com.example.test.ButtonStyle
import com.example.test.Caption
import com.example.test.DefaultSelect
import com.example.test.InputBase
import com.example.test.R

@Composable
fun RegistrationScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val viewModel = remember { RegistrationViewModel(context) }

    if (viewModel.errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.errorMessage = "" },
            title = { Text(text = viewModel.errorMessage) },
            confirmButton = {}
        )
    }

    LazyColumn(
        modifier.padding(horizontal = 20.dp)
    ) {
        if (!viewModel.isPasswordMode) {
            item {
                Spacer(Modifier.height(32.dp))
                Text(
                    text = "Создание Профиля",
                    color = Black,
                    fontWeight = FontWeight.W700,
                    fontSize = 24.sp,
                )
                Spacer(Modifier.height(44.dp))
                Text(
                    text = "Без профиля вы не сможете создавать проекты.",
                    color = Caption,
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(32.dp))
                InputBase(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    text = "Имя",
                    textPlaceHolder = "Введите имя"
                )
                Spacer(Modifier.height(24.dp))
                InputBase(
                    value = viewModel.surname,
                    onValueChange = { viewModel.surname = it },
                    text = "Фамилия",
                    textPlaceHolder = "Введите фамилию",
                )
                Spacer(Modifier.height(24.dp))
                InputBase(
                    value = viewModel.fatherName,
                    onValueChange = { viewModel.fatherName = it },
                    text = "Отчество",
                    textPlaceHolder = "Введите отчество",
                )
                Spacer(Modifier.height(24.dp))
                InputBase(
                    value = viewModel.birthDay,
                    onValueChange = { viewModel.birthDay = it },
                    text = "Дата рождения",
                    textPlaceHolder = "--.--.----",
                )
                Spacer(Modifier.height(24.dp))
                DefaultSelect(
                    value = viewModel.gender,
                    onValueChange = { viewModel.gender = it },
                    placeholder = "Пол",
                    data = listOf("Мужской", "Женский", "Не указан")
                )
                Spacer(Modifier.height(24.dp))
                InputBase(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    text = "Почта",
                    textPlaceHolder = "example@mail.com",
                )
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(68.dp))
                    BigButton(
                        buttonStyle = if (viewModel.gender.isNotEmpty() && 
                            viewModel.birthDay.isNotEmpty() && 
                            viewModel.fatherName.isNotEmpty() && 
                            viewModel.surname.isNotEmpty() && 
                            viewModel.name.isNotEmpty() && 
                            viewModel.email.isNotEmpty()) ButtonStyle.Primary else ButtonStyle.Inactive,
                        text = "Создать",
                        onClick = { viewModel.nextStep() }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        } else {
            item {
                Spacer(modifier = Modifier.height(59.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Image(
                        painter = painterResource(R.drawable.home), 
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Создание пароля",
                        fontWeight = FontWeight.W700,
                        fontSize = 24.sp,
                        color = Black
                    )
                }
                Spacer(modifier = Modifier.height(23.dp))
                Text(
                    text = "Введите новый пароль",
                    fontWeight = FontWeight.W400,
                    fontSize = 15.sp,
                    color = Black
                )
                Spacer(modifier = Modifier.height(90.dp))
                InputBase(
                    text = "Новый Пароль",
                    textPlaceHolder = "Минимум 6 символов",
                    isPassword = true,
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it }
                )
                Spacer(modifier = Modifier.height(12.dp))
                InputBase(
                    text = "Повторите пароль",
                    textPlaceHolder = "Минимум 6 символов",
                    isPassword = true,
                    value = viewModel.passwordRepeat,
                    onValueChange = { viewModel.passwordRepeat = it }
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (viewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    BigButton(
                        buttonStyle = if (viewModel.passwordRepeat.isNotEmpty() && 
                            viewModel.password.isNotEmpty()) ButtonStyle.Primary else ButtonStyle.Inactive,
                        onClick = {
                            viewModel.register(context) {
                                navController.navigate("passwordApp")
                            }
                        },
                        text = "Далее"
                    )
                }
            }
        }
    }
}
