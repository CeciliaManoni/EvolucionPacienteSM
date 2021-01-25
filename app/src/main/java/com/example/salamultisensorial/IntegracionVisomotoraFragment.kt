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

class IntegracionVisomotoraFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var comunicador: Comunicador

    private lateinit var integracionVisomotora1: RadioButton
    private lateinit var integracionVisomotora2: RadioButton
    private lateinit var integracionVisomotora3: RadioButton
    private lateinit var integracionVisomotora4: RadioButton
    private lateinit var integracionVisomotora5: RadioButton
    private lateinit var radioGroupVisomotora: RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""
    private var completoV = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_integracion_visomotora, container, false)

        comunicador = activity as Comunicador

        integracionVisomotora1 = view.findViewById(R.id.integracionVisomotora1)
        integracionVisomotora2 = view.findViewById(R.id.integracionVisomotora2)
        integracionVisomotora3 = view.findViewById(R.id.integracionVisomotora3)
        integracionVisomotora4 = view.findViewById(R.id.integracionVisomotora4)
        integracionVisomotora5 = view.findViewById(R.id.integracionVisomotora5)

        radioGroupVisomotora = view.findViewById(R.id.radioGroupVisomotora)

        //Recibir los datos enviados desde el bundle de PanelInteractivoActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaSesion = arguments?.getString("fechaSesion")
        valorX = arguments?.getString("valorX")

        visomotora()

        return view
    }

    fun visomotora() {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Visomotora Panel")
            .child(fechaSesion.toString())

        radioGroupVisomotora.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.integracionVisomotora1 -> {
                    dbReference.child("y").setValue(0)
                    dbReference.child("x").setValue(valorX)
                    completoV = true
                    comunicador.completeVi(completoV)
                }
                R.id.integracionVisomotora2 ->{
                    dbReference.child("y").setValue(1)
                    dbReference.child("x").setValue(valorX)
                    completoV = true
                    comunicador.completeVi(completoV)
                }
                R.id.integracionVisomotora3 ->{
                    dbReference.child("y").setValue(2)
                    dbReference.child("x").setValue(valorX)
                    completoV = true
                    comunicador.completeVi(completoV)
                }
                R.id.integracionVisomotora4 ->{
                    dbReference.child("y").setValue(3)
                    dbReference.child("x").setValue(valorX)
                    completoV = true
                    comunicador.completeVi(completoV)
                }
                R.id.integracionVisomotora5 ->{
                    dbReference.child("y").setValue(4)
                    dbReference.child("x").setValue(valorX)
                    completoV = true
                    comunicador.completeVi(completoV)
                }
            }
        }
    }
}