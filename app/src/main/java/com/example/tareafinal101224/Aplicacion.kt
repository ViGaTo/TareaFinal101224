package com.example.tareafinal101224

import android.app.Application
import android.content.Context
import com.example.tareafinal101224.providers.db.MyDatabase

class Aplicacion: Application() {
    companion object{
        const val VERSION = 1
        const val DB = "TareaFinal_1"
        const val TABLA = "proveedores"
        lateinit var appContext: Context
        lateinit var llave: MyDatabase
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        llave = MyDatabase()
    }
}