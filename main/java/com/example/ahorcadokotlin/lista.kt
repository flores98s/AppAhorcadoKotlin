package com.example.ahorcadokotlin


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import java.util.ArrayList
import java.util.HashSet

class lista : AppCompatActivity() {
    var ward: Button? = null
    var salir: Button? = null
    var eliminar: Button? = null
    var pal_lista = ArrayList<String>()
    var ar: ArrayList<String> = ArrayList()
    var list: TextView? = null
    var res = ""
    var del: EditText? = null
    var `in`: EditText? = null
    var existe: Boolean = false
    var wardExist = true
    var valido = false
    private var cuadro: AlertDialog.Builder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        ward = findViewById(R.id.brn_guardar)
        salir = findViewById(R.id.brn_regresar)
        eliminar = findViewById(R.id.brn_eliminar)
        del = findViewById(R.id.et_eliminar)
        `in` = findViewById(R.id.et_guardar)
        //list = findViewById(R.id.lista)
        val list: TextView = findViewById<TextView>(R.id.lista)
        cuadro = AlertDialog.Builder(this@lista)

        datoslista()
        for (i in pal_lista.indices) {
            res += pal_lista[i] + "\n"
        }
        if (pal_lista.isEmpty()) {
            res = "--Lista vacia--"
        }
        list.setText(res)
    }

    fun back(v: View) {
        val i = Intent(this@lista, MainActivity::class.java)
        startActivity(i)
    }


    fun datoslista() {
        val palabras = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val pal = palabras.getStringSet("words", null)
        if (pal == null) {
            pal_lista.clear()
        } else {
            pal_lista.clear()
            convertir(pal)
        }
    }

    fun convertir(a: Set<String>) {
        val arra = arrayOfNulls<String>(a.size)
        var i = 0
        for (s in a) {
            arra[i++] = s
        }
        for (j in arra.indices) {
            arra[j]?.let { pal_lista.add(it) }
        }
    }

    fun eliminar(v: View) {
        val word = del?.text.toString()
        verificar(word)
        if (valido == true) {
            existe(word)
            if (existe == true) {
                eliminarDatos(word)
                datos()
                Toast.makeText(this@lista, word.toUpperCase() + " eliminada!", Toast.LENGTH_SHORT).show()
            } else {
                cuadro!!.setTitle("Error")
                cuadro!!.setMessage("La palabra no existe")
                cuadro!!.setNeutralButton("Corregir") { dialog, which -> del?.setText("") }
                cuadro!!.setCancelable(false)
                cuadro!!.show()
                datos()
            }
        }
    }

    fun agregar(v: View) {
        val word = `in`?.text.toString()
        verificar(word)
        if (valido == true) {
            existe(word)
            if (existe == false) {
                guardarDatos(word)
                datos()
                Toast.makeText(this@lista, word.toUpperCase() + " agregada!", Toast.LENGTH_SHORT).show()
            } else {
                cuadro!!.setTitle("Error")
                cuadro!!.setMessage("La palabra existe")
                cuadro!!.setNeutralButton("Corregir") { dialog, which -> del?.setText("") }
                cuadro!!.setCancelable(false)
                cuadro!!.show()
                datos()
            }
        }
    }

    fun existe(a: String) {
        var a = a
        a = a.toUpperCase()
        val palabras = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val pal = palabras.getStringSet("words", null)
        if (pal == null) {
            existe = false

        } else {
            if (pal.contains(a)) {
                existe = true
            } else {
                existe = false
            }
        }
    }

    fun guardarDatos(a: String) {
        var a = a
        a = a.toUpperCase()
        val palabras = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val pal = HashSet<String>()
        helper()
        if (wardExist == true) {
            for (j in ar.indices) {
                val b = ar[j]
                pal.add(b)
            }
        }
        pal.add(a)
        val edit = palabras.edit()
        edit.putStringSet("words", pal)
        edit.commit()
    }

    fun eliminarDatos(a: String) {
        var a = a
        a = a.toUpperCase()
        val palabras = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val pal = HashSet<String>()
        helper()
        if (wardExist == true) {
            for (j in ar.indices) {
                val b = ar[j]
                pal.add(b)
            }
        }
        pal.remove(a)
        val edit = palabras.edit()
        edit.putStringSet("words", pal)
        edit.commit()
    }

    fun helper() {
        ar.clear()
        val palabras = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val pal = palabras.getStringSet("words", null)
        if (pal == null) {
            wardExist = false
        } else {
            wardExist = true
            val arra = arrayOfNulls<String>(pal.size)
            var i = 0
            for (s in pal) {
                arra[i++] = s
            }
            for (j in arra.indices) {
                arra[j]?.let { ar.add(it) }
            }
        }
    }

    fun datos() {
        del?.setText("")
        `in`?.setText("")
        list?.setText("")
        pal_lista.clear()
        res = ""
        datoslista()
        for (i in pal_lista.indices) {
            res += pal_lista[i] + "\n"
        }
        if (pal_lista.isEmpty()) {
            res = "--Lista vacia--"
        }
        list?.setText(res)
    }

    fun verificar(a: String) {
        var a = a
        a = a.toUpperCase()
        var voc = 0
        var con = 0
        if (a.length > 1 && a.length < 26) {
            val vocon = a.toCharArray()
            for (i in vocon.indices) {
                if (vocon[i] == 'A' || vocon[i] == 'E' || vocon[i] == 'I' || vocon[i] == 'O' || vocon[i] == 'U') {
                    voc++
                } else {
                    con++
                }
            }
            if (voc > 1 && con > 1) {
                valido = true
            } else {
                cuadro!!.setTitle("Error")
                cuadro!!.setMessage("La palabra debe de tener entre 2 o mas vocales y consonantes")
                cuadro!!.setNeutralButton("Corregir") { dialog, which -> del?.setText("") }
                cuadro!!.setCancelable(false)
                cuadro!!.show()
                valido = false
            }
        } else {
            cuadro!!.setTitle("Error")
            cuadro!!.setMessage("La palabra debe de tener entre 2 y 25 letras")
            cuadro!!.setNeutralButton("Corregir") { dialog, which -> del?.setText("") }
            cuadro!!.setCancelable(false)
            cuadro!!.show()
            valido = false
        }
    }
}