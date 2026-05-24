package com.example.task.data

import androidx.compose.ui.text.font.FontWeight

data class RegisterModel(
    val email: String,
    val password: String
)

data class AuthModel(
    val email: String,
    val password: String,
)

data class AuthResponseModel(
    val userId: Int,
    val firstName: String,
    val token: String
)

data class UpdateProfileModel(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthday: String,
    val gender: String,
    val telegram: String
)

data class UserTokenModel(
    val id: String,
    val token: String
)

data class UserModel(
    val id: Int,
    val email: String,
    val lastName: String,
    val middleName: String,
    val birthday: String,
    val gender: String,
    val telegram: String,
)

data class ProjectModel(
    val id: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val descriptionSource: String,
    val category: String
)

data class ProductModel(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val gender: String
)

data class ProductDetailsModel(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val category: String,
    val weight: String,
    val gender: String,
)

data class NewsModel(
    val id: Int,
    val fileName: String,
    val collectionName: String
)

data class CartModel(
    val id: Int,
    val product: ProductModel,
    val quantity: Int
)