package com.example.salamultisensorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.salamultisensorial.R.array.herramientas
import com.google.firebase.database.*
import kotlin.collections.ArrayList

class InicioSesionActivity : AppCompatActivity(){
    //Declaración de variables globales
    private lateinit var herramientasSpinner: Spinner
    private lateinit var profesionesSpinner: Spinner
    private lateinit var profesionalesSpinner: Spinner
    private lateinit var pacientesSpinner: Spinner
    private lateinit var dbReference: DatabaseReference
    private var profesionSeleccionada = ""
    private var profesionalSeleccionado = ""
    private var pacienteSeleccionado = ""
    private var pacienteDni = ""
    private var pacienteElegido:Pacientes = Pacientes(pacienteDni,pacienteSeleccionado)
    private var herramientaSeleccionada = ""
    private var profesiones: MutableList<String> = ArrayList()
    private var pacientes: MutableList<Pacientes> = ArrayList()
    private var paciente: MutableList<String> = ArrayList()
    private var profesionales: MutableList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)//Conectar con su layout

        //Instanciar variables con id del layout
        herramientasSpinner = findViewById(R.id.herramientasSpinner)
        profesionesSpinner = findViewById(R.id.profesionesSpinner)
        profesionalesSpinner = findViewById(R.id.profesionalesSpinner)
        pacientesSpinner = findViewById(R.id.pacientesSpinner)

        supportActionBar?.title = "Configuración de sesión"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Crear instancia con firebase y referenciar a la base de datos
        dbReference = FirebaseDatabase.getInstance().reference

        spinnerProfesiones()
        spinnerHerramientas()
        spinnerPacientes()

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
                val ayudaIntent = Intent(this, AyudaInicioActivity::class.java)
                startActivity(ayudaIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Función para obtener los items del SpinnerProfesiones desde la base de datos
    private fun spinnerProfesiones() {

        //Búsqueda de las profesiones en la base de datos
        dbReference.child("Profesiones")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val profesionId = ds.key
                            profesiones.add(profesionId.toString())
                        }

                        arrayAdapter(profesiones,profesionesSpinner)

                        //Función acción a ejecutar cuando se selecciona un item del Spinner
                        profesionesSpinner.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?,
                                                        p2: Int, p3: Long) {
                                profesionSeleccionada = profesiones[p2]
                                spinnerProfesionales()
                                profesionales.clear()
                            }
                        }
                    }
                }
            })
    }

    private fun spinnerProfesionales() {
        //Búsqueda de los profesionales registrados en la base de datos
        dbReference.child("Profesiones").child(profesionSeleccionada)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val profesionalId = ds.child("Nombre").value
                            profesionales.add(profesionalId.toString())
                        }

                        arrayAdapter(profesionales,profesionalesSpinner)

                        //Función acción a ejecutar cuando se selecciona un item del Spinner
                        profesionalesSpinner.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?,
                                                        p2: Int, p3: Long) {
                                profesionalSeleccionado = profesionales[p2]
                            }
                        }
                    }
                }
            })
    }

    private fun spinnerPacientes() {
        //Búsqueda de los pacientes registrados en la base de datos
        dbReference.child("Pacientes")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val pacienteId = ds.key
                            val nombrePaciente:String = ds.child("nombre").value.toString()
                            pacienteElegido = Pacientes(pacienteId,nombrePaciente)
                            pacientes.add(pacienteElegido)
                            paciente.add(nombrePaciente)
                        }

                        ArrayAdapter<Pacientes>(this@InicioSesionActivity ,
                            R.layout.spinner_style, pacientes).also{
                                adapter-> adapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            // Apply the adapter to the spinner
                            pacientesSpinner.adapter = adapter
                        }

                        //Función acción a ejecutar cuando se selecciona un item del Spinner
                        pacientesSpinner.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?,
                                                        p2: Int, p3: Long) {
                                pacienteSeleccionado = p0?.getItemAtPosition(p2).toString()

                                // Búsqueda en el arreglo pacientes del dni del paciente seleccionado
                                for (i in pacientes) {
                                    if(pacienteSeleccionado == i.nombre){
                                        pacienteDni = i.dni
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }

    private fun spinnerHerramientas() {
        //Crear las vistas del Spinner a partir de el arreglo "herramientas"
        val adapter = ArrayAdapter.createFromResource(this, herramientas, R.layout.spinner_style)
        herramientasSpinner.adapter = adapter

        //Función acción a ejecutar cuando se selecciona un item del Spinner
        herramientasSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?,
                                        p2: Int, p3: Long) {
                herramientaSeleccionada = p0?.getItemAtPosition(p2).toString()
            }
        }
    }

    //Función que crear las vistas del Spinner a partir de los datos almacenados en arreglo
    fun arrayAdapter(arreglo:MutableList<String>, spinner:Spinner) {
        ArrayAdapter<String>(this@InicioSesionActivity , R.layout.spinner_style, arreglo).also{
                adapter-> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    fun completarSesion(view: View) {
        if (herramientaSeleccionada == "Panel Interactivo") {
            val panelIntent = Intent(this, PanelInteractivoActivity::class.java).apply {
                putExtra("profesional", profesionalSeleccionado)
                putExtra("paciente", pacienteSeleccionado)
                putExtra("pacienteDni", pacienteDni)
            }
            startActivity(panelIntent)
        }
        if (herramientaSeleccionada == "Pared de Seguimiento Visual") {
            val paredIntent = Intent(this, ParedSeguimientoVisualActivity::class.java).apply {
                putExtra("profesional", profesionalSeleccionado)
                putExtra("paciente", pacienteSeleccionado)
                putExtra("pacienteDni", pacienteDni)
            }
            startActivity(paredIntent)
        }
    }

    fun evolucionPaciente(view: View) {
        val graficosIntent = Intent(this, GraficosActivity::class.java).apply{
            putExtra("paciente",pacienteSeleccionado)
            putExtra("pacienteDni",pacienteDni)
            putExtra("herramientaSeleccionada", herramientaSeleccionada)
        }
        startActivity(graficosIntent)
    }

}