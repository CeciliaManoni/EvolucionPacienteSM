package com.example.salamultisensorial

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*

class ProfesionalesActivity : AppCompatActivity() {

    //Declaración de variables globales
    private lateinit var nombreProfesionalET: EditText
    private lateinit var dniProfesionalET: EditText
    private lateinit var dbReference: DatabaseReference
    private lateinit var spinner: Spinner
    private var profesionSeleccionada = "Seleccione su profesión"
    private var profesiones: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profesionales) //Conectar con su layout
        supportActionBar?.title = "Registro del Profesional"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Instanciar variables con id del layout
        nombreProfesionalET = findViewById(R.id.nombreProfesionalET)
        dniProfesionalET = findViewById(R.id.dniProfesionalET)
        spinner = findViewById(R.id.spinner)

        //Crear instancia con firebase y referenciar a la base de datos
        //en el nodo "Profesiones"
        dbReference = FirebaseDatabase.getInstance().getReference("Profesiones")

        configuracionSpinner()
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

    //Función para añadir opciones al spinner de profesiones
    private fun configuracionSpinner() {

        //Búsqueda de las profesiones en la base de datos
        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val profesionId = ds.key
                        profesiones.add(profesionId.toString())
                    }
                    arrayAdapter()
                    seleccionSpinner()
                }
            }
        })
    }

    //Función que crear las vistas del Spinner a partir de los datos almacenados en
    //el array profesiones
    fun arrayAdapter() {
        profesiones.add(0,"Seleccione su profesion")
        val adapter: ArrayAdapter<String> = object: ArrayAdapter<String> (this@ProfesionalesActivity,
            R.layout.spinner_style, profesiones){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view:TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                view.typeface = view.typeface

                // Estilo del item seleccionado
                if (position == spinner.selectedItemPosition && position != 0 ){
                    view.background = ColorDrawable(Color.parseColor("#646567"))
                    view.setTextColor(Color.WHITE)
                }

                // Color gris para "Seleccione su profesión"
                if(position == 0 ){
                    view.setTextColor(Color.LTGRAY)
                }

                return view
            }

            override fun isEnabled(position: Int): Boolean {
                // Deshabilitar el primer item
                return position != 0
            }
        }

        //Asignar el array adapter al Spinner
        spinner.adapter = adapter
    }

    //Función acción a ejecutar cuando se selecciona un item del Spinner
    fun seleccionSpinner(){
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: android.view.View?,
                                        p2: Int, p3: Long) {
                profesionSeleccionada = profesiones[p2]
            }
        }
    }

    //Función onClick del botón cargar datos
    fun cargarDatos(view: View) {
        val nombre = nombreProfesionalET.text.toString()
        val dni= dniProfesionalET.text.toString()

        //setError si los campos están vacíos
        if (nombre.isEmpty()){
            nombreProfesionalET.error = "Ingrese el nombre del profesional"
        }
        if (dni.isEmpty()){
            dniProfesionalET.error = "Ingrese el DNI del profesional"
        }

        //Corroborar que los campos tienen contenido
       if(nombre.isNotEmpty() && dni.isNotEmpty()){
           //Cargar los datos del profesional en la base de datos
           dbReference.child(profesionSeleccionada).child(dni).child("Nombre").setValue(nombre)
           Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_LONG).show()
       }else{
           Toast.makeText(this,"Complete todos los campos", Toast.LENGTH_LONG).show()
       }
    }
}
