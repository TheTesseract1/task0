package com.example.task.presentation.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.core.content.edit
import androidx.navigation.NavController
import com.example.task.domain.CodeUseCaseImpl
import com.example.test.Accent
import com.example.test.Black
import com.example.test.InputBg
import com.example.test.R
import com.example.test.White
import kotlinx.coroutines.delay

@Composable
fun PasswordScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var code by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val isNotInit = context.getSharedPreferences("newUsers", Context.MODE_PRIVATE).getString(
        "code",
        null
    ) != null

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {errorMessage = ""},
            title ={
                Text(text = errorMessage)
            },
            confirmButton ={}
        )
    }

    LaunchedEffect(code) {
        if (code.length == 4) {
            val codeUseCase = CodeUseCaseImpl()
            if (isNotInit) {
                codeUseCase.checkCode(
                    code,
                    {
                        errorMessage = it
                    },
                    {
                        navController.navigate("main")
                    },
                    {
                        isLoading = it
                    },
                    context
                )
            } else {
                codeUseCase.codeReg(
                    code,
                    {
                        errorMessage = it
                    },
                    {
                        navController.navigate("main")
                    },
                    {
                        isLoading = it
                    },
                    context
                )
            }
        }
    }

    LazyColumn(modifier.fillMaxSize()
        .padding(horizontal = 20.dp))
    {
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (!isNotInit) {"Пропустить"} else {""},
                    color = Accent,
                    fontWeight = FontWeight.W400,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (!isNotInit) {"Создайте пароль"} else {"Введите пароль"},
                    color = Black,
                    fontWeight = FontWeight.W700,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (!isNotInit) {"Для защиты персональных данных"} else {""},
                    color = Color(0xFF939396),
                    fontWeight = FontWeight.W400,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(56.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4) { a ->
                    CirclePassword(
                        isNotEmpty = code.length <= a
                    )

                    Spacer(Modifier.width(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            for (a in 0..8 step 3) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    repeat(3) { b ->
                        CircleNum((a + (b + 1)).toString(), onClick = {
                            code = addText(code = code, text = (a + (b + 1)).toString())
                        })
                        if (b != 2)
                            Spacer(Modifier.width(24.dp))
                    }
                }

                if (a != 8)
                    Spacer(modifier = Modifier.height(24.dp))
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircleNum("0", isInvisible = true, onClick = {})
                Spacer(Modifier.width(24.dp))
                CircleNum("0", onClick = {
                    code = addText(code = code, text = "0")
                })
                Spacer(Modifier.width(24.dp))
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (code.isNotEmpty()) code = code.dropLast(1)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_delete),
                            contentDescription = null,
                            Modifier
                                .width(35.dp)
                                .height(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CirclePassword(isNotEmpty: Boolean = true){
    Box(
        Modifier
            .border(
                shape = CircleShape,
                color = Accent,
                width = 1.dp
            )
            .background(
                color = if (isNotEmpty) White else Accent,
                shape = CircleShape
            )
            .size(16.dp)
    ){
    }
}

@Composable
fun CircleNum(text: String, isInvisible: Boolean = false, onClick: () -> Unit) {
    var isClicked by remember { mutableStateOf(false) }
    LaunchedEffect(isClicked) {
        if (isClicked) {
            delay(100L)
            isClicked = false
        }
    }
    Box(
        Modifier
            .background(
                color = if (isInvisible) Color.Transparent else if (isClicked) Accent else InputBg,
                shape = CircleShape
            )
            .size(80.dp)
            .clickable { onClick()
                isClicked = true
            }
    ){
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = if (isInvisible) Color.Transparent else if (isClicked) White else Black,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp
            )
        }
    }
}

fun addText(code: String, text: String): String {
    var tempCode = code
    tempCode += text
    return tempCode
}