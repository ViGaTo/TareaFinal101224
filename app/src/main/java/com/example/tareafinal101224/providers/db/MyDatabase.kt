package com.example.tareafinal101224.providers.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tareafinal101224.Aplicacion

class MyDatabase: SQLiteOpenHelper(Aplicacion.appContext, Aplicacion.DB, null, Aplicacion.VERSION) {
    val query = "CREATE TABLE ${Aplicacion.TABLA}(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre TEXT UNIQUE NOT NULL," +
            "descripcion TEXT NOT NULL," +
            "tipo TEXT NOT NULL," +
            "ambito TEXT NOT NULL," +
            "calificacion INTEGER NOT NULL," +
            "activo INTEGER NOT NULL);";

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(newVersion > oldVersion){
            val borrarTabla = "DROP TABLE ${Aplicacion.TABLA};"
            db?.execSQL(borrarTabla)
            onCreate(db)
        }
    }
}