package com.example.salamultisensorial

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_panel_interactivo.*
import kotlinx.android.synthetic.main.mensaje_alerta.*
import java.security.AccessController.getContext
import java.util.*

class PanelInteractivoActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference

    //Declaración variables globales
    private lateinit var simonCB: CheckBox
    private lateinit var sonidosCB: CheckBox
    private lateinit var colchonetaCB: CheckBox
    private lateinit var tableroCB: CheckBox
    private lateinit var flMemoria : FrameLayout
    private lateinit var flEquilibrio : FrameLayout
    private lateinit var flAtencion : FrameLayout
    private lateinit var cargarDatos : Button
    private lateinit var chau : Date
    private var paciente:String? = ""
    private var pacienteDni:String? = ""
    private var profesional: String? = ""
    private var fechaSesion: String? = ""
    private var valorX: String? = ""
    private var anoG = 0
    private var mesG = 0
    private var diaG = 0

    val memoriaFragment = MemoriaFragment()
    val equilibrioFragment = EquilibrioFragment()
    val atencionFragment = AtencionFragment()
    val motricidadFinaFragment = MotricidadFinaFragment()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_interactivo)

        supportActionBar?.title = "Evaluación Panel Interactivo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recuperar parámetros enviados desde AuthActivity con bundle
        val bundle: Bundle? = intent.extras
        profesional = bundle?.getString("profesional")
        paciente = bundle?.getString("paciente")
        pacienteDni = bundle?.getString("pacienteDni")

        simonCB= findViewById(R.id.simonCB)
        sonidosCB= findViewById(R.id.sonidosCB)
        colchonetaCB= findViewById(R.id.colchonetaCB)
        tableroCB= findViewById(R.id.tableroCB)

        flAtencion = findViewById(R.id.flAtencion)
        flMemoria = findViewById(R.id.flMemoria)
        flEquilibrio = findViewById(R.id.flEquilibrio)

        cargarDatos = findViewById(R.id.cargarDatos)

        profSeleccionadoTV.text = "Profesional: $profesional"
        pacSeleccionadoTV.text = "Paciente: $paciente"

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
                val ayudaIntent = Intent(this, AyudaPanelActivity::class.java)
                startActivity(ayudaIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun seleccionFecha() {
        val c = Calendar.getInstance()
        val ano = c.get(Calendar.YEAR)
        val mes = c.get(Calendar.MONTH)
        val dia = c.get(Calendar.DAY_OF_MONTH)


        buttonFechaSesion.setOnClickListener {
            var mesBD = ""
            var diaBD = ""
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, ano, mes, dia ->
                mesG = mes+1
                diaG = dia
                anoG = ano
                fechaSesionTV.text = "Fecha de la sesión: $dia-$mesG-$ano"

                val cal = Calendar.getInstance()
                cal.set(anoG,mes,diaG, 0, 0, 0)
                val sdf = SimpleDateFormat("d/M/y")
                chau = cal.time
                println(sdf.format(chau))

                mesBD = if(mesG<=9) {
                    "0$mesG"
                }else{
                    "$mesG"
                }

                diaBD = if(diaG<=9){
                    "0$diaG"
                }else{
                    "$diaG"
                }
                //hola = c.set(anoG,mesG,diaG)
                fechaSesion = "$anoG$mesBD$diaBD"
                valorX = "$diaG/$mesG"
            }, ano, mes, dia)
            dpd.show()
            
        }

    }

    fun opciones(view: View) {

        flEquilibrio.removeAllViews()
        flMemoria.removeAllViews()
        flAtencion.removeAllViews()
        flMotricidadFina.removeAllViews()

        if (fechaSesion.equals("") ){
            mensajeAlerta("Seleccione la fecha de la sesión")
        }else {
            //Desplegar el fragment adecuado según la configuración de uso
            if(simonCB.isChecked){
                memoria()
                atencion()
            }
            if(sonidosCB.isChecked){
                atencion()

            }
            if(colchonetaCB.isChecked){
                equilibrio()
            }
            if(tableroCB.isChecked){
                motricidadFina()
            }

        }
        cargarDatos.isEnabled = true
        //cargarDatos.setBackgroundColor(Color.parseColor("#B870A8"))
        cargarDatos.setBackgroundResource(R.drawable.buttonprincipalstyle)
        cargarDatos.setTextColor(Color.parseColor("#D0D8E0"))
    }

    private fun memoria(){
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)
        bundle.putString("chau", chau.toString())

        //Pasar los datos del bundle al MemoriaFragment()
        memoriaFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            remove(memoriaFragment)
            replace(R.id.flMemoria, memoriaFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
    }
    private fun equilibrio(){
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)

        //Pasar los datos del bundle al EquilibrioFragment()
        equilibrioFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            remove(equilibrioFragment)
            replace(R.id.flEquilibrio, equilibrioFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
    }
    private fun atencion(){
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)

        //Pasar los datos del bundle al AtencionFragment()
        atencionFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            remove(atencionFragment)
            replace(R.id.flAtencion, atencionFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
    }
    private fun motricidadFina(){
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)

        //Pasar los datos del bundle al AtencionFragment()
        motricidadFinaFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            remove(motricidadFinaFragment)
            replace(R.id.flMotricidadFina, motricidadFinaFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
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

    fun cargar(view: View) {
        Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_LONG).show()

    }

}