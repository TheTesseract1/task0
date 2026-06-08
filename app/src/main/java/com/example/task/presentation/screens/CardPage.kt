package com.example.task.presentation.screens

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.task.data.CartModel
import com.example.task.domain.NetworkRepository
import com.example.test.BigButton
import com.example.test.Black
import com.example.test.ButtonStyle
import com.example.test.MainCart
import com.example.test.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

@Composable
fun CardPage(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var token by remember { mutableStateOf("") }
    val cardsInCard = remember { mutableStateListOf<CartModel>() }
    var totalPrice by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }

    var isNotToken by remember { mutableStateOf(false) }

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { errorMessage = "" },
            title = {
                Text(text = errorMessage)
            },
            confirmButton = {}
        )
    }

    LaunchedEffect(Unit) {
        val notT = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
            "notToken",
            null
        )
        isNotToken = notT == "true"
        if (!isNotToken) {
            token = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
                "token",
                null
            ) ?: ""
            if (token.isNotEmpty()) {
                val actuallyCardsInCard = NetworkRepository.getCard(token)
                if (actuallyCardsInCard != null) {
                    cardsInCard.clear()
                    cardsInCard.addAll(actuallyCardsInCard)
                }
            }
        } else {
            val tempCards = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
                "cards",
                null
            )
            if (tempCards != null) {
                val gson = Gson()
                val tempCardsList = gson.fromJson<List<CartModel>>(tempCards, object : TypeToken<List<CartModel>>() {}.type)
                if (tempCardsList != null) {
                    cardsInCard.clear()
                    cardsInCard.addAll(tempCardsList)
                }
            }
        }
        var sum = 0
        for (card in cardsInCard) {
            sum += card.product.price * card.quantity
        }
        totalPrice = sum
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
                Icon(
                    painter = painterResource(R.drawable.icon_chevron_left),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                        .clickable {
                            navController.navigate("main")
                        },
                    tint = Color.Unspecified
                )
                Spacer(Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Корзина",
                        fontWeight = FontWeight.W700,
                        fontSize = 24.sp,
                        color = Black
                    )
                    Icon(
                        painter = painterResource(R.drawable.icon_delete),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                            .clickable {
                                if (!isNotToken) {
                                    cardsInCard.toList().forEach { card ->
                                        scope.launch {
                                            val isSuccess =
                                                NetworkRepository.deleteCardById(card.id, token)
                                            if (isSuccess) {
                                                cardsInCard.remove(card)
                                                totalPrice -= card.product.price * card.quantity
                                            } else {
                                                errorMessage = "Ошибка при удалении товара"
                                            }
                                        }
                                    }
                                } else {
                                    val gson = Gson()
                                    val trueTempCards = listOf<CartModel>()
                                    val gsonTrueTempCards = gson.toJson(trueTempCards)
                                    context.getSharedPreferences("newUsers", MODE_PRIVATE).edit {
                                        putString("cards", gsonTrueTempCards)
                                    }
                                    cardsInCard.clear()
                                    totalPrice = 0
                                }
                            },
                        tint = Color.Unspecified
                    )
                }
                Spacer(Modifier.height(32.dp))

            }
            items(
                items = cardsInCard,
                key = { card -> card.id }
            ) { card ->
                var count by remember { mutableIntStateOf(card.quantity) }
                var isLoadingInner by remember { mutableStateOf(false) }

                MainCart(
                    name = card.product.name,
                    price = card.product.price.toString(),
                    count = if (!isLoadingInner) { count.toString() } else { "..." },
                    onAdd = {
                        scope.launch {
                            isLoadingInner = true
                            val isSuccess = if (!isNotToken) {
                                NetworkRepository.patchCardById(card.id, count + 1, token)
                            } else {
                                try {
                                    val sharedPrefs = context.getSharedPreferences("newUsers", MODE_PRIVATE)
                                    val tempCards = sharedPrefs.getString("cards", null)
                                    val gson = Gson()
                                    val tempCardsList = gson.fromJson<List<CartModel>>(
                                        tempCards,
                                        object : TypeToken<List<CartModel>>() {}.type
                                    ) ?: listOf()

                                    val tempCardsListMutable = tempCardsList.toMutableList()
                                    val indexToUpdate = tempCardsListMutable.indexOfFirst { it.id == card.id }

                                    if (indexToUpdate != -1) {
                                        val itemToUpdate = tempCardsListMutable[indexToUpdate]
                                        tempCardsListMutable[indexToUpdate] = itemToUpdate.copy(quantity = count + 1)
                                    }

                                    val gsonTrueTempCards = gson.toJson(tempCardsListMutable)
                                    sharedPrefs.edit { putString("cards", gsonTrueTempCards) }
                                    true
                                } catch (e: Exception) {
                                    errorMessage = e.toString()
                                    false
                                }
                            }
                            if (isSuccess) {
                                totalPrice += card.product.price
                                count += 1
                            } else {
                                errorMessage = "Ошибка при добавлении товара"
                            }
                            isLoadingInner = false
                        }
                    },
                    onMinus = {
                        if (count > 1) {
                            scope.launch {
                                isLoadingInner = true
                                val isSuccess = if (!isNotToken) {
                                    NetworkRepository.patchCardById(card.id, count - 1, token)
                                } else {
                                    try {
                                        val sharedPrefs = context.getSharedPreferences("newUsers", MODE_PRIVATE)
                                        val tempCards = sharedPrefs.getString("cards", null)
                                        val gson = Gson()
                                        val tempCardsList = gson.fromJson<List<CartModel>>(
                                            tempCards,
                                            object : TypeToken<List<CartModel>>() {}.type
                                        ) ?: listOf()

                                        val tempCardsListMutable = tempCardsList.toMutableList()
                                        val indexToUpdate = tempCardsListMutable.indexOfFirst { it.id == card.id }

                                        if (indexToUpdate != -1) {
                                            val itemToUpdate = tempCardsListMutable[indexToUpdate]
                                            tempCardsListMutable[indexToUpdate] = itemToUpdate.copy(quantity = count - 1)
                                        }

                                        val gsonTrueTempCards = gson.toJson(tempCardsListMutable)
                                        sharedPrefs.edit { putString("cards", gsonTrueTempCards) }
                                        true
                                    } catch (e: Exception) {
                                        errorMessage = e.toString()
                                        false
                                    }
                                }
                                if (isSuccess) {
                                    totalPrice -= card.product.price
                                    count -= 1
                                } else {
                                    errorMessage = "Ошибка при уменьшении количества товара"
                                }
                                isLoadingInner = false
                            }
                        }
                    },
                    onDelete = {
                        scope.launch {
                            val isSuccess = if (!isNotToken) {
                                NetworkRepository.deleteCardById(card.id, token)
                            } else {
                                try {
                                    val sharedPrefs = context.getSharedPreferences("newUsers", MODE_PRIVATE)
                                    val tempCards = sharedPrefs.getString("cards", null)
                                    val gson = Gson()
                                    val tempCardsList = gson.fromJson<List<CartModel>>(
                                        tempCards,
                                        object : TypeToken<List<CartModel>>() {}.type
                                    ) ?: listOf()

                                    val tempCardsListMutable = tempCardsList.toMutableList()
                                    tempCardsListMutable.removeAll { it.id == card.id }

                                    val gsonTrueTempCards = gson.toJson(tempCardsListMutable)
                                    sharedPrefs.edit { putString("cards", gsonTrueTempCards) }
                                    true
                                } catch (e: Exception) {
                                    errorMessage = e.toString()
                                    false
                                }
                            }
                            if (isSuccess) {
                                cardsInCard.remove(card)
                                totalPrice -= card.product.price * count
                            } else {
                                errorMessage = "Ошибка при удалении товара"
                            }
                        }
                    }
                )
                Spacer(Modifier.height(32.dp))
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Сумма",
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
                        color = Black
                    )
                    Text(
                        text = "$totalPrice ₽",
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
                        color = Black
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            BigButton(
                buttonStyle = if (totalPrice == 0) ButtonStyle.Inactive else ButtonStyle.Primary,
                text = "Оформить заказ",
                onClick = {
                    scope.launch {
                        if (!isNotToken) {
                            val isSuccess = NetworkRepository.cardPlaceOlderDo(token)
                            if (isSuccess) {
                                errorMessage = "Успешно!"
                                cardsInCard.clear()
                                totalPrice = 0
                                navController.navigate("main")
                            } else {
                                errorMessage = "Ошибка при оформлении заказа"
                            }
                        } else {
                            errorMessage = "Войдите чтобы оформить заказ!"
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}
