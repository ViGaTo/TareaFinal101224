package com.example.tareafinal101224.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tareafinal101224.R

class BotonFragment(private val onClickButton: () -> Unit) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
    }

    private fun setListeners() {
        val boton = view?.findViewById<Button>(R.id.btn_activos)
        boton?.setOnClickListener{
            cambiarEstado()
            onClickButton()
        }
    }

    private fun cambiarEstado() {
        val btn_activos = view?.findViewById<Button>(R.id.btn_activos)
        if (btn_activos?.text.toString().equals(getString(R.string.Activos))) {
            btn_activos?.setText(R.string.Inactivos)
            btn_activos?.setBackgroundColor(Color.parseColor("#6F6E6E"))
        } else {
            btn_activos?.setText(R.string.Activos)
            btn_activos?.setBackgroundColor(Color.parseColor("#FFEB3B"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boton, container, false)
    }
}