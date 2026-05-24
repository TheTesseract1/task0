package com.example.task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.BigButton
import com.example.test.ButtonStyle
import com.example.test.InputBase
import com.example.test.MainCart

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTestScreen()
        }
    }
}

@Composable
fun MyTestScreen() {
    // Состояния для поля ввода (чтобы текст сохранялся при наборе)
    var loginValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Отступы между всеми элементами
    ) {
        // 1. Вызываем твоё поле ввода (Логин)
        InputBase(
            value = loginValue,
            onValueChange = { loginValue = it },
            text = "Ваш логин",
            textPlaceHolder = "Введите email",
            errorMessage = "" // Ошибки нет
        )

        // 2. Вызываем поле ввода пароля
        InputBase(
            value = passwordValue,
            onValueChange = { passwordValue = it },
            text = "Пароль",
            textPlaceHolder = "Минимум 6 символов",
            errorMessage = if (passwordValue.length < 6 && passwordValue.isNotEmpty()) "Пароль слишком короткий" else "",
            isPassword = true // Включаем режим глазика
        )

        // 3. Вызываем твою карточку товара (MainCart)
        MainCart(
            name = "Крутая рубашка",
            price = "1500",
            count = "1",
            onAdd = { /* тут логика +1 */ },
            onMinus = { /* тут логика -1 */ },
            onDelete = { /* тут логика удаления */ }
        )

        Spacer(modifier = Modifier.weight(1f)) // Занимает всё пустое место

        // 4. Вызываем твою кнопку (BigButton)
        BigButton(
            buttonStyle = ButtonStyle.Primary,
            text = "Подтвердить заказ",
            onClick = {
                println("Нажата кнопка! Логин: $loginValue")
            }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMyTestScreen() {
    MyTestScreen()
}