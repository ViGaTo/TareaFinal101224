package com.example.tareafinal101224.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Producto(
    @SerializedName("title") val titulo: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("price") val precio: Double,
    @SerializedName("image") val imagen: String
): Serializable
