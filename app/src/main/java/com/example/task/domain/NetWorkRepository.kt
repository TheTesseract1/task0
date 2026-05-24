package com.example.task.domain


import com.example.task.data.AuthModel
import com.example.task.data.AuthResponseModel
import com.example.task.data.CartModel
import com.example.task.data.NewsModel
import com.example.task.data.ProductDetailsModel
import com.example.task.data.ProductModel
import com.example.task.data.ProjectModel
import com.example.task.data.RegisterModel
import com.example.task.data.UpdateProfileModel
import com.example.task.data.UserModel
import com.example.task.data.UserTokenModel
import com.example.task.data.network
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object NetworkRepository {

    suspend fun reg(body: RegisterModel): Boolean {
        val response = network.reg(body)
        return response.isSuccessful
    }

    suspend fun login(body: AuthModel): AuthResponseModel? {
        try {
            val response = network.login(body)
            return if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {return null}
    }

    suspend fun userInfoAdd(body: UpdateProfileModel): Boolean {
        val response = network.userInfoAdd(body)
        return response.isSuccessful
    }

    suspend fun userTokensGet(token: String): List<UserTokenModel>? {
        try {
            val response = network.userTokensGet("Bearer $token")
            return if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {return null}
    }

    suspend fun userInfoGet(token: String): UserModel? {
        try {
            val response = network.userInfoGet("Bearer $token")
            return if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {return null}
    }

    suspend fun deleteToken(tokenId: String,authToken: String): Boolean {
        try {
            val response = network.deleteToken(tokenId,"Bearer $authToken")
            return response.isSuccessful
        } catch (e: Exception) {return false}
    }

    suspend fun projectsGet(token: String): List<ProjectModel>? {
        try {
            val response = network.projectsGet("Bearer $token")
            return if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            return null}
    }

    suspend fun projectsAdd(
        name: String,
        startDate: String,
        endDate: String,
        descriptionSource: String,
        category: String,
        fileBytes: ByteArray,
        fileName: String,
        token: String
    ): Boolean {
        try {
            val fileBody = fileBytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", fileName,fileBody)

            val response = network.projectsAdd(
                name,
                startDate,
                endDate,
                descriptionSource,
                category,
                filePart,
                "Bearer $token"
            )
            return response.isSuccessful
        } catch (e: Exception) {return false}
    }

    suspend fun getProjectById(projectId: Int,token: String): ProjectModel? {
        try {
            val response = network.getProjectById(projectId,"Bearer $token")
            return if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {return null}
    }

    suspend fun getCard(token: String): List<CartModel>? {
        try {
            val response = network.getCard("Bearer $token")
            return if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {return null}
    }

    suspend fun addCardById(projectId: Int, token: String): Boolean {
        try {
            val response = network.addCardById(projectId,"Bearer $token")
            return response.isSuccessful
        } catch (e: Exception) {return false}
    }

    suspend fun cardPlaceOlderDo(token: String): Boolean {
        try {
            val response = network.cardPlaceOlderDo("Bearer $token")
            return response.isSuccessful
        } catch (e: Exception) {return false}
    }

    suspend fun deleteCardById(id: Int, token: String): Boolean {
        try {
            val response = network.deleteCardById(id,"Bearer $token")
            return response.isSuccessful
        } catch (e: Exception) {return false}
    }

    suspend fun patchCardById(id: Int,quantity: Int, token: String): Boolean {
        try {
            val response = network.patchCardById(id,quantity,"Bearer $token")
            return response.isSuccessful
        } catch (e: Exception) {return false}
    }

    suspend fun getProducts(): List<ProductModel> {
        try {
            val response = network.getProducts()
            return if (response.isSuccessful && response.body() !== null) response.body()!! else emptyList()
        } catch (e: Exception) {return emptyList()}
    }

    suspend fun getProductDescriptionById(id: Int): ProductDetailsModel? {
        try {
            val response = network.getProductDescriptionById(id)
            return if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {return null}
    }

    suspend fun getNews(): List<NewsModel>? {
        try {
            val response = network.getNews()
            return if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {return null}
    }

}