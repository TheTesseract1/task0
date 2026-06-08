package com.example.task.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.task.domain.URLActions
import com.example.test.Accent
import com.example.test.Black
import com.example.test.Caption
import com.example.test.Error
import com.example.test.InputBg
import com.example.test.R
import com.example.test.White


@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // In a real project with DI, we would use hiltViewModel() or similar.
    val viewModel = remember { ProfileViewModel(context) }

    val userPolicyOfConfidence = "https://avatars.mds.yandex.net/i?id=f0e5e2479006e0343b1e058f3032553a27b177e9-7555196-images-thumbs&n=13"

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
        item {
            Spacer(modifier = Modifier.height(12.dp))
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = viewModel.name,
                    fontWeight = FontWeight.W700,
                    fontSize = 24.sp,
                    color = Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = viewModel.email,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    color = Caption
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            RawContainer(R.drawable.icon_file_text, "Мои заказы")
            Spacer(modifier = Modifier.height(32.dp))
            RawContainer(
                R.drawable.ic_settings,
                "Уведомления",
                true,
                viewModel.isNotifications,
            ) {
                viewModel.toggleNotifications(it)
            }
            Spacer(modifier = Modifier.height(194.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Политика конфиденциальности",
                    fontWeight = FontWeight.W500,
                    fontSize = 15.sp,
                    color = Caption,
                    modifier = Modifier.clickable {
                        URLActions.openURL(context, userPolicyOfConfidence) {
                            viewModel.errorMessage = "Не удалось открыть URL"
                        }
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Пользовательское соглашение",
                    fontWeight = FontWeight.W500,
                    fontSize = 15.sp,
                    color = Caption,
                    modifier = Modifier.clickable {
                        // TODO: Implement PDF opening logic
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                if (!viewModel.isNotToken) {
                    Text(
                        text = "Выход",
                        fontWeight = FontWeight.W500,
                        fontSize = 15.sp,
                        color = Error,
                        modifier = Modifier.clickable {
                            viewModel.logout(context) {
                                navController.navigate("welcome")
                            }
                        }
                    )
                } else {
                    Text(
                        text = "Войти в личный кабинет",
                        fontWeight = FontWeight.W500,
                        fontSize = 15.sp,
                        color = Accent,
                        modifier = Modifier.clickable {
                            viewModel.enterAsUser(context) {
                                navController.navigate("welcome")
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun RawContainer(
    icon: Int,
    text: String,
    switcher: Boolean = false,
    isChecked: Boolean = false,
    onSwitcherChange: (Boolean) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(size = 32.dp),
                painter = painterResource(icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                fontWeight = FontWeight.W600,
                fontSize = 17.sp,
                color = Black
            )
        }
        if (switcher) {
            Switch(
                modifier = Modifier.width(48.dp)
                    .height(28.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = White,
                    uncheckedThumbColor = White,
                    uncheckedBorderColor = Color.Transparent,
                    checkedTrackColor = Accent,
                    uncheckedTrackColor = InputBg
                ),
                thumbContent = {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(Color.White, CircleShape)
                            .size(24.dp)
                    )
                },
                checked = isChecked,
                onCheckedChange = { v ->
                    onSwitcherChange(v)
                }
            )
        }
    }
}
