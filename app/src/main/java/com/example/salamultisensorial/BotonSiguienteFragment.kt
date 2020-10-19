package com.example.salamultisensorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import kotlinx.android.synthetic.main.fragment_boton_siguiente.view.*

class BotonSiguienteFragment : Fragment() {

    private lateinit var comunicador: Comunicador
    private lateinit var simonCB: CheckBox
    private lateinit var sonidosCB: CheckBox
    private lateinit var colchonetaCB: CheckBox
    private lateinit var tableroCB: CheckBox

    private var simon = false
    private var sonidos = false
    private var colchoneta = false
    private var tablero = false
    private var habilitarSig = true
    private var habilitarCar = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_boton_siguiente, container, false)

        comunicador = activity as Comunicador

        simonCB = v.findViewById(R.id.simonCB)
        sonidosCB = v.findViewById(R.id.sonidosCB)
        tableroCB = v.findViewById(R.id.tableroCB)
        colchonetaCB = v.findViewById(R.id.colchonetaCB)

        v.buttonOpciones.setOnClickListener {
            if (simonCB.isChecked){
                simon = true
            }
            if (sonidosCB.isChecked){
                sonidos = true
            }
            if (colchonetaCB.isChecked){
                colchoneta = true
            }
            if (tableroCB.isChecked){
                tablero = true
            }

            comunicador.pasarDatosCb(simon,sonidos,colchoneta,tablero)
            comunicador.pasarDatosBtn(habilitarSig, habilitarCar)

            simonCB.isChecked = false
            sonidosCB.isChecked = false
            colchonetaCB.isChecked = false
            tableroCB.isChecked = false
            simon = false
            sonidos = false
            colchoneta = false
            tablero = false

        }

        return v
    }

}