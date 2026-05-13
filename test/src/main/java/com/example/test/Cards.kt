package com.example.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CardDefault(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        shape = AbsoluteRoundedCornerShape(12.dp), //задали скругление
        colors = CardDefaults.cardColors(
            containerColor = White, //задали белый фон контейнера
        ),
        modifier = modifier
            .shadow(  //тенюшка
                elevation = 10.dp,
                shape = AbsoluteRoundedCornerShape(12.dp),
                spotColor = Color(0x99E4E8F5), //цвета направленной тени
                ambientColor = Color(0x99E4E8F5) //рассеянной
            )
            .border(
                //тонкая рамка
                border = BorderStroke(1.dp, Color(0xFFF4F4F4)),
                shape = AbsoluteRoundedCornerShape(12.dp),
            )
    ){
        Column(
            Modifier.padding((16.dp))
        ){
            content() //вызов функции, которая передаст контент внутрь
        }
    }
}

@Composable
fun MainCart(
    modifier: Modifier = Modifier,
    name: String,
    price: String,
    count: String,
    onAdd: () -> Unit = {},  //переменная в которой лежит действие, добавить
    onMinus: () -> Unit = {},  //убрать, минус одна единица короч
    onDelete: () -> Unit = {}  //удалить
    ){
    CardDefault(
        modifier = modifier
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = name,
                color = Black,
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Icon(
               painter = painterResource(R.drawable.icon_close) ,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onDelete()
                    }
                    .size(20.dp),
                tint = Description
            )
        }
        Spacer(Modifier.height(34.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) { Text(
            text = "$price ₽",
            color = Black,
            fontSize = 17.sp,
            fontWeight = FontWeight.W400
        )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$count штук",
                    color = Black,
                    fontSize = 15.sp
                )
            }
           Spacer(Modifier.width(42.dp))
            Row(
                modifier = Modifier
                    .clip(
                        shape = AbsoluteRoundedCornerShape(8.dp)
                    )
                    .background(
                        color = InputBg,
                        shape = AbsoluteRoundedCornerShape(8.dp)
                    )
            ) {
                Row(
                    Modifier.padding(6.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_minus),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onMinus()
                            },
                        tint = IconsColor
                    )
                    Spacer(Modifier.width(6.dp))
                    VerticalDivider(
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .height(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        painter = painterResource(R.drawable.icon_plus),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onAdd },
                        tint = Caption
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectsCard(
    modifier: Modifier,
    name: String,
    daysPast: String,
    onClick: () -> Unit = {}
){
    CardDefault(
        modifier = modifier
    ) {
        Text(
            text = name,
            color = Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(36.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "Прошло $daysPast дня",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            SmallButton(
                modifier = Modifier,
                buttonStyle = ButtonStyle.Primary,
                text = "Открыть",
                onClick = onClick
            )
        }
    }
}

@Composable
fun PrimaryCard(
    modifier: Modifier,
    name: String,
    category: String,
    price: String,
    isInCard: Boolean = false,
    onClickButton: () -> Unit = {}
    ) {
        CardDefault(
            modifier = modifier
        ) {
            Text(
                text = name,
                color = Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600
            )
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column{
                    Text(
                        text = category,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        color = Caption
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "$price ₽",
                        color = Black,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W600
                    )
                }
                Spacer(Modifier.width(90.dp))
                SmallButton(
                    buttonStyle = if (isInCard) ButtonStyle.Secondary else ButtonStyle.Primary,
                    onClick = onClickButton,
                    text = if (isInCard) "Убрать" else "Добавить",
                    modifier = Modifier
                )
        }
    }
}

