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
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    text: String,
    textPlaceHolder: String,
    errorMessage: String,
    isPassword: Boolean = false,
    leadingIcon: @Composable (()-> Unit)? = null,
    trailingIcon: @Composable (()-> Unit)? = null,
    needIcon: @Composable (()-> Unit)? = null,
    enabled: Boolean = true
    ) {
    var isClicked by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    if (text.isEmpty()){
        Text(
            text = text,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            color = Description
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = modifier.onFocusChanged{
                isFocused = it.isFocused
            }.fillMaxWidth(
                if(needIcon != null) 0.8f else 1f
            ).border(
                width = 1.dp,
                color = if (errorMessage.isNotEmpty()) Error
                else if (isFocused) Accent
                else if (value.isNotEmpty() && enabled) IconsColor
                else InputStoke,
                shape = RoundedCornerShape(10.dp)
            ),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
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
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (errorMessage.isNotEmpty()) Color(0x1AFD3535) else InputBg,
                unfocusedContainerColor = if (errorMessage.isNotEmpty()) Color(0x1AFD3535) else InputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledContainerColor = if (errorMessage.isNotEmpty()) Color(0x1AFD3535) else InputBg,
                disabledTextColor = Black,
                cursorColor = Accent
            ),
            leadingIcon = leadingIcon,
            trailingIcon = {
                trailingIcon?.let { it() }
                if (isPassword){
//                    IconButton(onClick = (onClick{} isClicked = !isClicked)){
//                        Icon(
//                            contentDescription = null,
//                            modifier = Modifier.size(20.dp),
//                            painter = if (!isClicked) painterResource(R.drawable.group_1)
//                            else painterResource(R.drawable.eye_off_an_inner_journey___iconsvg_co)
//                        )
//                    }
                }
            },
            visualTransformation = if (isPassword && !isClicked) PasswordVisualTransformation('*') else VisualTransformation.None,
        )
        needIcon?.let{
            needIcon()
        }
    }
    if (errorMessage.isNotEmpty()){
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage,
            color = Error,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp
        )
    }
}