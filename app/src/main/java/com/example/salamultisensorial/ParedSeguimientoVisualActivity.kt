package com.example.salamultisensorial

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_pared_seguimiento_visual.*
import kotlinx.android.synthetic.main.activity_pared_seguimiento_visual.buttonFechaSesion
import kotlinx.android.synthetic.main.activity_pared_seguimiento_visual.fechaSesionTV
import java.util.*

class ParedSeguimientoVisualActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var radioGroupAtencion:RadioGroup
    private lateinit var radioGroupEquilibrio:RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""
    private var paciente:String? = ""
    private var profesional: String? = ""
    private var anoG = 0
    private var mesG = 0
    private var diaG = 0

    private var completoV = false
    private var completoA = false
    private var completoE = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pared_seguimiento_visual)

        supportActionBar?.title = "Pared de Seguimiento Visual"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recuperar parámetros enviados desde AuthActivity con bundle
        val bundle: Bundle? = intent.extras
        profesional = bundle?.getString("profesional")
        paciente = bundle?.getString("paciente")
        pacienteDni = bundle?.getString("pacienteDni")

        // Deshabilitar opciones hasta que se seleccione una fecha de sesión
        atencionOpcion1.isEnabled = false
        atencionOpcion2.isEnabled = false
        atencionOpcion3.isEnabled = false
        atencionOpcion4.isEnabled = false
        atencionOpcion5.isEnabled = false
        equilibrioOpcion1.isEnabled = false
        equilibrioOpcion2.isEnabled = false
        equilibrioOpcion3.isEnabled = false
        equilibrioOpcion4.isEnabled = false
        equilibrioOpcion5.isEnabled = false
        integracionVisomotora1.isEnabled = false
        integracionVisomotora2.isEnabled = false
        integracionVisomotora3.isEnabled = false
        integracionVisomotora4.isEnabled = false
        integracionVisomotora5.isEnabled = false


        profesionalSeleccionadoTV.text = "Profesional: $profesional"
        pacienteSeleccionadoTV.text = "Paciente: $paciente"

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
                val ayudaIntent = Intent(this, AyudaParedActivity::class.java)
                startActivity(ayudaIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
                fechaSesionTV.text = "Fecha de sesión: $dia-$mesG-$ano"

                // Habilitar opciones una vez que se seleccionó la fecha
                atencionOpcion1.isEnabled = true
                atencionOpcion2.isEnabled = true
                atencionOpcion3.isEnabled = true
                atencionOpcion4.isEnabled = true
                atencionOpcion5.isEnabled = true
                equilibrioOpcion1.isEnabled = true
                equilibrioOpcion2.isEnabled = true
                equilibrioOpcion3.isEnabled = true
                equilibrioOpcion4.isEnabled = true
                equilibrioOpcion5.isEnabled = true
                integracionVisomotora1.isEnabled = true
                integracionVisomotora2.isEnabled = true
                integracionVisomotora3.isEnabled = true
                integracionVisomotora4.isEnabled = true
                integracionVisomotora5.isEnabled = true

                if(mesG<=9) {
                    mesBD = "0$mesG"
                }else{
                    mesBD = "$mesG"
                }

                if(diaG<=9){
                    diaBD = "0$diaG"
                }else{
                    diaBD = "$diaG"
                }

                fechaSesion = "$anoG$mesBD$diaBD"
                valorX = "$diaG/$mesG"
            }, ano, mes, dia)
            dpd.show()
        }
    }

    fun atencion(view: View) {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Capacidad de Atención Pared")
            .child(fechaSesion.toString())

        if (view is RadioButton) {
                // Is the button now checked?
                val checked = view.isChecked
                if (checked){
                    completoA = true
                }
                // Check which radio button was clicked
                when (view.getId()) {
                    R.id.atencionOpcion1 ->
                        if(checked){
                        dbReference.child("y").setValue(0)
                        dbReference.child("x").setValue(valorX)
                    }
                    R.id.atencionOpcion2 ->
                        if(checked){
                        dbReference.child("y").setValue(1)
                        dbReference.child("x").setValue(valorX)
                    }
                    R.id.atencionOpcion3 ->
                        if(checked){
                        dbReference.child("y").setValue(2)
                        dbReference.child("x").setValue(valorX)
                    }
                    R.id.atencionOpcion4 ->
                        if(checked){
                        dbReference.child("y").setValue(3)
                        dbReference.child("x").setValue(valorX)
                    }
                    R.id.atencionOpcion5 ->
                        if(checked){
                        dbReference.child("y").setValue(4)
                        dbReference.child("x").setValue(valorX)
                    }
                }
            }

    }

    fun equilibrio(view: View) {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Equilibrio Postural Pared")
            .child(fechaSesion.toString())

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            if (checked){
                completoE = true
            }
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.equilibrioOpcion1 ->
                    if(checked){
                        dbReference.child("y").setValue(0)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.equilibrioOpcion2 ->
                    if(checked){
                        dbReference.child("y").setValue(1)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.equilibrioOpcion3 ->
                    if(checked){
                        dbReference.child("y").setValue(2)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.equilibrioOpcion4 ->
                    if(checked){
                        dbReference.child("y").setValue(3)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.equilibrioOpcion5 ->
                    if(checked){
                        dbReference.child("y").setValue(4)
                        dbReference.child("x").setValue(valorX)
                    }
            }
        }
    }

    fun visomotora(view: View) {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Visomotora Pared")
            .child(fechaSesion.toString())

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            if (checked){
                completoV = true
            }

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.integracionVisomotora1 ->
                    if(checked){
                        dbReference.child("y").setValue(0)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.integracionVisomotora2 ->
                    if(checked){
                        dbReference.child("y").setValue(1)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.integracionVisomotora3 ->
                    if(checked){
                        dbReference.child("y").setValue(2)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.integracionVisomotora4 ->
                    if(checked){
                        dbReference.child("y").setValue(3)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.integracionVisomotora5 ->
                    if(checked){
                        dbReference.child("y").setValue(4)
                        dbReference.child("x").setValue(valorX)
                    }
            }
        }
    }

    fun cargarPared(view: View) {
        if (fechaSesion.equals("") || fechaSesionTV.text.equals("Fecha de la sesión: ") ){
            mensajeAlerta("Seleccione la fecha de la sesión")
        }else {
            if (completoV && completoA && completoE) {
                fechaSesionTV.text = "Fecha de la sesión: "
                Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_LONG).show()
                completoA = false
                completoE = false
                completoV = false
                val inicioIntent = Intent(this, InicioSesionActivity::class.java)
                startActivity(inicioIntent)
            } else {
                Toast.makeText(this, "Responda todas las preguntas", Toast.LENGTH_LONG).show()
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