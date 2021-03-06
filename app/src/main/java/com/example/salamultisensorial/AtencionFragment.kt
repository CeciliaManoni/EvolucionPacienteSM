package com.example.salamultisensorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AtencionFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var comunicador: Comunicador

    private lateinit var atencionOpcion1: RadioButton
    private lateinit var atencionOpcion2: RadioButton
    private lateinit var atencionOpcion3: RadioButton
    private lateinit var atencionOpcion4: RadioButton
    private lateinit var atencionOpcion5: RadioButton
    private lateinit var radioGroupAtencion: RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""

    private var completoA = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_atencion, container, false)

        comunicador = activity as Comunicador

        atencionOpcion1 = view.findViewById(R.id.atencionOpcion1)
        atencionOpcion2 = view.findViewById(R.id.atencionOpcion2)
        atencionOpcion3 = view.findViewById(R.id.atencionOpcion3)
        atencionOpcion4 = view.findViewById(R.id.atencionOpcion4)
        atencionOpcion5 = view.findViewById(R.id.atencionOpcion5)

        radioGroupAtencion = view.findViewById((R.id.radioGroupAtencion))

        //Recibir los datos enviados desde el bundle de PanelInteractivoActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaSesion = arguments?.getString("fechaSesion")
        valorX = arguments?.getString("valorX")

        capacidadAtencion()

        return view
    }

    fun capacidadAtencion() {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Capacidad de Atenci??n Panel")


        radioGroupAtencion.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.atencionOpcion1 ->{
                    dbReference.child(fechaSesion.toString()).child("y").setValue(0)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    completoA = true
                    comunicador.completeAt(completoA)
                }
                R.id.atencionOpcion2 ->{
                    dbReference.child(fechaSesion.toString()).child("y").setValue(1)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    completoA = true
                    comunicador.completeAt(completoA)
                }
                R.id.atencionOpcion3 ->{
                    dbReference.child(fechaSesion.toString()).child("y").setValue(2)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    completoA = true
                    comunicador.completeAt(completoA)
                }
                R.id.atencionOpcion4 -> {
                    dbReference.child(fechaSesion.toString()).child("y").setValue(3)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    completoA = true
                    comunicador.completeAt(completoA)
                }
                R.id.atencionOpcion5 -> {
                    dbReference.child(fechaSesion.toString()).child("y").setValue(4)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    completoA = true
                    comunicador.completeAt(completoA)
                }
            }
        }
    }

}