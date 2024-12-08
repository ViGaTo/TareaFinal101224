package com.example.tareafinal101224

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tareafinal101224.adapters.ProveedorAdapter
import com.example.tareafinal101224.databinding.ActivitySqliteBinding
import com.example.tareafinal101224.fragments.BotonFragment
import com.example.tareafinal101224.models.ProveedorModel
import com.example.tareafinal101224.providers.db.CrudProveedores

class SQLiteActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySqliteBinding
    private lateinit var adapter: ProveedorAdapter
    var lista = mutableListOf<ProveedorModel>()

    var activado = 1
    var primera = false

    val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        when(it.resultCode){
            RESULT_OK ->{
                finish()
                startActivity(intent)
            }

            RESULT_CANCELED ->{
                Toast.makeText(this, "Se ha cancelado el proceso", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySqliteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        iniciarFragment()
        setListeners()
        setRecycler()
    }

    private fun iniciarFragment() {
        val fg = BotonFragment({onClickButton()})
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragmentBoton,fg)
        }
    }

    private fun onClickButton(){
        obtenerLista(activado)
        onRestart()
    }

    private fun setRecycler() {
        val layout = LinearLayoutManager(this)
        binding.recycler.layoutManager = layout
        if(primera == false) {
            obtenerLista(1)
            primera = true
        }
        adapter = ProveedorAdapter(lista, {position -> deleteProveedor(position)}, {position -> updateProveedor(position)}, {position -> onDetalleClick(position)})
        binding.recycler.adapter = adapter
    }

    private fun deleteProveedor(position: Int) {
        val id = lista[position].id
        lista.removeAt(position)
        if(CrudProveedores().delete(id)){
            adapter.notifyItemRemoved(position)
        }else{
            Toast.makeText(this, "No se pudo eliminar al proveedor.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProveedor(position: Int){
        val proveedor = lista[position]
        val i = Intent(this, FormularioActivity::class.java).apply{
            putExtra("PROVEEDOR", proveedor)
        }

        responseLauncher.launch(i)
    }

    private fun onDetalleClick(position: Int){
        val proveedor = lista[position]
        val i = Intent(this, DetalleActivity::class.java).apply {
            putExtra("PROVEEDOR", proveedor)
        }

        startActivity(i)
    }

    fun obtenerLista(activo: Int) {
        lista = CrudProveedores().read(activo)
        if(lista.size > 0){
            binding.tvLista.visibility = View.INVISIBLE
        }else{
            binding.tvLista.visibility = View.VISIBLE
        }

        when(activado){
            0 -> activado = 1
            1 -> activado = 0
        }
    }

    private fun setListeners() {
        binding.btnNuevo.setOnClickListener {
            responseLauncher.launch(Intent(this, FormularioActivity::class.java))
        }

        binding.btnPortal.setOnClickListener {
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()
        setRecycler()
    }
}