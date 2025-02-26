package com.example.tareafinal101224.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tareafinal101224.R
import com.example.tareafinal101224.data.models.Usuario
import com.example.tareafinal101224.data.providers.repository.ListaUsuarioRepository
import com.example.tareafinal101224.databinding.ActivityViewModelBinding
import com.example.tareafinal101224.ui.adapters.UsuarioAdapter
import com.example.tareafinal101224.ui.viewmodels.ListaUsuarioViewModel
import com.example.tareafinal101224.utils.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ViewModelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewModelBinding
    private val viewModel: ListaUsuarioViewModel by viewModels()
    private val adapter = UsuarioAdapter(listOf<Usuario>(), { item -> activamientoUsuario(item)}, { item -> editarUsuario(item)})
    private lateinit var database: DatabaseReference
    private var usuario = ""

    private lateinit var preferences: Preferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewModelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = FirebaseDatabase.getInstance().getReference("usuarios")
        viewModel.usuarios.observe(this, { lista ->
            adapter.actualizarLista(lista)
        })
        auth = Firebase.auth
        preferences = Preferences(this)
        setRecycler()
        setListeners()
    }

    private fun setListeners() {
        binding.btnAdd.setOnClickListener {
            addUsuario()
        }

        binding.btnVolverViewmodel.setOnClickListener {
            finish()
        }

        if(!preferences.isAdmin()){
            binding.menuViewModel.menu.removeItem(R.id.item_portal_usuarios)
        }

        binding.menuViewModel.setCheckedItem(R.id.item_portal_usuarios)

        binding.menuViewModel.setNavigationItemSelectedListener {
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

    private fun addUsuario() {
        usuario = binding.etUsuario.text.toString().trim()
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(usuario).matches()){
            binding.etUsuario.error = "ERROR. El email no es válido."
            return
        }
        viewModel.addUsuario(usuario)
        binding.etUsuario.setText("")
        Toast.makeText(this, "Usuario $usuario añadido", Toast.LENGTH_SHORT).show()
    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvLista.layoutManager = layoutManager
        binding.rvLista.adapter = adapter
    }

    private fun activamientoUsuario(item: Usuario) {
        viewModel.activamientoUsuario(item)
        Toast.makeText(this, "El usuario ${item.email} ha cambiado su activación", Toast.LENGTH_SHORT).show()
    }

    private fun editarUsuario(item: Usuario) {
        viewModel.editarUsuario(item)
        Toast.makeText(this, "Usuario ${item.email} editado", Toast.LENGTH_SHORT).show()
    }
}