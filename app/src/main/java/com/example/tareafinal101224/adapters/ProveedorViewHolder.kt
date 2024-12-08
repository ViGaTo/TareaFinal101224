package com.example.tareafinal101224.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tareafinal101224.databinding.ProveedorLayoutBinding
import com.example.tareafinal101224.models.ProveedorModel

class ProveedorViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val binding = ProveedorLayoutBinding.bind(v)
    fun render(p: ProveedorModel, deleteProveedor: (Int) -> Unit, updateProveedor: (Int) -> Unit, onDetalleClick: (Int) -> Unit){
        binding.tvNombre.text = p.nombre + " (${p.id})"

        binding.btnBorrar.setOnClickListener {
            deleteProveedor(adapterPosition)
        }

        binding.btnUpdate.setOnClickListener {
            updateProveedor(adapterPosition)
        }

        binding.layout.setOnClickListener{
            onDetalleClick(adapterPosition)
        }
    }
}