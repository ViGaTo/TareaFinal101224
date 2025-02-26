package com.example.tareafinal101224.data.providers.api

import com.example.tareafinal101224.data.models.Producto
import retrofit2.Response
import retrofit2.http.GET

interface ProductosInterfaz {
    @GET("products/")
    suspend fun getProductos(): Response<List<Producto>>
}