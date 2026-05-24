package com.example.task.data

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Network {

    // users

    @POST("users/register")
    suspend fun reg(
        @Body body: RegisterModel
    ): Response<Unit>

    @POST("users/login")
    suspend fun login(
        @Body body: AuthModel
    ): Response<AuthResponseModel>

    @PATCH("users/profile")
    suspend fun userInfoAdd(
        @Body body: UpdateProfileModel
    ): Response<Unit>

    @GET("users/tokens")
    suspend fun userTokensGet(
        @Header("Authorization") token: String
    ): Response<List<UserTokenModel>>

    @GET("users/info")
    suspend fun userInfoGet(
        @Header("Authorization") token: String
    ): Response<UserModel>

    @DELETE("users/token/{id}")
    suspend fun deleteToken(
        @Path("id") tokenId: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    // projects

    @GET("projects")
    suspend fun projectsGet(
        @Header("Authorization") token: String
    ): Response<List<ProjectModel>>

    @Multipart
    @POST("projects")
    suspend fun projectsAdd(
        @Query("name") name: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("descriptionSource") descriptionSource: String,
        @Query("category") category: String,
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("projects/{id}")
    suspend fun getProjectById(
        @Path("id") projectId: Int,
        @Header("Authorization") token: String
    ): Response<ProjectModel>

    // card

    @GET("carts")
    suspend fun getCard(
        @Header("Authorization") token: String
    ): Response<List<CartModel>>

    @POST("carts")
    suspend fun addCardById(
        @Query("projectId") projectId: Int,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("carts/place-order")
    suspend fun cardPlaceOlderDo(
        @Header("Authorization") token: String
    ): Response<Unit>

    @DELETE("carts/{id}")
    suspend fun deleteCardById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>

    @PATCH("carts/{id}")
    suspend fun patchCardById(
        @Path("id") id: Int,
        @Query("quantity") quantity: Int,
        @Header("Authorization") token: String
    ): Response<Unit>

    // products

    @GET("products")
    suspend fun getProducts(
    ): Response<List<ProductModel>>

    @GET("products/{id}")
    suspend fun getProductDescriptionById(
        @Path("id") id: Int
    ): Response<ProductDetailsModel>

    // news

    @GET("news")
    suspend fun getNews(
    ): Response<List<NewsModel>>
}

val network = Retrofit.Builder()
    .baseUrl("https://skill.matstart.ru/matule-api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create<Network>()