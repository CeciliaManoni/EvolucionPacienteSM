package com.example.salamultisensorial

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_panel_interactivo.*
import java.util.*

class PanelInteractivoActivity : AppCompatActivity(), Comunicador {

    // Instanciamiento de vistas del layout
    private lateinit var flMemoria : FrameLayout
    private lateinit var flEquilibrio : FrameLayout
    private lateinit var flAtencion : FrameLayout
    private lateinit var flMotricidadFina : FrameLayout
    private lateinit var flBotonSiguiente : FrameLayout
    private lateinit var flBotonCargar : FrameLayout
    private lateinit var flDiscColores : FrameLayout
    private lateinit var flIntegracionVisomotora : FrameLayout
    private lateinit var radioGroupEquilibrio:RadioGroup

    //Declaración variables globales

    private var seleccionEquilibrio = false
    private var seleccionMotricidad = false
    private var seleccionMemoria = false
    private var habilitarSig = false
    private var habilitarCar = false
    private var simon = false
    private var sonidos = false
    private var colchoneta = false
    private var tablero = false
    private var paciente:String? = ""
    private var pacienteDni:String? = ""
    private var profesional: String? = ""
    private var fechaSesion: String? = ""
    private var valorX: String? = ""
    private var desmarcar: Boolean = false
    private var anoG = 0
    private var mesG = 0
    private var diaG = 0
    private var completoV = false
    private var completoA = false
    private var completoE = false
    private var completoMF = false
    private var completoM = false
    private var completoDC = false

    // Instanciamiento de fragments
    val memoriaFragment = MemoriaFragment()
    val equilibrioFragment = EquilibrioFragment()
    val atencionFragment = AtencionFragment()
    val motricidadFinaFragment = MotricidadFinaFragment()
    val discColoresFragment = DiscriminacionColoresFragment()
    val integracionVisomotoraFragment = IntegracionVisomotoraFragment()
    val botonSiguienteFragment = BotonSiguienteFragment()
    val botonCargarFragment = BotonCargarFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_interactivo)

        // Crear títulos y botón ayuda
        supportActionBar?.title = "Panel Interactivo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recuperar parámetros enviados desde AuthActivity con bundle
        val bundle: Bundle? = intent.extras
        profesional = bundle?.getString("profesional")
        paciente = bundle?.getString("paciente")
        pacienteDni = bundle?.getString("pacienteDni")

        // Instanciar las vistas
        flAtencion = findViewById(R.id.flAtencion)
        flMemoria = findViewById(R.id.flMemoria)
        flEquilibrio = findViewById(R.id.flEquilibrio)
        flMotricidadFina = findViewById(R.id.flMotricidadFina)
        flDiscColores = findViewById(R.id.flDiscColores)
        flIntegracionVisomotora = findViewById(R.id.flIntegracionVisomotora)
        flBotonSiguiente = findViewById(R.id.flBotonSiguiente)
        flBotonCargar = findViewById(R.id.flBotonCargar)

        profSeleccionadoTV.text = "Profesional: $profesional"
        pacSeleccionadoTV.text = "Paciente: $paciente"

        // Función para seleccionar la fecha de la sesión
        seleccionFecha()

        // Habilitar el botón siguiente
        supportFragmentManager.beginTransaction().replace(R.id.flBotonSiguiente, botonSiguienteFragment)
            .commit()
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

    // Función para seleccionar fecha de la sesión a registrar
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
                fechaSesion = "$anoG$mesBD$diaBD"
                valorX = "$diaG/$mesG"
            }, ano, mes, dia)
            dpd.show()
            
        }

    }

    override fun pasarDatosCb(simonP: Boolean, sonidosP: Boolean, colchonetaP: Boolean, tableroP: Boolean) {
        simon = simonP
        sonidos = sonidosP
        colchoneta = colchonetaP
        tablero = tableroP

    }

    override fun completeEq(completoEq: Boolean) {
        completoE = completoEq
    }

    override fun completeAt(completoAt: Boolean) {
        completoA = completoAt
    }

    override fun completeMe(completoMe: Boolean) {
        completoM = completoMe
    }

    override fun completeMFi(completoMFi: Boolean) {
        completoMF = completoMFi
    }

    override fun completeDco(completoDco: Boolean) {
        completoDC = completoDco
    }

    override fun completeVi(completoVi: Boolean) {
        completoV = completoVi
    }

    // Función para comunicar fragments con Panel Interactivo activity
    override fun pasarDatosBtn(habilitaSig: Boolean, habilitaCar: Boolean) {
        habilitarSig = habilitaSig
        habilitarCar = habilitaCar

        // Ejecutar cuando se presiona el botón siguiente
        if(habilitarSig) {
            // Borrar los estados actuales
            flEquilibrio.removeAllViews()
            flMemoria.removeAllViews()
            flAtencion.removeAllViews()
            flMotricidadFina.removeAllViews()
            flIntegracionVisomotora.removeAllViews()
            flDiscColores.removeAllViews()

            // Corroborar que se ha seleccionado una fecha
            if (fechaSesion.equals("") || fechaSesionTV.text == "Fecha de la sesión: "){
                mensajeAlerta("Seleccione la fecha de la sesión")
            }else {
                // Corroborar que se haya seleccionado la configuración de uso
                if ((simon || sonidos) && (colchoneta || tablero)) {

                    // Deshabilitar el botón de selección de fecha sesión
                    buttonFechaSesion.isEnabled = false
                    buttonFechaSesion.visibility = View.INVISIBLE

                    // Habilitar el botón cargar datos
                    flBotonSiguiente.removeAllViews()
                    supportFragmentManager.beginTransaction().remove(botonCargarFragment)
                        .replace(R.id.flBotonCargar, botonCargarFragment)
                        .addToBackStack(null).commit()
                    habilitarSig = false

                    // Desplegar los fragments adecuados según la configuración de uso
                    if (sonidos) {
                        atencion()
                        discColores()
                        visomotora()
                        seleccionMemoria = true
                        memoria(seleccionMemoria)
                        if(colchoneta){
                            seleccionEquilibrio = false
                            equilibrio(seleccionEquilibrio)
                        }
                        else{
                            seleccionEquilibrio = true
                            equilibrio(seleccionEquilibrio)
                        }
                        if (tablero) {
                            seleccionMotricidad = false
                            motricidadFina(seleccionMotricidad)
                        }
                        else{
                            seleccionMotricidad = true
                            motricidadFina(seleccionMotricidad)
                        }
                    }
                    if (simon) {
                        seleccionMemoria = false
                        memoria(seleccionMemoria)
                        atencion()
                        discColores()
                        visomotora()
                        if(colchoneta){
                            seleccionEquilibrio = false
                            equilibrio(seleccionEquilibrio)
                        }
                        else{
                            seleccionEquilibrio = true
                            equilibrio(seleccionEquilibrio)
                        }
                        if (tablero) {
                            seleccionMotricidad = false
                            motricidadFina(seleccionMotricidad)
                        }
                        else{
                            seleccionMotricidad = true
                            motricidadFina(seleccionMotricidad)
                        }
                    }
                }else {
                    Toast.makeText(this, "Seleccione los modos de funcionamiento " +
                            "y accesorios utilizados", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Ejecutar cuando se presiona el botón cargar datos
        if(habilitarCar){

            if (sonidos){
                completoM = true
                if (colchoneta){
                    completoMF = true
                }
                if (tablero){
                    completoE = true
                }
            }
            if (simon){
                if (colchoneta){
                    completoMF = true
                }
                if (tablero){
                    completoE = true
                }
            }
            if(completoA && completoV && completoDC && completoM && completoE && completoMF) {
                completoA = false
                completoV = false
                completoDC = false
                completoM = false
                completoE = false
                completoMF = false

                // Habilitar el botón de fecha sesión
                buttonFechaSesion.isEnabled = true
                buttonFechaSesion.visibility = View.VISIBLE
                fechaSesionTV.text = "Fecha de la sesión: "

                // Habilitar el botón siguiente
                supportFragmentManager.beginTransaction().remove(botonSiguienteFragment)
                    .replace(R.id.flBotonSiguiente, botonSiguienteFragment)
                    .addToBackStack(null).commit()

                // Borrar los estados actuales
                flEquilibrio.removeAllViews()
                flMemoria.removeAllViews()
                flAtencion.removeAllViews()
                flMotricidadFina.removeAllViews()
                flDiscColores.removeAllViews()
                flIntegracionVisomotora.removeAllViews()
                flBotonCargar.removeAllViews()
                desmarcar = true

                Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_LONG).show()

                val inicioIntent = Intent(this, InicioSesionActivity::class.java)
                startActivity(inicioIntent)
            }else{
                Toast.makeText(this, "Responda todas las preguntas", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun memoria(seleccionMemoria:Boolean){
        //Pasar datos a MemoriaFragment()
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)
        bundle.putString("seleccionMemoria",seleccionMemoria.toString())
        bundle.putString("desmarcar",desmarcar.toString())

        memoriaFragment.arguments = bundle
        // Habilitar pregunta memoria
        supportFragmentManager.beginTransaction().apply {
            remove(memoriaFragment)
            replace(R.id.flMemoria, memoriaFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
        // Cargar por defecto un valor en memoria cuando sea necesario
        if(seleccionMemoria) {
            supportFragmentManager.beginTransaction().remove(memoriaFragment).commit()
        }
    }

    private fun discColores(){
        //Pasar datos a DiscColoresFragment()
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)
        discColoresFragment.arguments = bundle

        // Habilitar pregunta discriminación de colores
        supportFragmentManager.beginTransaction().apply {
            remove(discColoresFragment)
            replace(R.id.flDiscColores, discColoresFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
    }

    private fun visomotora(){
        //Pasar datos a IntegracionVisomotoraFragment()
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)
        integracionVisomotoraFragment.arguments = bundle

        // Habilitar pregunta integración visomotora
        supportFragmentManager.beginTransaction().apply {
            remove(integracionVisomotoraFragment)
            replace(R.id.flIntegracionVisomotora, integracionVisomotoraFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
    }

    private fun equilibrio(seleccionEquilibrio: Boolean){
        //Pasar datos a EquilibrioFragment()
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)
        bundle.putString("desmarcar", desmarcar.toString())
        bundle.putString("seleccionEquilibrio",seleccionEquilibrio.toString())
        equilibrioFragment.arguments = bundle

        // Habilitar pregunta equilibrio postural
        supportFragmentManager.beginTransaction().apply {
            remove(equilibrioFragment)
            replace(R.id.flEquilibrio, equilibrioFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
        if(seleccionEquilibrio) {
            supportFragmentManager.beginTransaction().remove(equilibrioFragment).commit()
        }
    }

    private fun atencion(){
        //Pasar datos a AtencionFragment()
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)
        atencionFragment.arguments = bundle

        // Habilitar pregunta capacidad de atención
        supportFragmentManager.beginTransaction().apply {
            remove(atencionFragment)
            replace(R.id.flAtencion, atencionFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
    }

    private fun motricidadFina(seleccionMotricidad: Boolean){
        //Pasar datos a MotricidadFinaFragment()
        val bundle = Bundle()
        bundle.putString("pacienteDni", pacienteDni)
        bundle.putString("fechaSesion", fechaSesion)
        bundle.putString("valorX", valorX)
        bundle.putString("seleccionMotricidad", seleccionMotricidad.toString())
        motricidadFinaFragment.arguments = bundle

        // Habilitar pregunta motricidad fina
        supportFragmentManager.beginTransaction().apply {
            remove(motricidadFinaFragment)
            replace(R.id.flMotricidadFina, motricidadFinaFragment)
            //Añadir a la cola de tareas para que cuando aprete atras no cierre la app
            addToBackStack(null)
            commit()
        }
        if(seleccionMotricidad) {
            supportFragmentManager.beginTransaction().remove(motricidadFinaFragment).commit()
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