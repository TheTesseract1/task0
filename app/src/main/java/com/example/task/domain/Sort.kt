package com.example.task.domain

import com.example.task.data.ProductModel


object Sort {

    fun filter(name: String, cards: List<ProductModel>): List<ProductModel> {
        if (name.isEmpty() || name == ""){
            return cards
        }

        return cards.filter { productCard ->
            productCard.name.contains(name,true)
        }
    }

    fun filterCategory(category: String, cards: List<ProductModel>): List<ProductModel> {
        if (category.isEmpty() || category == ""){
            return cards
        }

        return cards.filter { productCard ->
            productCard.gender.contains(category,true)
        }
    }
}