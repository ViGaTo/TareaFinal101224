package com.example.tareafinal101224.data.models

import java.io.Serializable

class ProveedorModel(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val tipo: String,
    val ambito: String,
    val calificacion: Int,
    val activo: Int
): Serializable