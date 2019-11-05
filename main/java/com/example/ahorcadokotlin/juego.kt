package com.example.ahorcadokotlin


import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import java.sql.Array
import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet

class juego : AppCompatActivity() {

    private var monito: ImageView? = null
    private var btnTirar: Button? = null
    private var errores: Int = 0
    private var palabra: String? = null
    private var tv_lineas: TextView? = null
    private var points: TextView? = null
    private var et_letra: EditText? = null
    var guiones = ArrayList<String>()
    var hearts = ArrayList<String>()
    var pals = ArrayList<String>()
    var ar: ArrayList<String> = ArrayList()
    var res = ""
    var cora = ""
    var vida = 5
    var resta: Int = 0
    var num: Int = 0
    private var msg: AlertDialog.Builder? = null
    var corazon: TextView? = null
    var timer: Chronometer? = null
    var inicio: Double = 0.toDouble()
    private var cuadro: AlertDialog.Builder? = null
    var puntosT: Int = 0
    var timepartida: Int = 0
    var exist = true
    var stop = false
    var wardExist = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        msg = AlertDialog.Builder(this@juego)
        monito = findViewById(R.id.iv_img)
        btnTirar = findViewById(R.id.btn_ing)
        palabra = intent.getStringExtra("palabra")
        palabra = palabra!!.toUpperCase()
        tv_lineas = findViewById(R.id.tv_lin)
        et_letra = findViewById(R.id.et_letra)
        corazon = findViewById(R.id.heart)
        timer = findViewById(R.id.crono)
        points = findViewById(R.id.points)
        cuadro = AlertDialog.Builder(this@juego)

        val puntos = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val pts = puntos.getInt("puntos", 0).toString()
        points!!.text = pts

        timepartida = palabra!!.length * 8 * 1000
        object : CountDownTimer(timepartida.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (stop == true) {
                    cancel()
                } else {
                    this@juego.timer?.text = "Tiempo restante " + millisUntilFinished / 1000
                }
            }

            override fun onFinish() {
                MediaPlayer.create(this@juego, R.raw.lose).start()
                msg!!.setTitle("GAME OVER")
                msg!!.setMessage("Se te acabo el tiempo :( \nLa palabra era " + palabra!!)
                msg!!.setNegativeButton("Salir") { dialog, which ->
                    val i = Intent(this@juego, MainActivity::class.java)
                    startActivity(i)
                }
                msg!!.setCancelable(false)
                msg!!.show()
            }
        }.start()
        inicio = System.currentTimeMillis().toDouble()


        val p = palabra!!.toCharArray()
        for (i in 0 until palabra!!.length) {
            guiones.add("_")
        }
        for (i in 0 until palabra!!.length) {
            res += guiones[i] + " "
        }
        for (i in 0..4) {
            hearts.add("❤")
        }
        for (i in 0..4) {
            cora += hearts[i] + " "
        }

        corazon?.text = cora
        tv_lineas!!.text = res
        errores = p.size
        resta = p.size


    }

    fun tirar(v: View) {
        val p = palabra!!.toCharArray()
        val letra = et_letra!!.text.toString().toUpperCase()
        if (letra.length > 0) {
            val letter = letra.get(0)
            var i = 0
            var acierto = ""
            errores = p.size
            while (i < p.size) {
                if (p[i] == letter) {
                    acierto = letter.toString()
                    if (pals.contains(acierto)) {
                        Toast.makeText(this, "Ya ingresaste esa letra", Toast.LENGTH_SHORT).show()
                        MediaPlayer.create(this@juego, R.raw.no).start()
                        i = p.size
                    } else {
                        guiones[i] = acierto
                        resta--
                        i++
                        Toast.makeText(this, "Letra encontrada", Toast.LENGTH_SHORT).show()
                        MediaPlayer.create(this@juego, R.raw.punto).start()
                    }
                } else {
                    errores--
                    i++
                }
            }
            if (pals.contains(letra)) {
                exist = true
                Toast.makeText(this, "Ya ingresaste eso", Toast.LENGTH_SHORT).show()
            } else {
                exist = false
            }
            if (errores <= 0 && exist == false) {
                vida--
                Toast.makeText(this, "Letra incorrecta", Toast.LENGTH_SHORT).show()
                MediaPlayer.create(this@juego, R.raw.no).start()
            }
            et_letra!!.setText("")
            when (vida) {
                0 -> monito!!.setImageResource(R.drawable.ah5)

                1 -> monito!!.setImageResource(R.drawable.ah4)

                2 -> monito!!.setImageResource(R.drawable.ah3)

                3 -> monito!!.setImageResource(R.drawable.ah2)

                4 -> monito!!.setImageResource(R.drawable.ah1)

                5 -> monito!!.setImageResource(R.drawable.ah0)
            }
            pals.add(letra)
            datos()
            if (vida <= 0 || resta <= 0) {
                val fin = System.currentTimeMillis()
                val tiempo = (fin - inicio) / 1000
                timer?.stop()
                stop = true
                num = tiempo.toInt()
                val hor = num / 3600
                val min = (num - 3600 * hor) / 60
                val seg = num - (hor * 3600 + min * 60)
                if (vida <= 0) {
                    MediaPlayer.create(this@juego, R.raw.lose).start()
                    msg!!.setTitle("GAME OVER")
                    msg!!.setMessage("Se te acabaron las vidas :( \nLa palabra era " + palabra!!)
                    msg!!.setNegativeButton("Salir") { dialog, which ->
                        val i = Intent(this@juego, MainActivity::class.java)
                        startActivity(i)
                    }
                    msg!!.setCancelable(false)
                    msg!!.show()
                } else {
                    MediaPlayer.create(this@juego, R.raw.win).start()
                    sumarPuntos()
                    insertarPalabra(palabra!!)
                    msg!!.setTitle("GANADOR")
                    msg!!.setMessage("Felicidades :D \nLa palabra era $palabra\nTiempo: $hor hrs : $min min : $seg seg \nPuntos acumulados: $puntosT")
                    msg!!.setNegativeButton("Salir") { dialog, which ->
                        val i = Intent(this@juego, MainActivity::class.java)
                        startActivity(i)
                    }
                    msg!!.setCancelable(false)
                    msg!!.show()
                }
            }
        } else {
            cuadro!!.setTitle("Error")
            cuadro!!.setMessage("Debes de ingresar una letra :)")
            cuadro!!.setNeutralButton("Corregir") { dialog, which -> et_letra!!.setText("") }
            cuadro!!.setCancelable(false)
            cuadro!!.show()
        }
    }

    private fun sumarPuntos() {
        puntosT = Integer.parseInt(points!!.text.toString())
        puntosT++
        val puntos = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val edit = puntos.edit()
        edit.putInt("puntos", puntosT)
        edit.commit()
    }

    fun datos() {
        cora = ""
        for (i in 0 until vida) {
            hearts.add("❤")
        }
        for (i in 0 until vida) {
            cora += hearts[i] + " "
        }
        corazon?.text = cora

        res = ""
        for (i in 0 until palabra!!.length) {
            res += guiones[i] + " "
        }
        tv_lineas!!.text = res
    }

    fun insertarPalabra(a: String) {
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

    fun helper() {
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

    override fun onBackPressed() {

    }
}