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

class DiscriminacionColoresFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var comunicador: Comunicador

    private lateinit var discOpcion1: RadioButton
    private lateinit var discOpcion2: RadioButton
    private lateinit var discOpcion3: RadioButton
    private lateinit var discOpcion4: RadioButton
    private lateinit var discOpcion5: RadioButton
    private lateinit var radioGroupDisc: RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""
    private var completoDC = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_discriminacion_colores, container, false)

        comunicador = activity as Comunicador

        discOpcion1 = view.findViewById(R.id.discOpcion1)
        discOpcion2 = view.findViewById(R.id.discOpcion2)
        discOpcion3 = view.findViewById(R.id.discOpcion3)
        discOpcion4 = view.findViewById(R.id.discOpcion4)
        discOpcion5 = view.findViewById(R.id.discOpcion5)

        radioGroupDisc = view.findViewById(R.id.radioGroupDisc)

        //Recibir los datos enviados desde el bundle de PanelInteractivoActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaSesion = arguments?.getString("fechaSesion")
        valorX = arguments?.getString("valorX")

        discColores()
        return view
    }

    fun discColores() {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Discriminación de Colores")
            .child(fechaSesion.toString())

        radioGroupDisc.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.discOpcion1 -> {
                    dbReference.child("y").setValue(0)
                    dbReference.child("x").setValue(valorX)
                    completoDC = true
                    comunicador.completeDco(completoDC)
                }
                R.id.discOpcion2 ->{
                    dbReference.child("y").setValue(1)
                    dbReference.child("x").setValue(valorX)
                    completoDC = true
                    comunicador.completeDco(completoDC)
                }
                R.id.discOpcion3 ->{
                    dbReference.child("y").setValue(2)
                    dbReference.child("x").setValue(valorX)
                    completoDC = true
                    comunicador.completeDco(completoDC)
                }
                R.id.discOpcion4 ->{
                    dbReference.child("y").setValue(3)
                    dbReference.child("x").setValue(valorX)
                    completoDC = true
                    comunicador.completeDco(completoDC)
                }
                R.id.discOpcion5 ->{
                    dbReference.child("y").setValue(4)
                    dbReference.child("x").setValue(valorX)
                    completoDC = true
                    comunicador.completeDco(completoDC)
                }
            }
        }
    }
}