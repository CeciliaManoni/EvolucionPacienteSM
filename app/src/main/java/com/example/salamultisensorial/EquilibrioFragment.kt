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
import kotlinx.android.synthetic.main.fragment_equilibrio.*

class EquilibrioFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference

    private lateinit var equilibrioOpcion1: RadioButton
    private lateinit var equilibrioOpcion2: RadioButton
    private lateinit var equilibrioOpcion3: RadioButton
    private lateinit var equilibrioOpcion4: RadioButton
    private lateinit var equilibrioOpcion5: RadioButton

    private lateinit var radioGroupEquilibrio: RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_equilibrio, container, false)

        equilibrioOpcion1 = view.findViewById(R.id.equilibrioOpcion1)
        equilibrioOpcion2 = view.findViewById(R.id.equilibrioOpcion2)
        equilibrioOpcion3 = view.findViewById(R.id.equilibrioOpcion3)
        equilibrioOpcion4 = view.findViewById(R.id.equilibrioOpcion4)
        equilibrioOpcion5 = view.findViewById(R.id.equilibrioOpcion5)

        radioGroupEquilibrio = view.findViewById(R.id.radioGroupEquilibrio)

        //Recibir los datos enviados desde el bundle de PanelInteractivoActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaSesion = arguments?.getString("fechaSesion")
        valorX = arguments?.getString("valorX")

        equilibrio()

        return view
    }

    fun equilibrio() {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Equilibrio Postural").child(fechaSesion.toString())

        radioGroupEquilibrio.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.equilibrioOpcion1 ->{
                        dbReference.child("y").setValue(0)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.equilibrioOpcion2 -> {
                        dbReference.child("y").setValue(1)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.equilibrioOpcion3 ->{
                        dbReference.child("y").setValue(2)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.equilibrioOpcion4 -> {
                        dbReference.child("y").setValue(3)
                        dbReference.child("x").setValue(valorX)
                    }
                R.id.equilibrioOpcion5 -> {
                        dbReference.child("y").setValue(4)
                        dbReference.child("x").setValue(valorX)
                    }
            }
        }

    }
}