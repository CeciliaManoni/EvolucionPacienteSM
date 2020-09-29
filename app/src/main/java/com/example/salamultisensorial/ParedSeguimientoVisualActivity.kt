package com.example.salamultisensorial

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_panel_interactivo.*
import kotlinx.android.synthetic.main.activity_pared_seguimiento_visual.*
import kotlinx.android.synthetic.main.activity_pared_seguimiento_visual.buttonFechaSesion
import kotlinx.android.synthetic.main.activity_pared_seguimiento_visual.fechaSesionTV
import java.util.*

class ParedSeguimientoVisualActivity : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference

    private lateinit var atencionOpcion1: RadioButton
    private lateinit var atencionOpcion2: RadioButton
    private lateinit var atencionOpcion3: RadioButton
    private lateinit var atencionOpcion4: RadioButton
    private lateinit var atencionOpcion5: RadioButton
    private lateinit var equilibrioOpcion1: RadioButton
    private lateinit var equilibrioOpcion2: RadioButton
    private lateinit var equilibrioOpcion3: RadioButton
    private lateinit var equilibrioOpcion4: RadioButton
    private lateinit var equilibrioOpcion5: RadioButton

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""
    private var paciente:String? = ""
    private var profesional: String? = ""
    private var anoG = 0
    private var mesG = 0
    private var diaG = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pared_seguimiento_visual)

        supportActionBar?.title = "Evaluación Pared de Seguimiento Visual"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        equilibrioOpcion1 = findViewById(R.id.equilibrioOpcion1)
        equilibrioOpcion2 = findViewById(R.id.equilibrioOpcion2)
        equilibrioOpcion3 = findViewById(R.id.equilibrioOpcion3)
        equilibrioOpcion4 = findViewById(R.id.equilibrioOpcion4)
        equilibrioOpcion5 = findViewById(R.id.equilibrioOpcion5)
        atencionOpcion1 = findViewById(R.id.atencionOpcion1)
        atencionOpcion2 = findViewById(R.id.atencionOpcion2)
        atencionOpcion3 = findViewById(R.id.atencionOpcion3)
        atencionOpcion4 = findViewById(R.id.atencionOpcion4)
        atencionOpcion5 = findViewById(R.id.atencionOpcion5)

        //Recuperar parámetros enviados desde AuthActivity con bundle
        val bundle: Bundle? = intent.extras
        profesional = bundle?.getString("profesional")
        paciente = bundle?.getString("paciente")
        pacienteDni = bundle?.getString("pacienteDni")

        profesionalSeleccionadoTV.text = "Profesional: $profesional"
        pacienteSeleccionadoTV.text = "Paciente: $paciente"

        seleccionFecha()
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
                fechaSesionTV.text = "Fecha de la sesión: $dia-$mesG-$ano"

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
            .child(pacienteDni.toString()).child("Capacidad de Atención")
            .child(fechaSesion.toString())

        if (view is RadioButton) {
                // Is the button now checked?
                val checked = view.isChecked

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
            .child(pacienteDni.toString()).child("Equilibrio Postural")
            .child(fechaSesion.toString())

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

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
            .child(pacienteDni.toString()).child("Visomotora Superior")
            .child(fechaSesion.toString())

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

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
        Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_LONG).show()
    }

}