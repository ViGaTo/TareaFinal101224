package com.example.tareafinal101224.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tareafinal101224.databinding.ProductoLayoutBinding
import com.example.tareafinal101224.data.models.Producto
import com.squareup.picasso.Picasso

class ProductoViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val binding = ProductoLayoutBinding.bind(v)
    fun render(producto: Producto, onDetalleClick: (Int) -> Unit){
        binding.tvNombreApi.text = producto.titulo
        binding.tvPrecioApi.text = producto.precio.toString() + " €"
        Picasso.get().load(producto.imagen).into(binding.ivProducto)

        binding.layoutProducto.setOnClickListener {
            onDetalleClick(adapterPosition)
        }
    }
}
