package com.example.salamultisensorial

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.*
import kotlinx.android.synthetic.*


class GraficosPanelFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference

    //Variables de observacion
    private lateinit var spinnerObservaciones: Spinner
    private var arrayObservaciones:MutableList<Observaciones> = ArrayList()
    private var observacionSeleccionada = ""
    private var fechaId = ""
    private var observacionElegida:Observaciones = Observaciones(fechaId,observacionSeleccionada)
    private lateinit var observacionesTV : TextView

    //-------------------------------Variables gráficos de línea-------------------------------//

    //Parámetro memoria
    private lateinit var lineChartMemoria: LineChart
    private lateinit var lineDataSetM: LineDataSet
    private lateinit var lineDataM: LineData
    private lateinit var xAxisM: XAxis
    private lateinit var legendM: Legend
    private lateinit var descripcionM: Description
    private lateinit var buttonDescargarM : Button
    private val valorDatosLineaM: MutableList<Entry> = ArrayList()
    private var fechasArrayM: MutableList<String> = ArrayList()

    //Parámetro equilibrio postural
    private lateinit var lineChartEquilibrio: LineChart
    private lateinit var lineDataSetE: LineDataSet
    private lateinit var lineDataE: LineData
    private lateinit var xAxisE: XAxis
    private lateinit var legendE: Legend
    private lateinit var descripcionE: Description
    private lateinit var buttonDescargarE : Button
    private val valorDatosLineaE: MutableList<Entry> = ArrayList()
    private var fechasArrayE: MutableList<String> = ArrayList()

    //Parámetro capacidad de atención
    private lateinit var lineChartAtencion: LineChart
    private lateinit var lineDataSetA: LineDataSet
    private lateinit var lineDataA: LineData
    private lateinit var xAxisA: XAxis
    private lateinit var legendA: Legend
    private lateinit var descripcionA: Description
    private lateinit var buttonDescargarA : Button
    private val valorDatosLineaA: MutableList<Entry> = ArrayList()
    private var fechasArrayA: MutableList<String> = ArrayList()

    //Parámetro motricidad fina
    private lateinit var lineChartMFina: LineChart
    private lateinit var lineDataSetMF: LineDataSet
    private lateinit var lineDataMF: LineData
    private lateinit var xAxisMF: XAxis
    private lateinit var legendMF: Legend
    private lateinit var descripcionMF: Description
    private lateinit var buttonDescargarMF: Button
    private val valorDatosLineaMF: MutableList<Entry> = ArrayList()
    private var fechasArrayMF: MutableList<String> = ArrayList()

    //Parámetro discriminación de colores
    private lateinit var lineChartColores: LineChart
    private lateinit var lineDataSetColores: LineDataSet
    private lateinit var lineDataColores: LineData
    private lateinit var xAxisColores: XAxis
    private lateinit var legendColores: Legend
    private lateinit var descripcionColores: Description
    private lateinit var buttonDescargarColores : Button
    private val valorDatosLineaColores: MutableList<Entry> = ArrayList()
    private var fechasArrayColores: MutableList<String> = ArrayList()

    //Parámetro integración visomotora
    private lateinit var lineChartVisomotora: LineChart
    private lateinit var lineDataSetV: LineDataSet
    private lateinit var lineDataV: LineData
    private lateinit var xAxisV: XAxis
    private lateinit var legendV: Legend
    private lateinit var descripcionV: Description
    private lateinit var buttonDescargarV : Button
    private val valorDatosLineaV: MutableList<Entry> = ArrayList()
    private var fechasArrayV: MutableList<String> = ArrayList()


    //-----------------------------------Evolución integral-------------------------------------//
    // Variables gráfico de línea
    private lateinit var lineChart: LineChart
    private var lineData: LineData = LineData()
    private lateinit var xAxis: XAxis
    private lateinit var yAxis: YAxis
    private lateinit var legend : Legend
    private lateinit var descripcionL: Description
    private lateinit var buttonDescargarL : Button
    private var memoria = false
    private var atencion = false
    private var equilibrio = false
    private var motricidad = false
    private var discriminacion = false
    private var visomotora = false
    private lateinit var memoriaCB : CheckBox
    private lateinit var atencionCB : CheckBox
    private lateinit var equilibrioCB : CheckBox
    private lateinit var discriminacionCB : CheckBox
    private lateinit var motricidadCB : CheckBox
    private lateinit var visomotoraCB : CheckBox

    // Variables gráfico radar
    private lateinit var radarChart: RadarChart
    private lateinit var radarDataSet: RadarDataSet
    private lateinit var radarData: RadarData
    private lateinit var xAxisr: XAxis
    private lateinit var yAxisr: YAxis
    private lateinit var buttonDescargarR : Button
    private val valorDatosRadar: MutableList<RadarEntry> = ArrayList()
    private var promedioMemoria = 0f
    private var promedioEquilibro = 0f
    private var promedioAtencion = 0f
    private var promedioMFina = 0f
    private var promedioColores = 0f
    private var promedioVisomotora = 0f
    private val arrayParametros = arrayListOf(" M", " E ", " A ", " MF ", " DC ", " IVM ")

    //Variables con datos de la sesión
    private var datoX =0f
    private var pos = 0 //Variable de selección de configuración de gráficos
    private var pacienteDni:String? = ""
    private var fechaInicio:String? = ""
    private var fechaFin:String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_graficos_panel, container, false)

        lineChartMemoria = view.findViewById(R.id.lineChartMemoria)
        lineChartAtencion = view.findViewById(R.id.lineChartAtencion)
        lineChartEquilibrio = view.findViewById(R.id.lineChartEquilibrio)
        lineChartMFina = view.findViewById(R.id.lineChartMFina)
        lineChartColores = view.findViewById(R.id.lineChartColores)
        lineChartVisomotora = view.findViewById(R.id.lineChartVisomotora)
        lineChart = view.findViewById(R.id.lineChart)
        radarChart = view.findViewById(R.id.radarChart)
        buttonDescargarM = view.findViewById(R.id.buttonDescargarM)
        buttonDescargarE = view.findViewById(R.id.buttonDescargarE)
        buttonDescargarA = view.findViewById(R.id.buttonDescargarA)
        buttonDescargarMF = view.findViewById(R.id.buttonDescargarMF)
        buttonDescargarColores = view.findViewById(R.id.buttonDescargarColores)
        buttonDescargarV = view.findViewById(R.id.buttonDescargarV)
        buttonDescargarR = view.findViewById(R.id.buttonDescargarRadar)
        buttonDescargarL = view.findViewById(R.id.buttonDescargarLinea)
        memoriaCB = view.findViewById(R.id.memoriaCB)
        atencionCB = view.findViewById(R.id.atencionCB)
        equilibrioCB = view.findViewById(R.id.equilibrioCB)
        discriminacionCB = view.findViewById(R.id.discriminacionCB)
        visomotoraCB = view.findViewById(R.id.visomotoraCB)
        motricidadCB = view.findViewById(R.id.motricidadFinaCB)
        observacionesTV = view.findViewById(R.id.observacionesTV)
        spinnerObservaciones = view.findViewById(R.id.spinnerObservaciones)

        descripcionM = lineChartMemoria.description
        descripcionE = lineChartEquilibrio.description
        descripcionA = lineChartAtencion.description
        descripcionMF = lineChartMFina.description
        descripcionColores = lineChartColores.description
        descripcionV = lineChartVisomotora.description
        descripcionL = lineChart.description

        //Recibir los datos enviados desde el bundle de MainActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaInicio = arguments?.getString("fechaInicio")
        fechaFin = arguments?.getString("fechaFin")

        //Ubicar la referencia de la bd en la carpeta del paciente seleccionado
        dbReference = FirebaseDatabase.getInstance().getReference("Pacientes").
        child(pacienteDni.toString())

        lineData = LineData()
        lineChart.clear()
        lineChart.invalidate()

        sinDatos(lineChartMemoria)
        sinDatos(lineChartVisomotora)
        sinDatos(lineChartEquilibrio)
        sinDatos(lineChartAtencion)
        sinDatos(lineChartMFina)
        sinDatos(lineChartColores)
        sinDatos(lineChart)

        graficar()

        descagar(buttonDescargarM, lineChartMemoria, descripcionM, "#204078", "Memoria")
        descagar(buttonDescargarMF, lineChartMFina, descripcionMF, "#98E088" , "Motricidad Fina")
        descagar(buttonDescargarColores, lineChartColores, descripcionColores, "#F090E0", "Discriminación de Colores")
        descagar(buttonDescargarE, lineChartEquilibrio, descripcionE, "#0080C8", "Equilibrio Postural")
        descagar(buttonDescargarA, lineChartAtencion, descripcionA, "#02D6E4", "Capacidad de atención")
        descagar(buttonDescargarV, lineChartVisomotora, descripcionV,"#B82870", "Integración Viso-Motora" )
        descagar(buttonDescargarL, lineChart, descripcionL, "", "")


        graficarLinea()
        seleccionObservacion()

        return view
    }

    // Función para descargar los gráficos
    private fun descagar(buttonDescargar: Button, lineChart: LineChart, descripcion: Description, color: String, parametro: String) {

        buttonDescargar.setOnClickListener {
            if (color != "") {
                descripcion(descripcion, color, parametro)
            }
            if(lineChart.saveToGallery(System.currentTimeMillis().toString(), 85)){
              Toast.makeText(context, "Guardado", Toast.LENGTH_LONG).show()
            }
            descripcion.isEnabled = false
        }
    }

    private fun descripcion(descripcion: Description, color: String, parametro: String){
        descripcion.isEnabled = true
        descripcion.textSize = 25f
        descripcion.textColor = Color.parseColor(color)
        descripcion.text = parametro
        descripcion.yOffset = 285f
    }

    private fun seleccionObservacion() {
        //Búsqueda de las obervaciones en la base de datos
        dbReference.child("Observaciones Panel")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        arrayObservaciones.clear()
                        for (ds in snapshot.children) {
                            if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                                val fechaId = ds.child("fecha").value.toString()
                                val observacion = ds.child("Observación").value.toString()
                                observacionElegida = Observaciones(fechaId, observacion)
                                arrayObservaciones.add(observacionElegida)
                            }
                        }

                        val seleccioneSuObservacion = Observaciones("Seleccione la fecha", "No hay datos para mostrar")
                        arrayObservaciones.add(0, seleccioneSuObservacion)
                        val adapter: ArrayAdapter<Observaciones> =
                            object : ArrayAdapter<Observaciones>(
                                context!!,
                                R.layout.spinner_style2, arrayObservaciones
                            ) {
                                override fun getDropDownView(
                                    position: Int,
                                    convertView: View?,
                                    parent: ViewGroup
                                ): View {
                                    val view: TextView = super.getDropDownView(
                                        position,
                                        convertView,
                                        parent
                                    ) as TextView
                                    view.typeface = view.typeface

                                    // Color gris para "Seleccione su profesión"
                                    if (position == 0) {
                                        view.setTextColor(Color.LTGRAY)
                                    }

                                    return view
                                }

                                override fun isEnabled(position: Int): Boolean {
                                    // Deshabilitar el primer item
                                    return position != 0
                                }
                            }

                        //Asignar el array adapter al Spinner
                        spinnerObservaciones.adapter = adapter

                        //Función acción a ejecutar cuando se selecciona un item del Spinner
                        spinnerObservaciones.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?,
                                                        p2: Int, p3: Long) {
                                observacionSeleccionada = p0?.getItemAtPosition(p2).toString()
                                for (i in arrayObservaciones) {
                                    if(observacionSeleccionada == i.fechaObservacion){
                                        observacionesTV.text = i.observacion.toLowerCase()
                                    }
                                }

                            }
                        }
                    }
                }
            })
    }
    //Funcino para graficar linea integral
    fun graficarLinea() {

        memoriaCB.setOnClickListener(View.OnClickListener { view ->
            if ((view as CompoundButton).isChecked) {
                memoria = true
                println("Checked")
                println("parametro dentro $memoria")
            } else {
                println("Un-Checked")
                memoria = false
                println(memoria)
            }
            lineData = LineData()
            lineChart.clear()
            lineChart.invalidate()
            graficar()
        })

        atencionCB.setOnClickListener(View.OnClickListener { view ->
            if ((view as CompoundButton).isChecked) {
                atencion = true
                println("Checked")
            } else {
                println("Un-Checked")
                atencion = false
            }
            lineData = LineData()
            lineChart.clear()
            lineChart.invalidate()
            graficar()
        })

        equilibrioCB.setOnClickListener(View.OnClickListener { view ->
            if ((view as CompoundButton).isChecked) {
                equilibrio = true
                println("Checked")
            } else {
                println("Un-Checked")
                equilibrio = false
            }
            lineData = LineData()
            lineChart.clear()
            lineChart.invalidate()
            graficar()
        })

        motricidadCB.setOnClickListener(View.OnClickListener { view ->
            if ((view as CompoundButton).isChecked) {
                motricidad = true
            } else {
                println("Un-Checked")
                motricidad = false
            }
            lineData = LineData()
            lineChart.clear()
            lineChart.invalidate()
            graficar()
        })

        discriminacionCB.setOnClickListener(View.OnClickListener { view ->
            if ((view as CompoundButton).isChecked) {
                discriminacion = true
            } else {
                println("Un-Checked")
                discriminacion = false
            }
            lineData = LineData()
            lineChart.clear()
            lineChart.invalidate()
            graficar()
        })

        visomotoraCB.setOnClickListener(View.OnClickListener { view ->
            if ((view as CompoundButton).isChecked) {
                visomotora = true
            } else {
                println("Un-Checked")
                visomotora = false
            }
            lineData = LineData()
            lineChart.clear()
            lineChart.invalidate()
            graficar()
        })
    }

    // Función para configurar que mostrar por defecto si no hay datos
    private fun sinDatos(lineChartParametros: LineChart) {
        lineChartParametros.setNoDataText("No hay datos disponibles")
        lineChartParametros.setNoDataTextColor(Color.parseColor("#484848"))
        lineChartParametros.setBackgroundColor(Color.parseColor("#E7E9EC"))

        lineChart.setNoDataText("No se ha seleccionado ningún parámetro")
        lineChart.setNoDataTextColor(Color.parseColor("#484848"))
        lineChart.setBackgroundColor(Color.parseColor("#E7E9EC"))


        radarChart.setNoDataText("No hay datos disponibles")
        radarChart.setNoDataTextColor(Color.parseColor("#484848"))
    }

    //-------------------------------------- GRÁFICO DE LÍNEA ------------------------------------------//
    private fun graficar() {
        //Limpiar datos del arreglo Radar
        valorDatosRadar.clear()

        //Búsqueda de pares ordenados (x,y) en la bd
        //Buscar los datos del parámetro memoria
        dbReference.child("Memoria Panel").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var j = 0f
                    var sumMemoria = 0f
                    valorDatosLineaM.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {
                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            val real = ds.child("real").value.toString().toBoolean()
                            i += 1
                            valorDatosLineaM.add(Entry(datoX, datoY))
                            if (real) {
                                sumMemoria += datoY
                                j += 1
                            }
                        }
                    }
                    promedioMemoria = sumMemoria / j
                    valorDatosRadar.add(RadarEntry(promedioMemoria))
                    graficoRadar()

                    //Asignar el set de datos al gráfico de línea de memoria
                    lineDataSetM = LineDataSet(valorDatosLineaM, "Memoria")

                    //configuracion para deshabilitar descripción
                    lineChartMemoria.description.isEnabled = false
                    xAxisM = lineChartMemoria.xAxis
                    legendM = lineChartMemoria.legend

                    lineaConfigGral(lineChartMemoria)
                    lineaEstiloConfig(lineDataSetM, "#204078")
                    lineaReferenciaConfig(legendM, 1)
                    lineaEjesConfig("Memoria Panel", fechasArrayM, lineChartMemoria, xAxisM)

                    lineDataM = LineData(lineDataSetM)
                    lineChartMemoria.data = lineDataM

                    //Asignar el set de datos al gráfico de evolución integral
                    if (memoria) {
                        lineData.addDataSet(lineDataSetM)
                        lineaIntegral()
                    }
                }

            }
        })

        //Buscar los datos del parámetro equilibrio postural
        dbReference.child("Equilibrio Postural Panel").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var j = 0f
                    var sumEquilibrio = 0f
                    valorDatosLineaE.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            val real = ds.child("real").value.toString().toBoolean()
                            valorDatosLineaE.add(Entry(datoX, datoY))
                            if (real) {
                                sumEquilibrio += datoY
                                j += 1
                            }
                        }
                    }
                    promedioEquilibro = sumEquilibrio / j
                    valorDatosRadar.add(RadarEntry(promedioEquilibro))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetE = LineDataSet(valorDatosLineaE, "Equilibrio Postural")

                    //configuracion para descripción, legenda y eje x
                    lineChartEquilibrio.description.isEnabled = false
                    legendE = lineChartEquilibrio.legend
                    xAxisE = lineChartEquilibrio.xAxis

                    lineaConfigGral(lineChartEquilibrio)
                    lineaEstiloConfig(lineDataSetE, "#0080C8")
                    lineaReferenciaConfig(legendE, 1)
                    lineaEjesConfig(
                        "Equilibrio Postural Panel",
                        fechasArrayE,
                        lineChartEquilibrio,
                        xAxisE
                    )

                    lineDataE = LineData(lineDataSetE)
                    lineChartEquilibrio.data = lineDataE

                    if (equilibrio) {
                        lineData.addDataSet(lineDataSetE)
                        lineaIntegral()
                    }
                }

            }
        })

        // Buscar los datos del parámetro capacidad de atención
        dbReference.child("Capacidad de Atención Panel").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var sumAtencion = 0f
                    valorDatosLineaA.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaA.add(Entry(datoX, datoY))
                            sumAtencion += datoY
                        }

                    }
                    promedioAtencion = sumAtencion / i
                    valorDatosRadar.add(RadarEntry(promedioAtencion))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetA = LineDataSet(valorDatosLineaA, "Capacidad de Atención")

                    //configuracion para deshabilitar descripción
                    lineChartAtencion.description.isEnabled = false
                    legendA = lineChartAtencion.legend
                    xAxisA = lineChartAtencion.xAxis

                    lineaConfigGral(lineChartAtencion)
                    lineaEstiloConfig(lineDataSetA, "#02D6E4")
                    lineaReferenciaConfig(legendA, 1)
                    lineaEjesConfig(
                        "Capacidad de Atención Panel",
                        fechasArrayA,
                        lineChartAtencion,
                        xAxisA
                    )

                    lineDataA = LineData(lineDataSetA)
                    lineChartAtencion.data = lineDataA

                    if (atencion) {
                        lineData.addDataSet(lineDataSetA)
                        lineaIntegral()
                    }
                }
            }
        })

        // Buscar los datos del parámetro motricidad fina
        dbReference.child("Motricidad Fina Panel").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var j = 0f
                    var sumMFina = 0f
                    valorDatosLineaMF.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            val real = ds.child("real").value.toString().toBoolean()
                            valorDatosLineaMF.add(Entry(datoX, datoY))
                            if (real) {
                                sumMFina += datoY
                                j += 1
                            }
                        }

                    }
                    promedioMFina = sumMFina / j
                    valorDatosRadar.add(RadarEntry(promedioMFina))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetMF = LineDataSet(valorDatosLineaMF, "Motricidad Fina")

                    //configuracion para deshabilitar descripción
                    lineChartMFina.description.isEnabled = false
                    legendMF = lineChartMFina.legend
                    xAxisMF = lineChartMFina.xAxis

                    lineaConfigGral(lineChartMFina)
                    lineaEstiloConfig(lineDataSetMF, "#98E088")
                    lineaReferenciaConfig(legendMF, 1)
                    lineaEjesConfig("Motricidad Fina Panel", fechasArrayMF, lineChartMFina, xAxisMF)

                    lineDataMF = LineData(lineDataSetMF)
                    lineChartMFina.data = lineDataMF

                    if (motricidad) {
                        lineData.addDataSet(lineDataSetMF)
                        lineaIntegral()
                    }
                }
            }
        })

        // Buscar los datos del parámetro discriminación de colores
        dbReference.child("Discriminación de Colores Panel").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var sumColores = 0f
                    valorDatosLineaColores.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaColores.add(Entry(datoX, datoY))
                            sumColores += datoY
                        }

                    }
                    promedioColores = sumColores / i
                    valorDatosRadar.add(RadarEntry(promedioColores))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetColores = LineDataSet(
                        valorDatosLineaColores,
                        "Discriminación de Colores"
                    )

                    //configuracion para deshabilitar descripción
                    lineChartColores.description.isEnabled = false
                    legendColores = lineChartColores.legend
                    xAxisColores = lineChartColores.xAxis

                    pos = 5
                    lineaEjesConfig(
                        "Discriminación de Colores Panel",
                        fechasArrayColores,
                        lineChartColores,
                        xAxisColores
                    )
                    lineaEstiloConfig(lineDataSetColores, "#F090E0")
                    lineaReferenciaConfig(legendColores, 1)
                    lineaConfigGral(lineChartColores)

                    lineDataColores = LineData(lineDataSetColores)
                    lineChartColores.data = lineDataColores

                    if (discriminacion) {
                        lineData.addDataSet(lineDataSetColores)
                        lineaIntegral()
                    }
                }
            }
        })

        // Buscar los datos del parámetro integración viso-motora
        dbReference.child("Visomotora Panel").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var sumVisomotora = 0f
                    valorDatosLineaV.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaV.add(Entry(datoX, datoY))
                            sumVisomotora += datoY
                        }

                    }
                    promedioVisomotora = sumVisomotora / i
                    valorDatosRadar.add(RadarEntry(promedioVisomotora))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetV = LineDataSet(valorDatosLineaV, "Integración Viso-Motora")

                    //configuracion para deshabilitar descripción
                    lineChartVisomotora.description.isEnabled = false
                    legendV = lineChartVisomotora.legend
                    xAxisV = lineChartVisomotora.xAxis

                    lineaConfigGral(lineChartVisomotora)
                    lineaEstiloConfig(lineDataSetV, "#B82870")
                    lineaReferenciaConfig(legendV, 1)
                    lineaEjesConfig("Visomotora Panel", fechasArrayV, lineChartVisomotora, xAxisV)

                    lineDataV = LineData(lineDataSetV)
                    lineChartVisomotora.data = lineDataV

                    if (visomotora) {
                        lineData.addDataSet(lineDataSetV)
                        lineaIntegral()
                    }
                }
            }
        })
    }

    //Funciones gráfico de línea
    private fun lineaIntegral(){
        lineChart.clear()
        lineChart.invalidate()

        //Configuración general
        lineaConfigGral(lineChart)

        //Configuracion descripción
        lineChart.description.isEnabled = false

        //Configuración leyenda
        legend = lineChart.legend
        lineaReferenciaConfig(legend, 0)

        //Configuración ejes
        xAxis = lineChart.xAxis
        lineaEjesConfig("Memoria Panel", fechasArrayM, lineChart, xAxis)

        lineChart.data = lineData

    }


    private fun lineaConfigGral(lineChart: LineChart) {
        //Configuracion estilo general del gráfico

        lineChart.setDrawGridBackground(true)
        lineChart.setDrawBorders(false)
        lineChart.setBackgroundColor(Color.parseColor("#E7E9EC"))
        lineChart.xAxis.axisMinimum = 0f
        lineChart.axisLeft.axisMinimum = 0f
        lineChart.isDragEnabled = true
        lineChart.invalidate()
    }

    private fun lineaEstiloConfig(lineDataSet: LineDataSet, color: String) {
        //configuración estilo de la línea
        lineDataSet.lineWidth = 3f
        lineDataSet.color = Color.parseColor(color)
        lineDataSet.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
        lineDataSet.setDrawFilled(false) //pinta el área debajo de la curva de color
        lineDataSet.setDrawCircles(true) //puntos de pares ordenados
        lineDataSet.setDrawCircleHole(false) //puntos circulos
        lineDataSet.setCircleColor(Color.parseColor(color)) //color del circulo
        lineDataSet.circleRadius = 3f
    }

    private fun lineaReferenciaConfig(legend: Legend, offset: Int){
        //configuracion para habilitar referencias
        legend.isEnabled = true
        legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM

            if(offset == 1){
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }else{
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.VERTICAL
            }

        }
        legend.textColor = Color.BLACK
        legend.textSize = 15f
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 15f
        legend.isWordWrapEnabled = true
        if(offset == 0){
            legend.xOffset = -30f
            legend.yOffset = 35f
        }
        legend.formToTextSpace = 6f
    }

    private fun lineaEjesConfig(
        parametro: String,
        fechasArray: MutableList<String>,
        lineChart: LineChart,
        xAxis: XAxis
    ) {
        //Configuración general de los ejes de los gráficos de línea
        //Arreglo para mostrar los dias como labels
        dbReference.child(parametro).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    fechasArray.clear()
                    for (ds in snapshot.children) {
                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val labelX = ds.child("x").value.toString()
                            fechasArray.add(labelX)
                        }
                    }
                    xAxis.valueFormatter = IndexAxisValueFormatter(fechasArray)
                    lineChart.invalidate()
                }
            }
        })

        //eje x
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMinimum = 0f
        xAxis.labelRotationAngle = 0f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(true) //config lineas verticales
        xAxis.textSize = 12f

        //eje y
        lineChart.axisRight.isEnabled = false //deshabilita el eje y a la derecha
        yAxis = lineChart.axisLeft
        yAxis.granularity = 1f
        yAxis.setDrawZeroLine(true)
        yAxis.setDrawAxisLine(false) // no axis line
        yAxis.setDrawGridLines(true) // no grid lines horizontales
        yAxis.setDrawZeroLine(true)
        yAxis.zeroLineColor = Color.parseColor("#D0D8E0")
        yAxis.zeroLineWidth = 2f
        yAxis.axisMaximum = 4.5f

    }

    //---------------------------------------- GRÁFICO RADAR ------------------------------------------------//
    private fun graficoRadar() {
        buttonDescargarR.setOnClickListener {
            if(radarChart.saveToGallery(System.currentTimeMillis().toString(), 85)){
                Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
            }
        }
        //Asignar el set de datos al gráfico
        radarDataSet = RadarDataSet(valorDatosRadar, "")
        radarDataSet.color = Color.parseColor("#B870A8")
        radarDataSet.setDrawFilled(true)
        radarDataSet.fillAlpha = Color.GRAY
        radarDataSet.fillColor = Color.parseColor("#B870A8")
        radarData = RadarData(radarDataSet)
        radarChart.data = radarData

        //configuración para deshabilitar descripción
        radarChart.description.isEnabled = false

        //configuración para deshabilitar referencias
        val legend: Legend = radarChart.legend
        legend.isEnabled = false

        //configuración estilo general del gráfico
        radarChart.setBackgroundColor(Color.parseColor("#D0D8E0"))

        //configuración de los ejes
        yAxisr = radarChart.yAxis
        yAxisr.isEnabled = false
        yAxisr.axisMinimum = 0f
        yAxisr.axisMaximum = 5.25f
        xAxisr = radarChart.xAxis
        radarDataSet.valueTextSize = 11f
        xAxisr.valueFormatter = IndexAxisValueFormatter(arrayParametros)
        xAxisr.textSize = 14f
        radarChart.scaleY = 1.1f
        radarChart.scaleX = 1.1f

        radarChart.textAlignment = View.TEXT_ALIGNMENT_CENTER
        radarChart.setTouchEnabled(false)
        radarChart.invalidate()
    }

}