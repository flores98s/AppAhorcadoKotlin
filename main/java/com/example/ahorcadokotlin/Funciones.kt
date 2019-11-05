package com.example.ahorcado

import java.util.ArrayList

class Funciones {
    internal var palabras: MutableList<String> = ArrayList()
    internal var i: Int = 0
    fun Agregar(a: String) {
        palabras.add(a)
    }

    fun Mostrar(): List<String> {
        return palabras
    }

    fun Tam(): Int {
        i = palabras.size
        return i
    }
}