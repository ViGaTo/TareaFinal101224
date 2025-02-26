package com.example.tareafinal101224.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tareafinal101224.R
import com.example.tareafinal101224.ui.adapters.ProductoAdapter
import com.example.tareafinal101224.databinding.ActivityApiBinding
import com.example.tareafinal101224.data.models.Producto
import com.example.tareafinal101224.data.providers.api.ObjectApiClient.apiClient
import com.example.tareafinal101224.utils.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApiBinding
    private lateinit var adapter: ProductoAdapter
    val lista = mutableListOf<Producto>()

    private lateinit var preferences: Preferences
    private lateinit var auth: FirebaseAuth

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
        auth = Firebase.auth
        preferences = Preferences(this)
        setRecycler()
        setListeners()
    }

    private fun setListeners() {
        binding.btnVolverProductos.setOnClickListener {
            finish()
        }

        if(!preferences.isAdmin()){
            binding.menuApi.menu.removeItem(R.id.item_portal_usuarios)
        }

        binding.menuApi.setCheckedItem(R.id.item_portal_productos)

        binding.menuApi.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_portal_inicio -> {
                    startActivity(Intent(this, PortalActivity::class.java))
                    true
                }

                R.id.item_portal_productos -> {
                    startActivity(Intent(this, ApiActivity::class.java))
                    true
                }

                R.id.item_portal_proveedores -> {
                    startActivity(Intent(this, SQLiteActivity::class.java))
                    true
                }

                R.id.item_portal_web -> {
                    startActivity(Intent(this, WebActivity::class.java))
                    true
                }

                R.id.item_portal_cerrar_sesion -> {
                    auth.signOut()
                    preferences.limpiarPreferencias()
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.item_portal_salir -> {
                    finishAffinity()
                    true
                }

                R.id.item_portal_usuarios -> {
                    startActivity(Intent(this, ViewModelActivity::class.java))
                    true
                }

                else -> false
            }
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