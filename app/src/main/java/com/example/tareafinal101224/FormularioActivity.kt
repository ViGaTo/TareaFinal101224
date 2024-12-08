package com.example.tareafinal101224

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tareafinal101224.databinding.ActivityFormularioBinding
import com.example.tareafinal101224.models.ProveedorModel
import com.example.tareafinal101224.providers.db.CrudProveedores

class FormularioActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityFormularioBinding
    private var id = -1
    private var nombre = ""
    private var descripcion = ""
    private var tipo = ""
    private var ambito = ""
    private var calificacion = 1
    private var activo = -1
    private var isUpdate = false

    private lateinit var adapterTipos: ArrayAdapter<CharSequence>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adapterTipos = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item)

        binding.spTipos.adapter = adapterTipos
        camposUpdate()
        setListeners()

        if(isUpdate){
            binding.tvTitulo.text = "Editar Proveedor"
            binding.btnAceptar.text = "Editar"
        }
    }

    private fun setListeners() {
        binding.sbCalificacion.setOnSeekBarChangeListener(this)
        binding.spTipos.onItemSelectedListener = this
        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnAceptar.setOnClickListener {
            guardar()
        }
    }

    private fun datosCorrectos(): Boolean{
        nombre = binding.etNombre.text.toString().trim()
        descripcion = binding.etDescripcion.text.toString().trim()

        if(nombre.length < 2){
            binding.etNombre.error = "El campo Nombre debe tener al menos dos caracteres"
            return false
        }

        if(descripcion.length < 5){
            binding.etDescripcion.error = "El campo Descripcion debe tener al menos cinco caracteres"
            return false
        }

        if(binding.spTipos.selectedItemId.toInt() == 0){
            Toast.makeText(this, "Seleccione un tipo...", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun guardar() {
        if(datosCorrectos()){
            tipo = binding.spTipos.selectedItem.toString()
            if(binding.rbPublico.isChecked){
                ambito = "Público"
            }else{
                ambito = "Privado"
            }

            if(binding.cbActivo.isChecked){
                activo = 1
            }else{
                activo = 0
            }

            val p = ProveedorModel(id, nombre, descripcion, tipo, ambito, calificacion, activo)
            if(!isUpdate){
                if(CrudProveedores().create(p) != -1L){
                    Toast.makeText(this, "Proveedor añadido", Toast.LENGTH_SHORT).show()
                    val i = Intent()
                    setResult(RESULT_OK, i)
                    finish()
                }else{
                    binding.etNombre.error = "El nombre del proveedor ya se esta usando"
                }
            }else{
                if(CrudProveedores().update(p)){
                    Toast.makeText(this, "Proveedor editado", Toast.LENGTH_SHORT).show()
                    val i = Intent()
                    setResult(RESULT_OK, i)
                    finish()
                }else{
                    binding.etNombre.error = "El nombre del proveedor ya se esta usando"
                }
            }
        }
    }

    private fun camposUpdate() {
        val datosProveedor = intent.extras
        if(datosProveedor != null){
            val p = datosProveedor.getSerializable("PROVEEDOR") as ProveedorModel
            isUpdate = true
            id = p.id
            nombre = p.nombre
            descripcion = p.descripcion
            tipo = p.tipo
            ambito = p.ambito
            calificacion = p.calificacion
            activo = p.activo
            completarCampos()
        }
    }

    private fun completarCampos() {
        binding.etNombre.setText(nombre)
        binding.etDescripcion.setText(descripcion)
        binding.spTipos.setSelection(adapterTipos.getPosition(tipo))
        if(ambito == "Público"){
            binding.rbPublico.isChecked = true
        }else{
            binding.rbPrivado.isChecked = true
        }

        if(activo == 1){
            binding.cbActivo.isChecked = true
        }else{
            binding.cbActivo.isChecked = false
        }
        binding.sbCalificacion.progress = calificacion
        mostrarCalificacion()
    }

    private fun mostrarCalificacion() {
        binding.tvCalificacion.text = getString(R.string.formato_calificacion, calificacion)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(seekBar?.id) {
            R.id.sbCalificacion -> {
                when (progress) {
                    1 -> {
                        calificacion = 1
                        mostrarCalificacion()
                    }

                    2 -> {
                        calificacion = 2
                        mostrarCalificacion()
                    }

                    3 -> {
                        calificacion = 3
                        mostrarCalificacion()
                    }

                    4 -> {
                        calificacion = 4
                        mostrarCalificacion()
                    }

                    5 -> {
                        calificacion = 5
                        mostrarCalificacion()
                    }
                }
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.spTipos->{
                tipo = parent.getItemAtPosition(position).toString()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}