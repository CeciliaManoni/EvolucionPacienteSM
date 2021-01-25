package com.example.salamultisensorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_equilibrio.*

class EquilibrioFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var comunicador: Comunicador
    private lateinit var equilibrioOpcion1: RadioButton
    private lateinit var equilibrioOpcion2: RadioButton
    private lateinit var equilibrioOpcion3: RadioButton
    private lateinit var equilibrioOpcion4: RadioButton
    private lateinit var equilibrioOpcion5: RadioButton

    private var valoresGuardadas: MutableList<String> = ArrayList()

    private lateinit var radioGroupEquilibrio: RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""
    private var seleccionEquilibrio:String? = ""

    private var completoE = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_equilibrio, container, false)

        comunicador = activity as Comunicador

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
        seleccionEquilibrio = arguments?.getString("seleccionEquilibrio")

        equilibrio()

        return view
    }

    private fun equilibrio() {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Equilibrio Postural Panel")

        radioGroupEquilibrio.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.equilibrioOpcion1 -> {
                    dbReference.child(fechaSesion.toString()).child("y").setValue(0)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                    completoE = true
                    comunicador.completeEq(completoE)
                }
                R.id.equilibrioOpcion2 -> {
                    dbReference.child(fechaSesion.toString()).child("y").setValue(1)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                    completoE = true
                    comunicador.completeEq(completoE)
                }
                R.id.equilibrioOpcion3 -> {
                    dbReference.child(fechaSesion.toString()).child("y").setValue(2)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                    completoE = true
                    comunicador.completeEq(completoE)
                }
                R.id.equilibrioOpcion4 -> {
                    dbReference.child(fechaSesion.toString()).child("y").setValue(3)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                    completoE = true
                    comunicador.completeEq(completoE)
                }
                R.id.equilibrioOpcion5 -> {
                    dbReference.child(fechaSesion.toString()).child("y").setValue(4)
                    dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                    dbReference.child(fechaSesion.toString()).child("real").setValue("true")
                    completoE = true
                    comunicador.completeEq(completoE)
                }
            }
        }

        if (seleccionEquilibrio?.toBoolean()!!) {
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
                        seleccionEquilibrio = "false"
                    }else{
                        dbReference.child(fechaSesion.toString()).child("y").setValue(0)
                        dbReference.child(fechaSesion.toString()).child("x").setValue(valorX)
                        dbReference.child(fechaSesion.toString()).child("real").setValue("false")
                    }
                }
            })
        }
    }
}