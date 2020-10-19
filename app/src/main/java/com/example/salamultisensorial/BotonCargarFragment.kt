package com.example.salamultisensorial

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.fragment_atencion.*
import kotlinx.android.synthetic.main.fragment_boton_cargar.view.*
import kotlinx.android.synthetic.main.fragment_equilibrio.*

class BotonCargarFragment : Fragment() {

    private lateinit var comunicador: Comunicador
    private var habilitarSig = false
    private var habilitarCar = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_boton_cargar, container, false)
        comunicador = activity as Comunicador

        v.cargarDatos.setOnClickListener {
            comunicador.pasarDatosBtn(habilitarSig, habilitarCar)
        }

        return v
    }

}