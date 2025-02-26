package com.example.tareafinal101224.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tareafinal101224.R
import com.example.tareafinal101224.data.models.Producto

class ProductoAdapter(var lista : MutableList<Producto>, private val onDetalleClick: (Int) -> Unit): RecyclerView.Adapter<ProductoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.producto_layout, parent, false)
        return ProductoViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = lista[position]
        holder.render(producto, onDetalleClick)
    }

    override fun getItemCount() = lista.size
}