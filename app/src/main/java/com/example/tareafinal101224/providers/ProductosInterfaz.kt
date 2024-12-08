package com.example.tareafinal101224.providers

import com.example.tareafinal101224.models.Producto
import retrofit2.Response
import retrofit2.http.GET

interface ProductosInterfaz {
    @GET("products/")
    suspend fun getProductos(): Response<List<Producto>>
}