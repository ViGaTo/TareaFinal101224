package com.example.tareafinal101224.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tareafinal101224.R
import com.example.tareafinal101224.databinding.ActivityDetalleProductoBinding
import com.example.tareafinal101224.data.models.Producto
import com.squareup.picasso.Picasso

class DetalleProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleProductoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setListeners()
        obtenerYMostrarDatos()
    }

    private fun obtenerYMostrarDatos() {
        val datosProducto = intent.extras
        val p = datosProducto?.getSerializable("PRODUCTO") as Producto
        binding.tvTituloDetalleApi.text = p.titulo
        binding.tvDescripcionDetalleApi.text = p.descripcion
        binding.tvPrecioDetalleApi.text = p.precio.toString() + " €"
        Picasso.get().load(p.imagen).into(binding.ivProductoDetalleApi)
    }

    private fun setListeners() {
        binding.btnVolverApi.setOnClickListener {
            finish()
        }
    }
}