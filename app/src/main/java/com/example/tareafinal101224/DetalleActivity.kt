package com.example.tareafinal101224

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tareafinal101224.databinding.ActivityDetalleBinding
import com.example.tareafinal101224.models.ProveedorModel

class DetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetalleBinding.inflate(layoutInflater)
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
        val datosProveedor = intent.extras
        val p = datosProveedor?.getSerializable("PROVEEDOR") as ProveedorModel
        binding.tvNombreDetalle.text = p.nombre
        binding.tvDescripcionDetalle.text = p.descripcion
        binding.tvTipoDetalle.text = "Tipo: ${p.tipo}"
        binding.tvAmbitoDetalle.text = "Ámbito: ${p.ambito}"
        binding.tvActivoDetalle.text = "Activo: " + if(p.activo == 1){
            "Activo"
        }else{
            "Inactivo"
        }
        binding.tvCalificacionDetalle.text = "Calificación: ${p.calificacion} estrella/s"
    }

    private fun setListeners() {
        binding.btnVolverDetalle.setOnClickListener {
            finish()
        }
    }
}