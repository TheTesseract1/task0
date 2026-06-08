package com.example.task.presentation.screens

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.example.task.data.CartModel
import com.example.task.data.ProductDetailsModel
import com.example.task.data.ProductModel
import com.example.task.domain.NetworkRepository
import com.example.task.domain.Sort.filter
import com.example.task.domain.Sort.filterCategory
import com.example.test.BigButton
import com.example.test.ButtonStyle
import com.example.test.CardButton as CartButton
import com.example.test.ChipsButton as Chips
import com.example.test.BaseModal
import com.example.test.Black
import com.example.test.Caption
import com.example.test.PrimaryCard
import com.example.test.R
import com.example.test.SearchDefault
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CatalogScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var firstLaunch by remember { mutableStateOf(false) }
    var categoryChoose by remember { mutableStateOf("Популярное") }
    var searchVal by remember { mutableStateOf("") }
    val cardsInCard = remember { mutableStateListOf<CartModel>() }
    var token by remember { mutableStateOf("") }

    val cards = remember { mutableStateListOf<ProductModel>() }
    val filteredCards = remember { mutableStateListOf<ProductModel>() }
    var isLoading by remember { mutableStateOf(false) }
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
        isLoading = true
        val notT = context.getSharedPreferences("newUsers", MODE_PRIVATE).getString(
            "notToken",
            null
        )
        isNotToken = notT == "true"

        val allFetchedCards = NetworkRepository.getProducts()
        cards.clear()
        cards.addAll(allFetchedCards)
        filteredCards.clear()
        filteredCards.addAll(allFetchedCards)
        firstLaunch = true

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
        isLoading = false
    }

    LaunchedEffect(categoryChoose) {
        val allFetchedCards = filterCategory(categoryChoose, cards)
        if (categoryChoose != "Популярное") {
            filteredCards.clear()
            filteredCards.addAll(allFetchedCards)
        } else {
            filteredCards.clear()
            filteredCards.addAll(cards)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        LazyColumn(
            modifier.padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                SearchDefault(
                    value = searchVal,
                    onValueChange = {
                        searchVal = it
                        filteredCards.clear()
                        filteredCards.addAll(filter(searchVal, cards))
                    },
                    needIcon = {
                        Spacer(Modifier.width(38.dp))
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                )
                Spacer(Modifier.height(32.dp))
                LazyRow {
                    item {
                        for (i in listOf("Популярное", "Женщинам", "Мужчинам", "Детям", "Аксессуары")) {
                            Chips(
                                buttonStyle = if (i == categoryChoose) ButtonStyle.Primary else ButtonStyle.Tetriary,
                                onClick = {
                                    categoryChoose = i
                                },
                                text = i
                            )
                            Spacer(Modifier.width(16.dp))
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
            items(
                items = filteredCards,
                key = { card -> card.id }
            ) { card ->
                val isInCard = cardsInCard.any { cartItem -> cartItem.product.id == card.id }
                var isOpenModal by remember { mutableStateOf(false) }
                var description by remember { mutableStateOf<ProductDetailsModel?>(null) }

                PrimaryCard(
                    name = card.name,
                    price = card.price.toString(),
                    category = card.category,
                    isInCard = isInCard,
                    onClickButton = {
                        scope.launch {
                            if (!isNotToken) {
                                val isSuccess = if (isInCard) {
                                    cardsInCard.filter { it.product.id == card.id }
                                        .all { cartItem ->
                                            NetworkRepository.deleteCardById(cartItem.id, token)
                                        }
                                } else {
                                    NetworkRepository.addCardById(card.id, token)
                                }
                                if (isSuccess) {
                                    val updatedCart = NetworkRepository.getCard(token)
                                    if (updatedCart != null) {
                                        cardsInCard.clear()
                                        cardsInCard.addAll(updatedCart)
                                    }
                                } else {
                                    errorMessage = "Не удалось выполнить операцию"
                                }
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

                                    if (isInCard) {
                                        tempCardsListMutable.removeAll { it.product.id == card.id }
                                    } else {
                                        val lastCardId = tempCardsListMutable.lastOrNull()?.id ?: 0
                                        tempCardsListMutable.add(CartModel(lastCardId + 1, card, 1))
                                    }

                                    sharedPrefs.edit { putString("cards", gson.toJson(tempCardsListMutable)) }

                                    cardsInCard.clear()
                                    cardsInCard.addAll(tempCardsListMutable)
                                } catch (e: Exception) {
                                    errorMessage = e.toString()
                                }
                            }
                        }
                    },
                    modifier = Modifier.clickable {
                        isOpenModal = true
                    }
                )
                if (isOpenModal) {

                    LaunchedEffect(Unit) {
                        delay(1000L)
                        description = NetworkRepository.getProductDescriptionById(card.id)
                    }

                    BaseModal(
                        onDismissRequest = {
                            isOpenModal = false
                            description = null
                        }
                    ) {
                        Row {
                            Text(
                                text = card.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W600,
                                color = Black,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(48.dp))
                            Icon(
                                painter = painterResource(R.drawable.icon_close),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                                    .clickable {
                                        isOpenModal = false
                                    },
                            )
                        }
                        Spacer(Modifier.height(20.dp))

                        if (description == null) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = "Описание",
                                color = Caption,
                                fontWeight = FontWeight.W500,
                                fontSize = 16.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = description!!.description,
                                color = Black,
                                fontWeight = FontWeight.W400,
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.height(89.dp))
                            Text(
                                text = "Примерный расход:",
                                color = Caption,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = description!!.weight,
                                color = Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500
                            )
                            Spacer(Modifier.height(19.dp))
                            BigButton(
                                buttonStyle = if (isInCard) ButtonStyle.Secondary else ButtonStyle.Primary,
                                text = if (isInCard) "Убрать" else "Добавить за " + card.price + " ₽",
                                onClick = {
                                    scope.launch {
                                        if (!isNotToken) {
                                            val isSuccess = if (isInCard) {
                                                cardsInCard.filter { it.product.id == card.id }
                                                    .all { cartItem ->
                                                        NetworkRepository.deleteCardById(cartItem.id, token)
                                                    }
                                            } else {
                                                NetworkRepository.addCardById(card.id, token)
                                            }
                                            if (isSuccess) {
                                                val updatedCart = NetworkRepository.getCard(token)
                                                if (updatedCart != null) {
                                                    cardsInCard.clear()
                                                    cardsInCard.addAll(updatedCart)
                                                }
                                            }
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

                                                if (isInCard) {
                                                    tempCardsListMutable.removeAll { it.product.id == card.id }
                                                } else {
                                                    val lastCardId = tempCardsListMutable.lastOrNull()?.id ?: 0
                                                    tempCardsListMutable.add(CartModel(lastCardId + 1, card, 1))
                                                }

                                                sharedPrefs.edit { putString("cards", gson.toJson(tempCardsListMutable)) }

                                                cardsInCard.clear()
                                                cardsInCard.addAll(tempCardsListMutable)
                                            } catch (e: Exception) {
                                                errorMessage = e.toString()
                                            }
                                        }
                                    }
                                    isOpenModal = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
        if (cardsInCard.isNotEmpty() && firstLaunch) {
            var totalPrice by remember { mutableIntStateOf(0) }

            LaunchedEffect(cardsInCard.toList()) {
                var sum = 0
                for (cardItem in cardsInCard) {
                    sum += cardItem.product.price * cardItem.quantity
                }
                totalPrice = sum
            }

            Column(
                Modifier.align(Alignment.BottomCenter)
            ) {
                CartButton(
                    buttonStyle = ButtonStyle.Primary,
                    prise = totalPrice.toString(),
                    onClick = {
                        navController.navigate("cardPage")
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
