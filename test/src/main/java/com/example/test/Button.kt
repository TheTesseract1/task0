package com.example.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//внешний вид кнопки (что должно быть)
enum class ButtonStyle(
    val backgroundColor: Color, //фон
    val borderColor: Color,  //рамка
    val isDisabled: Boolean //можно ли нажать
){ //уже готовые кнопки
    Primary(Accent, Accent, false), //фон, рамки нет так что тот же цвет, можно нажать
    Inactive(AccentInactive, AccentInactive, true), //фон, рамки нет, нельзя нажать
    Secondary(White, Accent, false), //фон, рамка, можно нажать
    Tetriary(InputBg, InputBg, false)  //фон, нет рамки, можно нажать
}

//цвет в зависимости
fun textColor(
    buttonStyle: ButtonStyle
): Color{
    return when(buttonStyle){
        ButtonStyle.Primary -> White
        ButtonStyle.Inactive -> White
        ButtonStyle.Secondary -> Accent
        ButtonStyle.Tetriary -> Black
        else -> White
    }
}

//базовая кнопка
@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle,
    onClick: () -> Unit,
    padding: PaddingValues = PaddingValues(vertical = 16.dp),
    shape: Shape = AbsoluteRoundedCornerShape(10.dp), //скругление по дефолту
    enabled: Boolean = true, //нажата ли
    content: @Composable RowScope.() -> Unit,  //нужен для вызова Text() или Image() внутри контекста Row
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        content = content,
        contentPadding = padding, //настройка внутренних отступов
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonStyle.backgroundColor, //цвет нажимаблной кнопки
            disabledContainerColor = buttonStyle.backgroundColor //цвет ненажимаблной кнопки
        ),
        border = BorderStroke(
            width = 1.dp,
            color = buttonStyle.borderColor
        ),
        enabled = enabled
    )
}

@Composable
fun BigButton(
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle,
    onClick: () -> Unit,
    text: String
) {
    val textColor = textColor(buttonStyle)
     BaseButton(
         modifier
             .height(56.dp)
             .width(335.dp),
         buttonStyle,
         onClick,
         padding = PaddingValues(horizontal = 115.dp)
     ) {
         Text(
             color = textColor,
             text = text,
             fontSize = 17.sp,
             fontWeight = FontWeight.W600
         )
     }
}
@Composable
fun SmallButton(
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle,
    onClick: () -> Unit,
    text: String
) {
    val textColor = textColor(buttonStyle)
    BaseButton(
        modifier
            .width(96.dp)
            .height(40.dp),
        buttonStyle,
        onClick,
        padding = PaddingValues(vertical = 10.dp, horizontal = 24.dp)
    ){
        Text(
            color = textColor,
            text = text,
            fontWeight = FontWeight.W600,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ChipsButton(
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle,
    onClick: () -> Unit,
    text: String
) {
    val textColor = textColor(buttonStyle)
    BaseButton(
        modifier
            .width(129.dp)
            .height(48.dp),
        buttonStyle,
        onClick,
        padding = PaddingValues(vertical = 14.dp, horizontal = 20.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.W500
        )
    }
}

@Composable
fun CardButton(
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle,
    onClick: () -> Unit,
    text: String) {
    BaseButton(
        modifier
            .height(56.dp)
            .width(335.dp),
        buttonStyle,
        onClick,
        padding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {
       Row(
           modifier = Modifier.fillMaxSize(),
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically
       ){
           Row(
               verticalAlignment = Alignment.CenterVertically
           ) {
               Icon(
                   painter = painterResource(R.drawable.)

               )
           }
       }

    }
}

@Preview
@Composable
private fun ButtonPrev() {
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(13.dp),) {

    }
}