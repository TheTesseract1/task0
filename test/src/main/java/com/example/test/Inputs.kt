package com.example.test

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun InputBase(
    modifier: Modifier = Modifier, //позволяет настраивать размеры и отступы снаружи
    value: String,                  //текст введенный в поле
    onValueChange: (String) -> Unit, //функция, срабатывающая при наборе текста
    text: String,                   //заголовок над полем (Label)
    textPlaceHolder: String,        //подсказка внутри пустого поля
    errorMessage: String = "",           //текст ошибки
    isPassword: Boolean = false,        //флаг: является ли поле паролем
    leadingIcon: @Composable (()-> Unit)? = null,  //иконка в начале поля (внутри)
    trailingIcon: @Composable (()-> Unit)? = null,  //иконка в конце поля (внутри)
    needIcon: @Composable (()-> Unit)? = null,   //иконка снаружи поля (справа)
    enabled: Boolean = true             //активно ли поле для ввода
    ) {
    //запомнит, нажат ли глазик
    var isClicked by remember { mutableStateOf(false) }
    //запоминает, находится ли сейчас курсор в этом поле
    var isFocused by remember { mutableStateOf(false) }

    Column {
        //рисуем текст когда он есть
        if (text.isNotEmpty()) {
            Text(
                text = text,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Description
            )
            Spacer(modifier = Modifier.height(4.dp)) //отступ под заголовком
        }
        Row(
            //выравнивает поле и внешнюю иконку по центру
            verticalAlignment = Alignment.CenterVertically
        ) {
            //****ПОЛЕ ВВОДА
            TextField(
                modifier = modifier
                    .onFocusChanged {
                        isFocused = it.isFocused //обновляем состояние фокуса
                    }
                    .fillMaxWidth(
                        if (needIcon != null) 0.8f else 1f  //если справа есть внешняя иконка, поле займет 80% ширины
                    )
                    .border(
                        width = 1.dp,
                        //**логика цвета рамки
                        color = if (errorMessage.isNotEmpty()) Error  //если есть ошибка - красная
                        else if (isFocused) Accent                      //если в фокусе - синяя
                        else if (value.isNotEmpty() && enabled) IconsColor  //если заполнен - серый
                        else InputStoke,                                //иначе стандартный
                        shape = RoundedCornerShape(10.dp)
                    ),
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                //***подсказка
                placeholder = {
                    Column {
                        Text(
                            text = textPlaceHolder,
                            color = Caption,
                            fontWeight = FontWeight.W400,
                            fontSize = 16.sp
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp),
                //***цвета поля
                colors = TextFieldDefaults.colors(
                    //если ошибка - делаем фон бледно-красным, иначе белый
                    focusedContainerColor = if (errorMessage.isNotEmpty()) Color(0x1AFD3535) else InputBg,
                    unfocusedContainerColor = if (errorMessage.isNotEmpty()) Color(0x1AFD3535) else InputBg,
                    //убираем стандартную полоску внизу, так как мы рисуем свою рамку через .border()
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledContainerColor = if (errorMessage.isNotEmpty()) Color(0x1AFD3535) else InputBg,
                    disabledTextColor = Black,
                    cursorColor = Accent //цвет мигающий палочки
                ),
                leadingIcon = leadingIcon,
                trailingIcon = {
                    trailingIcon?.let { it() } //рисуем иконку, если она передана
                    if (isPassword) { //если это пароль, добавляем кнопку глазка
                        IconButton(onClick = { isClicked = !isClicked }) {
                            Icon(
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                //меняем картинку глазка в зависимости от состояния isClicked
                                painter = if (!isClicked) painterResource(R.drawable.group_1)
                                else painterResource(R.drawable.eye_off_an_inner_journey___iconsvg_co)
                            )
                        }
                    }
                },
                // если пароль и "глаз" не нажат — рисуем точки вместо букв, иначе — обычный текст
                visualTransformation = if (isPassword && !isClicked)
                    PasswordVisualTransformation('*')
                else VisualTransformation.None,
            )
            //если передали needIcon, рисуем её в той же строке (Row)
            needIcon?.let {
                needIcon()
            }
        }
        //блок ошибки под полем
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp)) //отступ перед полем ошибки
            Text(
                text = errorMessage,
                color = Error,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp
            )
        }
    }
}
