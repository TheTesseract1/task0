package com.example.test


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultSelect(
    value: String,
    onValueChange: (String) -> Unit,
    data: List<String>,
    modifier: Modifier = Modifier,
    text: String = "",
    placeholder: String = "",
    needIcon: (@Composable () -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    var isOpenModal by remember { mutableStateOf(false) }
    if (isOpenModal) {
        BaseModal(
            onDismissRequest = {
                isOpenModal = false
            }
        ) {
            data.forEach{
                Text(
                    text = it,
                    Modifier.clickable {
                        onValueChange(it)
                        isOpenModal = false
                    }
                )
            }
        }
    }
    InputBase(
        value = value,
        onValueChange = {},
        enabled = false,
        textPlaceHolder = placeholder,
        trailingIcon = {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                Modifier.clickable {
                    isOpenModal = true
                }
            )
        },
        needIcon = needIcon,
        modifier = modifier,
        leadingIcon = leadingIcon,
        text = text
    )
}

@Preview
@Composable
private fun Prev() {
    var valueSelect by remember { mutableStateOf("") }
    Column {
        DefaultSelect(
            value = valueSelect,
            onValueChange = {valueSelect = it},
            data = listOf("12q3","12","dasdasda"),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            },
            needIcon = {
                Spacer(Modifier.width(21.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            },
            text = "1231231231231231"
        )
    }
}