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
import java.util.*

class PacienteActivity : AppCompatActivity() {

    //Declaración de variables globales
    private lateinit var nombrePacienteET: EditText
    private lateinit var dniPacienteET: EditText
    private lateinit var diagnosticoET: EditText
    private lateinit var fechaDeNacimiento: TextView
    private lateinit var buttonFecha: Button
    private lateinit var dbReference: DatabaseReference
    private var fecha = ""
    private var anoG = 0
    private var mesG = 0
    private var diaG = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente) //Conectar con su layout

        supportActionBar?.title="Registro del paciente"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Instanciar variables con id del layout
        nombrePacienteET = findViewById(R.id.nombrePacienteET)
        dniPacienteET = findViewById(R.id.dniPacienteET)
        diagnosticoET = findViewById(R.id.diagnostico)
        fechaDeNacimiento = findViewById(R.id.fechaDeNacimiento)
        buttonFecha = findViewById(R.id.buttonFecha)

        //Crear instancia con firebase y referenciar a la base de datos
        //en el nodo "Pacientes"
        dbReference = FirebaseDatabase.getInstance().getReference("Pacientes")

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
                val ayudaIntent = Intent(this, AyudaRegistroActivity::class.java)
                startActivity(ayudaIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Función para elección de fecha de nacimiento del paciente
    fun seleccionFecha() {

        //Objeto tipo calendar
        val c = Calendar.getInstance()
        val ano = c.get(Calendar.YEAR)
        val mes = c.get(Calendar.MONTH)
        val dia = c.get(Calendar.DAY_OF_MONTH)

        //Generar la ventana desplegable con fechas a elegir
        buttonFecha.setOnClickListener {
            val dpd =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, ano, mes, dia ->
                    mesG = mes + 1
                    diaG = dia
                    anoG = ano
                    fechaDeNacimiento.text = "$dia-$mesG-$ano"
                }, ano, mes, dia)
            dpd.show()

        }

    }

    //Función onClick botón cargar datos
    fun cargarDatosPaciente(view: View) {
        val nombre = nombrePacienteET.text.toString()
        val dni = dniPacienteET.text.toString()
        fecha = fechaDeNacimiento.text.toString()
        val diagnostico = diagnosticoET.text.toString()

        //setError si los campos están vacíos
        if (nombre.isEmpty()){
            nombrePacienteET.error = "Ingrese el nombre del paciente"
        }
        if (dni.isEmpty()){
            dniPacienteET.error = "Ingrese el DNI del paciente"
        }
        if (diagnostico.isEmpty()){
            diagnosticoET.error = "Ingrese el diagnóstico por CUD del paciente"
        }

        //Corroborar que los campos tienen contenido
        if(fecha == ""){
            mensajeAlerta("Seleccione la fecha de nacimiento del paciente")
        }else {
            if (nombre.isNotEmpty() && dni.isNotEmpty() && diagnostico.isNotEmpty()) {
                //Cargar los datos del paciente en la base de datos
                dbReference.child(dni).setValue(Pacientes(nombre, fecha, diagnostico))
                Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_LONG).show()
                val inicioIntent = Intent(this, PrincipalActivity::class.java)
                startActivity(inicioIntent)
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