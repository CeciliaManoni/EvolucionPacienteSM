package com.example.salamultisensorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_principal.*

class PrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme_NoActionBar) //Tema de la app
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal) //Conectar con su layout

        configuracion()
    }

    //Función de configuración
    private fun configuracion(){

        //Conectar con layout Registar o InicioSesión cuando se presione un botón
        buttonRegistrarse.setOnClickListener {
            val principalIntent = Intent(this, RegistrarActivity::class.java)
            startActivity(principalIntent)
        }

        buttonInicio.setOnClickListener {
            val inicioIntent = Intent(this, InicioSesionActivity::class.java)
            startActivity(inicioIntent)
        }

    }

}


