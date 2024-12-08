package com.example.tareafinal101224

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tareafinal101224.databinding.ActivityPortalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PortalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPortalBinding
    private lateinit var preferences: Preferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPortalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        preferences = Preferences(this)
        obtenerPreferencias()
        setListeners()
    }

    private fun setListeners() {
        binding.btnCerrarSesion.setOnClickListener{
            auth.signOut()
            preferences.limpiarPreferencias()

            finish()
        }

        binding.btnSalir.setOnClickListener {
            finishAffinity()
        }

        binding.btnWeb.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java))
        }

        binding.btnSqlite.setOnClickListener {
            startActivity(Intent(this, SQLiteActivity::class.java))
        }

        binding.btnApi.setOnClickListener {
            startActivity(Intent(this, ApiActivity::class.java))
        }
    }

    private fun obtenerPreferencias() {
        binding.tvBienvenida.text = "Bienvenido " +  auth.currentUser?.email.toString()
        val admin = preferences.isAdmin()

        if(!admin){
            binding.btnSqlite.visibility = View.INVISIBLE
        }
    }
}