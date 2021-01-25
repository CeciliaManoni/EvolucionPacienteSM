package com.example.salamultisensorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_memoria.*
import java.util.*

class MemoriaFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var comunicador: Comunicador

    private var valoresGuardadas: MutableList<String> = ArrayList()

    private lateinit var memoriaOpcion1: RadioButton
    private lateinit var memoriaOpcion2: RadioButton
    private lateinit var memoriaOpcion3: RadioButton
    private lateinit var memoriaOpcion4: RadioButton
    private lateinit var memoriaOpcion5: RadioButton
    private lateinit var radioGroupMemoria: RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""
    private var seleccionMemoria:String? = ""
    private var completoM = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_memoria, container, false)

        comunicador = activity as Comunicador

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
        seleccionMemoria = arguments?.getString("seleccionMemoria")

        memoria()
        return view
    }

   fun memoria() {
       dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
           .child(pacienteDni.toString()).child("Memoria Panel")

       radioGroupMemoria.setOnCheckedChangeListener { radioGroup, checkedId ->
           when (checkedId) {
               R.id.memoriaOpcion1 -> {
                   dbReference.child(fechaSesion.toString()).child("y").setValue(0)
                   dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                   dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                   completoM = true
                   comunicador.completeMe(completoM)
               }
               R.id.memoriaOpcion2 ->{
                   dbReference.child(fechaSesion.toString()).child("y").setValue(1)
                   dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                   dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                   completoM = true
                   comunicador.completeMe(completoM)
               }
               R.id.memoriaOpcion3 ->{
                   dbReference.child(fechaSesion.toString()).child("y").setValue(2)
                   dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                   dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                   completoM = true
                   comunicador.completeMe(completoM)
               }
               R.id.memoriaOpcion4 ->{
                   dbReference.child(fechaSesion.toString()).child("y").setValue(3)
                   dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                   dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                   completoM = true
                   comunicador.completeMe(completoM)
               }
               R.id.memoriaOpcion5 ->{
                   dbReference.child(fechaSesion.toString()).child("y").setValue(4)
                   dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                   dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                   completoM = true
                   comunicador.completeMe(completoM)
               }
           }
       }

       if(seleccionMemoria?.toBoolean()!!){
           dbReference.addValueEventListener(object : ValueEventListener {
               override fun onCancelled(error: DatabaseError) {

               }

               override fun onDataChange(snapshot: DataSnapshot) {
                   if (snapshot.exists()) {
                       var i = 0f
                       valoresGuardadas.clear() //Limpiar datos anteriores
                       for (ds in snapshot.children) {
                           if (ds.key!! < fechaSesion.toString()) {
                               val valor = ds.child("y").value.toString()
                               valoresGuardadas.add(valor)
                               i += 1
                           }
                       }
                       seleccionMemoria = "false"
                       if (valoresGuardadas.isNotEmpty()) {
                           dbReference.child(fechaSesion.toString()).child("y")
                               .setValue(valoresGuardadas[i.toInt() - 1])
                           dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                           dbReference.child(fechaSesion.toString()).child("real").setValue("false")
                       }else{
                           dbReference.child(fechaSesion.toString()).child("y").setValue(0)
                           dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                           dbReference.child(fechaSesion.toString()).child("real").setValue("false")
                       }
                   }else{
                       dbReference.child(fechaSesion.toString()).child("y").setValue(0)
                       dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                       dbReference.child(fechaSesion.toString()).child("real").setValue("false")
                   }
               }
           }) }

   }
}