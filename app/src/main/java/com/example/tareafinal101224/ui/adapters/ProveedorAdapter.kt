package com.example.tareafinal101224.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tareafinal101224.R
import com.example.tareafinal101224.data.models.ProveedorModel

class ProveedorAdapter(var lista: MutableList<ProveedorModel>, private val deleteProveedor: (Int) -> Unit, private val updateProveedor: (Int) -> Unit, private val onDetalleClick: (Int) -> Unit):
RecyclerView.Adapter<ProveedorViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.proveedor_layout, parent, false)
        return ProveedorViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
        holder.render(lista[position], deleteProveedor, updateProveedor, onDetalleClick)
    }

}