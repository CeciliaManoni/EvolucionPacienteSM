package com.example.salamultisensorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AyudaInicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda_inicio)

        val alto = resources.displayMetrics.heightPixels
        val ancho = resources.displayMetrics.widthPixels

        window.setLayout((ancho*0.85).toInt(), (alto*0.80).toInt())
    }
}