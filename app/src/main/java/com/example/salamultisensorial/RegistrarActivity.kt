package com.example.salamultisensorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_registrar.*

class RegistrarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar) //Conectar con su layout

    title="Registrarse"

    configuracion()
    }

    //Función de configuración
    private fun configuracion(){

        //Conectar con layout Profesional o Paciente cuando se presione un botón
        buttonTerapeuta.setOnClickListener {
            val terapeutaIntent = Intent(this, ProfesionalesActivity::class.java)
            startActivity(terapeutaIntent)
        }
        buttonPaciente.setOnClickListener {
            val pacienteIntent = Intent(this, PacienteActivity::class.java)
            startActivity(pacienteIntent)
        }
    }

}