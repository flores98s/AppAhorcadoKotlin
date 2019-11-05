package com.example.ahorcadokotlin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle


import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import java.util.ArrayList
import java.util.HashSet

class MainActivity : AppCompatActivity() {

    private var etpalabra: EditText? = null
    private var next: Button? = null
    private var list: Button? = null
    private var cuadro: AlertDialog.Builder? = null
    private var palabra: String? = null
    var check: Boolean = false
    var valido: Boolean = false
    var existe: Boolean = false
    internal var correctos = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Y", "Z", "Ñ", "Á", "É", "Í", "Ó", "Ú")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etpalabra = findViewById(R.id.et_word)
        next = findViewById(R.id.btn_next)
        list = findViewById(R.id.btn_lista)
        cuadro = AlertDialog.Builder(this@MainActivity)
    }

    fun validarPalabra(v: View) {
        var voc = 0
        var con = 0
        palabra = etpalabra!!.text.toString()
        val vocon = palabra!!.toUpperCase().toCharArray()
        for (i in vocon.indices) {
            if (vocon[i] == 'A' || vocon[i] == 'E' || vocon[i] == 'I' || vocon[i] == 'O' || vocon[i] == 'U') {
                voc++
            } else {
                con++
            }
        }
        validaPalabra(palabra!!)
        if (palabra!!.length >= 2 && palabra!!.length <= 25 && valido == true && palabra!!.trim { it <= ' ' } !== " " && existe == false && voc > 1 && con > 1) {
            iniciarJuego()
        } else {
            cuadro!!.setTitle("Error")
            cuadro!!.setMessage("La palabra debe de tener entre 2 y 25 letras\nTambien debe de tener 2 o mas vocales y dos o mas consonantes\nEso o ya la ingresaste anteriormente :)")
            cuadro!!.setNeutralButton("Corregir") { dialog, which -> etpalabra!!.setText("") }
            cuadro!!.setCancelable(false)
            cuadro!!.show()
        }
    }

    fun iniciarJuego() {
        val i = Intent(this@MainActivity, juego::class.java)
        i.putExtra("palabra", palabra)
        startActivity(i)
    }

    fun validaPalabra(a: String) {
        existePalabra(a)
        val p = a.toCharArray()
        for (i in p.indices) {
            var let = p[i].toString()
            let = let.toUpperCase()
            validaLetra(let)
            if (check == false) {
                valido = false
            } else {
                valido = true
            }
        }
    }

    fun validaLetra(a: String) {
        var b = 0
        for (i in correctos.indices) {
            if (correctos[i] == a) {
                b++
            }
        }
        if (b > 0) {
            check = true
        } else {
            check = false
        }

    }

    private fun existePalabra(a: String) {
        var a = a
        a = a.toUpperCase()
        val palabras = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val pal = palabras.getStringSet("words", null)
        if (pal != null) {
            if (pal.contains(a)) {
                existe = true
            } else {
                existe = false
            }
        } else {
            existe = false
        }
    }

    fun lista(v: View) {
        val i = Intent(this@MainActivity, lista::class.java)
        startActivity(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        val palabras = getSharedPreferences("datos", Context.MODE_PRIVATE)
        palabras.edit().clear().commit()
    }
}