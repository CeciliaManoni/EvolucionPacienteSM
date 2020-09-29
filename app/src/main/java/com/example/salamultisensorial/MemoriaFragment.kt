package com.example.salamultisensorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_memoria.*
import java.util.*

class MemoriaFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference

    private lateinit var memoriaOpcion1: RadioButton
    private lateinit var memoriaOpcion2: RadioButton
    private lateinit var memoriaOpcion3: RadioButton
    private lateinit var memoriaOpcion4: RadioButton
    private lateinit var memoriaOpcion5: RadioButton
    private lateinit var radioGroupMemoria: RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""
    private var chau:String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_memoria, container, false)

        memoriaOpcion1 = view.findViewById(R.id.memoriaOpcion1)
        memoriaOpcion2 = view.findViewById(R.id.memoriaOpcion2)
        memoriaOpcion3 = view.findViewById(R.id.memoriaOpcion3)
        memoriaOpcion4 = view.findViewById(R.id.memoriaOpcion4)
        memoriaOpcion5 = view.findViewById(R.id.memoriaOpcion5)

        radioGroupMemoria = view.findViewById(R.id.radioGroupMemoria)

        //Recibir los datos enviados desde el bundle de PanelInteractivoActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaSesion = arguments?.getString("fechaSesion")
        valorX = arguments?.getString("valorX")
        chau = arguments?.getString("chau")

        println("hola")
        println(chau)
        memoria()
        return view
    }

   fun memoria() {
       dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
           .child(pacienteDni.toString()).child("Memoria").child(fechaSesion.toString())

       radioGroupMemoria.setOnCheckedChangeListener { radioGroup, checkedId ->
           when (checkedId) {
               R.id.memoriaOpcion1 -> {
                      dbReference.child("y").setValue(0)
                      dbReference.child("x").setValue(valorX)
                   }
               R.id.memoriaOpcion2 ->{
                       dbReference.child("y").setValue(1)
                       dbReference.child("x").setValue(valorX)
                   }
               R.id.memoriaOpcion3 ->{
                       dbReference.child("y").setValue(2)
                       dbReference.child("x").setValue(valorX)
                   }
               R.id.memoriaOpcion4 ->{
                       dbReference.child("y").setValue(3)
                       dbReference.child("x").setValue(valorX)
                   }
               R.id.memoriaOpcion5 ->{
                       dbReference.child("y").setValue(4)
                       dbReference.child("x").setValue(valorX)
                   }
           }
       }

   }
}