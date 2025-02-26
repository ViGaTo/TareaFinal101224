package com.example.tareafinal101224.data.providers.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ObjectApiClient {
    private val retrofit2 = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiClient = retrofit2.create(ProductosInterfaz::class.java)
}