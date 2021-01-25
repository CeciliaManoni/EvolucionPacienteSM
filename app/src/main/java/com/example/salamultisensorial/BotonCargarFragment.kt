package com.example.salamultisensorial

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_atencion.*
import kotlinx.android.synthetic.main.fragment_boton_cargar.view.*
import kotlinx.android.synthetic.main.fragment_equilibrio.*

class BotonCargarFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var comunicador: Comunicador
    private lateinit var observacionesPanelET: EditText
    private var habilitarSig = false
    private var habilitarCar = true
    private var fechaCompleta:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_boton_cargar, container, false)
        comunicador = activity as Comunicador

        observacionesPanelET = v.findViewById(R.id.observacionesPanelET)

        //Recibir los datos enviados desde el bundle de PanelInteractivoActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaSesion = arguments?.getString("fechaSesion")
        fechaCompleta = arguments?.getString("fechaCompleta")

        v.cargarDatos.setOnClickListener {

            // Cargar en la base de datos las observaciones del día
            if (observacionesPanelET.text.isNotEmpty()){
                dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
                    .child(pacienteDni.toString()).child("Observaciones Panel")
                    .child(fechaSesion.toString())
                dbReference.child("Observación").setValue(observacionesPanelET.text.toString())
                dbReference.child("fecha").setValue(fechaCompleta)
            }
            // Enviar el estado de los botones
            comunicador.pasarDatosBtn(habilitarSig, habilitarCar)
        }

        return v
    }

}