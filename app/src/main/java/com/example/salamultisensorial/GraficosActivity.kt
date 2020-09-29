package com.example.salamultisensorial

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class GraficosActivity : AppCompatActivity() {

    private lateinit var fechaInicioTV: TextView
    private lateinit var fechaFinTV: TextView
    private lateinit var buttonFechaInicio: Button
    private lateinit var buttonFechaFin: Button
    private var pacienteDni:String? = ""
    private var valorX: String? = ""

    //Variables para selección de fecha
    private var fechaInicioG = ""
    private var diaInicio = 0
    private var mesInicio = 0
    private var anoInicio = 0
    private var fechaFinG = ""
    private var diaFin = 0
    private var mesFin = 0
    private var anoFin = 0

    //Constantes para instanciar los fragments
    private val lineaFragment = LineaFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graficos)

        supportActionBar?.title = "Evolución Paciente"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recuperar parámetros enviados desde AuthActivity con bundle
        val bundle: Bundle? = intent.extras
        pacienteDni = bundle?.getString("pacienteDni")
        valorX = bundle?.getString("valor_x")

        //Instanciar variable con su id del layout
        buttonFechaInicio=findViewById(R.id.buttonFechaInicio)
        buttonFechaFin=findViewById(R.id.buttonFechaFin)
        fechaInicioTV=findViewById(R.id.fechaInicioTV)
        fechaFinTV=findViewById(R.id.fechaFinTV)

        seleccionFecha()
    }

    //Agregar botón ayuda en ActionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_ayuda, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Agregar funcionalidad al botón de ayuda
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ayuda -> {
                val ayudaIntent = Intent(this, AyudaGraficosActivity::class.java)
                startActivity(ayudaIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Función para seleccionar la fecha de inicio y fin de evaluación del paciente
    private fun seleccionFecha() {
        val c = Calendar.getInstance()
        val ano = c.get(Calendar.YEAR)
        val mes = c.get(Calendar.MONTH)
        val dia = c.get(Calendar.DAY_OF_MONTH)

        buttonFechaInicio.setOnClickListener {
            var diaBD: String
            var mesBD: String
            val dpd = DatePickerDialog(this, {view, ano, mes, dia ->
                diaInicio = dia
                mesInicio = mes+1
                anoInicio = ano

                fechaInicioTV.text = "Desde: $dia-$mesInicio-$ano"

                mesBD = if(mesInicio<=9){
                    "0$mesInicio"
                }else{
                    "$mesInicio"
                }
                diaBD = if(diaInicio<=9){
                    "0$diaInicio"
                }else{
                    "$diaInicio"
                }

                fechaInicioG = "$anoInicio$mesBD$diaBD"
            }, ano, mes, dia)
            dpd.show()
        }

        buttonFechaFin.setOnClickListener {
            var diaBD: String
            var mesBD: String
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, ano, mes, dia ->
                diaFin = dia
                mesFin = mes+1
                anoFin = ano

                fechaFinTV.text = "Hasta: $dia-$mesFin-$ano"

                mesBD = if(mesFin<=9){
                    "0$mesFin"
                }else{
                    "$mesFin"
                }
                diaBD = if(diaFin<=9){
                    "0$diaFin"
                }else{
                    "$diaFin"
                }
                fechaFinG = "$anoFin$mesBD$diaBD"
            }, ano, mes, dia)
            dpd.show()
        }
    }

    fun graficar(view: View) {
        //Crear un bundle para poder enviar todos los datos con putString
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaInicio", fechaInicioG)
        bundle.putString("fechaFin", fechaFinG)

        if (fechaFinG == "" || fechaInicioG == ""){
            mensajeAlerta("Seleccione el período de evolución a graficar")
        }else {
            //Hacer el grafico de línea
            //Pasar los datos del bundle al FragmentSecond()
            lineaFragment.arguments = bundle
            //Cambiar fragment actual por FragmentSecond()
            supportFragmentManager.beginTransaction().apply {
                remove(lineaFragment)
                replace(R.id.flFragment, lineaFragment)
                //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun mensajeAlerta(mensaje:String) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.mensaje_alerta, null)
        val mensajeError = dialogView.findViewById<TextView>(R.id.mensajeError)
        val buttonAceptar = dialogView.findViewById<Button>(R.id.buttonAceptar)
        mensajeError.text = mensaje
        builder.setView(dialogView)
        val dialog: AlertDialog = builder.create()
        buttonAceptar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}