package com.example.salamultisensorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AyudaRegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda_registro)

        val alto = resources.displayMetrics.heightPixels
        val ancho = resources.displayMetrics.widthPixels

        window.setLayout((ancho*0.8).toInt(), (alto*0.3).toInt())
    }
}