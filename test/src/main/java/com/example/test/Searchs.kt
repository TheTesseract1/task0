package com.example.test

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

//пишем поисковую строку по уже сделанной заготовки InputBase,
// короч делаем специализированный компонент из универсального компонента inputBase

@Composable
fun SearchDefault(
    value: String,                   //текущий текст поиска
    onValueChange: (String) -> Unit, //функция, которая вызывается при вводе букв
    modifier: Modifier = Modifier,   //Настройки размеров/отступов
    text: String = "",              //Заголовок над поиском (по умолчанию пустой)
    errorMessage: String = "",      //Сообщение об ошибке
    isPassword: Boolean = false,    //Нужно ли скрывать текст (обычно для поиска - false)
    trailingIcon: @Composable (() -> Unit)? = null, // Иконка в конце (внутри поля)
    needIcon: (@Composable () -> Unit)? = null,     //Иконка снаружи поля
) {
    InputBase(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        text = text,
        textPlaceHolder = "Искать описание", // ЖЕСТКО ЗАДАНО: подсказка для поиска
        errorMessage = errorMessage,
        isPassword = isPassword,
        leadingIcon = { // ЖЕСТКО ЗАДАНО: всегда рисуем лупу в начале
            Icon(
                painter = painterResource(R.drawable.icon_search),
                contentDescription = null
            )
        },
        trailingIcon = trailingIcon,
        enabled = true,  // Поиск всегда активен по умолчанию
        needIcon = needIcon
    )
}

@Preview
@Composable
private fun Prev() {
    var value by remember { mutableStateOf("") }

    SearchDefault(
        value = value,
        onValueChange = {
            value = it
        }
    )
}