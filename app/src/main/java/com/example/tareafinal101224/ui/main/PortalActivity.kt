package com.example.tareafinal101224.ui.main

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tareafinal101224.utils.Preferences
import com.example.tareafinal101224.R
import com.example.tareafinal101224.databinding.ActivityPortalBinding
import com.example.tareafinal101224.ui.viewmodels.ListaUsuarioViewModel
import com.example.tareafinal101224.utils.encodeEmail
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class PortalActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityPortalBinding
    private lateinit var preferences: Preferences
    private lateinit var auth: FirebaseAuth

    private lateinit var mediaController: MediaController

    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null

    private var usuario = ""
    private val viewModel: ListaUsuarioViewModel by viewModels()
    private lateinit var database: DatabaseReference

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
        database = FirebaseDatabase.getInstance().getReference("usuarios")
        comprobarUsuario()
        mediaController=MediaController(this)
        reproducirTutorial()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        iniciarSensores()
        setListeners()
    }

    private fun comprobarUsuario() {
        usuario = auth.currentUser?.email.toString()
        database.child(usuario.encodeEmail()).get().addOnSuccessListener {
            if(!it.exists()){
                viewModel.addUsuario((usuario))
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al comprobar usuario", Toast.LENGTH_SHORT).show()
        }
        viewModel.addUsuario(usuario)
    }

    private fun iniciarSensores() {
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun reproducirTutorial() {
        val uri = Uri.parse("android.resource://$packageName/${R.raw.tutorial}")
        try{
            binding.videoView.setVideoURI(uri)
            binding.videoView.requestFocus()
            binding.videoView.start()
        }catch (e: Exception){
            System.out.println("Error al reproducir video")
        }
        binding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoView)
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

        binding.btnViewModel.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }

        if(!preferences.isAdmin()){
            binding.menu.menu.removeItem(R.id.item_portal_usuarios)
        }

        binding.menu.setCheckedItem(R.id.item_portal_inicio)

        binding.menu.setNavigationItemSelectedListener {
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

    private fun obtenerPreferencias() {
        binding.tvBienvenida.text = "Bienvenido " +  auth.currentUser?.email.toString()
        val admin = preferences.isAdmin()

        if(!admin){
            binding.btnViewModel.visibility = View.INVISIBLE
            binding.separator.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        acelerometro?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!=null){
            when(event.sensor.type){
               Sensor.TYPE_ACCELEROMETER -> {
                   val x = event.values[0]

                   if (Math.abs(x) > 1){
                       binding.drawerLayout.openDrawer(GravityCompat.START)
                   }
               }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}