package com.example.tareafinal101224.providers.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.tareafinal101224.Aplicacion
import com.example.tareafinal101224.models.ProveedorModel

class CrudProveedores {
    fun create(p: ProveedorModel): Long{
        val con = Aplicacion.llave.writableDatabase
        return try {
            con.insertWithOnConflict(
                Aplicacion.TABLA,
                null,
                p.toContentValues(),
                SQLiteDatabase.CONFLICT_IGNORE
            )
        }catch(ex: Exception){
            ex.printStackTrace()
            -1L
        }finally{
            con.close()
        }
    }

    fun read(activo: Int): MutableList<ProveedorModel>{
        val lista = mutableListOf<ProveedorModel>()
        val con = Aplicacion.llave.readableDatabase

        try{
            val cursor = con.query(
                Aplicacion.TABLA,
                arrayOf("id", "nombre", "descripcion", "tipo", "ambito", "calificacion", "activo"),
                "activo = ?",
                arrayOf(activo.toString()),
                null,
                null,
                null
            )
            while(cursor.moveToNext()){
                val proveedor = ProveedorModel(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6)
                )
                lista.add(proveedor)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }finally {
            con.close()
        }

        return lista
    }

    fun delete(id: Int): Boolean{
        val con = Aplicacion.llave.writableDatabase
        val proveedorBorrado = con.delete(Aplicacion.TABLA, "id = ?", arrayOf(id.toString()))
        con.close()
        return proveedorBorrado > 0
    }

    fun update(p: ProveedorModel): Boolean{
        val con = Aplicacion.llave.writableDatabase
        val values = p.toContentValues()
        var filasAfectadas = 0
        val query = "SELECT ID FROM ${Aplicacion.TABLA} WHERE NOMBRE = ? AND ID <> ?"
        val cursor = con.rawQuery(query, arrayOf(p.nombre, p.id.toString()))
        val existeNombre = cursor.moveToFirst()
        cursor.close()
        if(!existeNombre){
            filasAfectadas = con.update(Aplicacion.TABLA, values, "ID = ?", arrayOf(p.id.toString()))
        }
        con.close()
        return filasAfectadas > 0
    }

    private fun ProveedorModel.toContentValues(): ContentValues{
        return ContentValues().apply {
            put("nombre", nombre)
            put("descripcion", descripcion)
            put("tipo", tipo)
            put("ambito", ambito)
            put("calificacion", calificacion)
            put("activo", activo)
        }
    }
}