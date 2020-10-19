package com.example.salamultisensorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_motricidad_fina.*

class MotricidadFinaFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var dbReference2: DatabaseReference
    private lateinit var comunicador: Comunicador

    private var valoresGuardadas: MutableList<String> = ArrayList()

    private lateinit var motricidadFinaOpcion1: RadioButton
    private lateinit var motricidadFinaOpcion2: RadioButton
    private lateinit var motricidadFinaOpcion3: RadioButton
    private lateinit var motricidadFinaOpcion4: RadioButton
    private lateinit var motricidadFinaOpcion5: RadioButton

    private lateinit var radioGroupMotricidadFina: RadioGroup

    private var valorX:String? = ""
    private var fechaSesion:String? = ""
    private var pacienteDni:String? = ""
    private var seleccionMotricidadFina:String? = ""
    private var completoMF = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_motricidad_fina, container, false)
        comunicador = activity as Comunicador

        motricidadFinaOpcion1 = view.findViewById(R.id.motricidadFinaOpcion1)
        motricidadFinaOpcion2 = view.findViewById(R.id.motricidadFinaOpcion2)
        motricidadFinaOpcion3 = view.findViewById(R.id.motricidadFinaOpcion3)
        motricidadFinaOpcion4 = view.findViewById(R.id.motricidadFinaOpcion4)
        motricidadFinaOpcion5 = view.findViewById(R.id.motricidadFinaOpcion5)

        radioGroupMotricidadFina = view.findViewById((R.id.radioGroupMotricidadFina))

        //Recibir los datos enviados desde el bundle de PanelInteractivoActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaSesion = arguments?.getString("fechaSesion")
        valorX = arguments?.getString("valorX")
        seleccionMotricidadFina = arguments?.getString("seleccionMotricidad")

        motricidadFina()

        return view
    }

    fun motricidadFina() {
        dbReference = FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Motricidad Fina")
            .child(fechaSesion.toString())

        radioGroupMotricidadFina.setOnCheckedChangeListener { radioGroup, checkedId ->
            // Check which radio button was clicked
            when (checkedId) {
                R.id.motricidadFinaOpcion1 ->{
                    dbReference.child("y").setValue(0)
                    dbReference.child("x").setValue(valorX)
                    completoMF = true
                    comunicador.completeMFi(completoMF)
                }
                R.id.motricidadFinaOpcion2 ->{
                    dbReference.child("y").setValue(1)
                    dbReference.child("x").setValue(valorX)
                    completoMF = true
                    comunicador.completeMFi(completoMF)
                }
                R.id.motricidadFinaOpcion3 ->{
                    dbReference.child("y").setValue(2)
                    dbReference.child("x").setValue(valorX)
                    completoMF = true
                    comunicador.completeMFi(completoMF)

                }
                R.id.motricidadFinaOpcion4 -> {
                    dbReference.child("y").setValue(3)
                    dbReference.child("x").setValue(valorX)
                    completoMF = true
                    comunicador.completeMFi(completoMF)
                }
                R.id.motricidadFinaOpcion5 -> {
                    dbReference.child("y").setValue(4)
                    dbReference.child("x").setValue(valorX)
                    completoMF = true
                    comunicador.completeMFi(completoMF)
                }
            }
        }
        dbReference2= FirebaseDatabase.getInstance().reference.child("Pacientes")
            .child(pacienteDni.toString()).child("Motricidad Fina")

        if(seleccionMotricidadFina?.toBoolean()!!){
            dbReference2.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var i = 0f
                        valoresGuardadas.clear() //Limpiar datos anteriores
                        for (ds in snapshot.children) {
                            if (ds.key!! <= fechaSesion.toString()) {
                                val valor = ds.child("y").value.toString()
                                valoresGuardadas.add(valor)
                                i += 1
                            }
                        }
                        dbReference.child("y").setValue(valoresGuardadas[i.toInt()-1])
                        dbReference.child("x").setValue(valorX)
                        seleccionMotricidadFina = "false"
                    }
                }
            })
        }

    }

}