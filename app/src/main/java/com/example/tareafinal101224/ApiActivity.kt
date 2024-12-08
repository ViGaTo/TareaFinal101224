package com.example.tareafinal101224

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tareafinal101224.adapters.ProductoAdapter
import com.example.tareafinal101224.databinding.ActivityApiBinding
import com.example.tareafinal101224.models.Producto
import com.example.tareafinal101224.providers.db.ObjectApiClient.apiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApiBinding
    private lateinit var adapter: ProductoAdapter
    val lista = mutableListOf<Producto>()


    var api = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setRecycler()
        setListeners()
    }

    private fun setListeners() {
        binding.btnVolverProductos.setOnClickListener {
            finish()
        }
    }

    private fun setRecycler() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerProductos.layoutManager = layoutManager
        adapter = ProductoAdapter(lista, {position -> onDetalleClick(position)})
        binding.recyclerProductos.adapter = adapter
        obtenerProductos()
    }

    private fun onDetalleClick(position: Int) {
        val producto = adapter.lista[position]
        val i = Intent(this, DetalleProductoActivity::class.java).apply {
            putExtra("PRODUCTO", producto)
        }

        startActivity(i)
    }

    private fun obtenerProductos() {
        lifecycleScope.launch(Dispatchers.IO){
            val datos = apiClient.getProductos()
            val listaProductos = datos.body()?: emptyList()
            withContext(Dispatchers.Main) {
                adapter.lista = listaProductos.toMutableList()
                adapter.notifyDataSetChanged()

                if (!datos.isSuccessful) {
                    Toast.makeText(
                        this@ApiActivity,
                        "Error al recuperar los productos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}