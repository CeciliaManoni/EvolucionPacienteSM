package com.example.salamultisensorial

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.*
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_panel_interactivo.*
import kotlinx.android.synthetic.main.fragment_graficos.*


class GraficosFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference

    //-------------------------------Variables gráficos de línea-------------------------------//
    //Parámetro memoria
    private lateinit var lineChartMemoria: LineChart
    private lateinit var lineDataSetM: LineDataSet
    private lateinit var lineDataM: LineData
    private lateinit var xAxisM: XAxis
    private lateinit var yAxisM: YAxis
    private lateinit var legendM: Legend
    private lateinit var buttonDescargarM : Button
    val dataSetM:ArrayList<ILineDataSet> = ArrayList()
    private val valorDatosLineaM: MutableList<Entry> = ArrayList()
    private var fechasArrayM: MutableList<String> = ArrayList()
    //Parámetro equilibrio postural
    private lateinit var lineChartEquilibrio: LineChart
    private lateinit var lineDataSetE: LineDataSet
    private lateinit var lineDataE: LineData
    private lateinit var xAxisE: XAxis
    private lateinit var yAxisE: YAxis
    private lateinit var legendE: Legend
    private lateinit var buttonDescargarE : Button
    val dataSetE:ArrayList<ILineDataSet> = ArrayList()
    private val valorDatosLineaE: MutableList<Entry> = ArrayList()
    private var fechasArrayE: MutableList<String> = ArrayList()
    //Parámetro capacidad de atención
    private lateinit var lineChartAtencion: LineChart
    private lateinit var lineDataSetA: LineDataSet
    private lateinit var lineDataA: LineData
    private lateinit var xAxisA: XAxis
    private lateinit var yAxisA: YAxis
    private lateinit var legendA: Legend
    private lateinit var buttonDescargarA : Button
    val dataSetA:ArrayList<ILineDataSet> = ArrayList()
    private val valorDatosLineaA: MutableList<Entry> = ArrayList()
    private var fechasArrayA: MutableList<String> = ArrayList()
    //Parámetro motricidad fina
    private lateinit var lineChartMFina: LineChart
    private lateinit var lineDataSetMF: LineDataSet
    private lateinit var lineDataMF: LineData
    private lateinit var xAxisMF: XAxis
    private lateinit var yAxisMF: YAxis
    private lateinit var legendMF: Legend
    private lateinit var buttonDescargarMF: Button
    val dataSetMF:ArrayList<ILineDataSet> = ArrayList()
    private val valorDatosLineaMF: MutableList<Entry> = ArrayList()
    private var fechasArrayMF: MutableList<String> = ArrayList()
    //Parámetro discriminación de colores
    private lateinit var lineChartColores: LineChart
    private lateinit var lineDataSetColores: LineDataSet
    private lateinit var lineDataColores: LineData
    private lateinit var xAxisColores: XAxis
    private lateinit var yAxisColores: YAxis
    private lateinit var legendColores: Legend
    private lateinit var buttonDescargarColores : Button
    val dataSetColores:ArrayList<ILineDataSet> = ArrayList()
    private val valorDatosLineaColores: MutableList<Entry> = ArrayList()
    private var fechasArrayColores: MutableList<String> = ArrayList()
    //Parámetro integración visomotora
    private lateinit var lineChartVisomotora: LineChart
    private lateinit var lineDataSetV: LineDataSet
    private lateinit var lineDataV: LineData
    private lateinit var xAxisV: XAxis
    private lateinit var yAxisV: YAxis
    private lateinit var legendV: Legend
    private lateinit var buttonDescargarV : Button
    val dataSetV:ArrayList<ILineDataSet> = ArrayList()
    private val valorDatosLineaV: MutableList<Entry> = ArrayList()
    private var fechasArrayV: MutableList<String> = ArrayList()


    //-----------------------------------Evolución integral-------------------------------------//
    // Variables gráfico de línea
    private lateinit var lineChart: LineChart
    private lateinit var lineData: LineData
    private lateinit var xAxis: XAxis
    private lateinit var yAxis: YAxis
    private lateinit var legend : Legend
    private lateinit var buttonDescargarL : Button
    private lateinit var botonGraficarLinea : Button
    private var memoria = false
    private var atencion = false
    private var equilibrio = false
    private var motricidad = false
    private var discriminacion = false
    private var visomotora = false
    var dataSets: ArrayList<ILineDataSet> = ArrayList()
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
    private val arrayParametros = arrayListOf(" M"," E "," A "," MF "," DC "," IVM ")

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
        val view = inflater.inflate(R.layout.fragment_graficos, container, false)

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
        botonGraficarLinea = view.findViewById(R.id.botonGraficarLinea)
        memoriaCB = view.findViewById(R.id.memoriaCB)
        atencionCB = view.findViewById(R.id.atencionCB)
        equilibrioCB = view.findViewById(R.id.equilibrioCB)
        discriminacionCB = view.findViewById(R.id.discriminacionCB)
        visomotoraCB = view.findViewById(R.id.visomotoraCB)
        motricidadCB = view.findViewById(R.id.motricidadFinaCB)

        //Recibir los datos enviados desde el bundle de MainActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaInicio = arguments?.getString("fechaInicio")
        fechaFin = arguments?.getString("fechaFin")

        //Ubicar la referencia de la bd en la carpeta del paciente seleccionado
        dbReference = FirebaseDatabase.getInstance().getReference("Pacientes").
        child(pacienteDni.toString())

        sinDatos()
        graficoLinea()

        descagar(buttonDescargarM, lineChartMemoria)
        descagar(buttonDescargarE, lineChartEquilibrio)
        descagar(buttonDescargarA, lineChartAtencion)
        descagar(buttonDescargarMF, lineChartMFina)
        descagar(buttonDescargarColores, lineChartColores)
        descagar(buttonDescargarV, lineChartVisomotora)
        descagar(buttonDescargarL, lineChart)

        botonGraficarLinea.setOnClickListener {
            graficarLinea()
        }

        return view
    }

    // Función para descargar los gráficos
    private fun descagar(buttonDescargar: Button, lineChart:LineChart) {

        buttonDescargar.setOnClickListener {
            if(lineChart.saveToGallery(System.currentTimeMillis().toString(),85)){
                Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    //Funcino para graficar linea integral
    fun graficarLinea() {

        if(memoriaCB.isChecked){
            memoria = true
            println("memoria")
        }
        if(atencionCB.isChecked){
            memoria = true
            println("atencion")
        }
        if(equilibrioCB.isChecked){
            memoria = true
            println("equilirbio")
        }
        if(discriminacionCB.isChecked){
            memoria = true
            println("discriminacion")
        }
        if(visomotoraCB.isChecked){
            memoria = true
            println("visomotora")
        }
        if(motricidadCB.isChecked){
            memoria = true
            println("motricidad")
        }
    }

    // Función para configurar que mostrar por defecto si no hay datos
    private fun sinDatos() {
        lineChartMemoria.setNoDataText("No hay datos disponibles")
        lineChartMemoria.setNoDataTextColor(Color.parseColor("#484848"))
        lineChartEquilibrio.setNoDataText("No hay datos disponibles")
        lineChartEquilibrio.setNoDataTextColor(Color.parseColor("#484848"))
        lineChartAtencion.setNoDataText("No hay datos disponibles")
        lineChartAtencion.setNoDataTextColor(Color.parseColor("#484848"))
        lineChartMFina.setNoDataText("No hay datos disponibles")
        lineChartMFina.setNoDataTextColor(Color.parseColor("#484848"))
        lineChart.setNoDataText("No hay datos disponibles")
        lineChart.setNoDataTextColor(Color.parseColor("#484848"))
        radarChart.setNoDataText("No hay datos disponibles")
        radarChart.setNoDataTextColor(Color.parseColor("#484848"))
    }


    //-------------------------------------- GRÁFICO DE LÍNEA ------------------------------------------//
    private fun graficoLinea() {
        //Limpiar datos del arreglo Radar
        valorDatosRadar.clear()

        //Búsqueda de pares ordenados (x,y) en la bd
        //Buscar los datos del parámetro memoria
        dbReference.child("Memoria").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var sumMemoria = 0f
                    valorDatosLineaM.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {
                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaM.add(Entry(datoX, datoY))
                            sumMemoria += datoY
                        }
                    }
                    promedioMemoria = sumMemoria / i
                    valorDatosRadar.add(RadarEntry(promedioMemoria))
                    graficoRadar()

                    //Asignar el set de datos al gráfico de línea de memoria
                    lineDataSetM = LineDataSet(valorDatosLineaM, "Memoria")
                    dataSetM.add(lineDataSetM)

                    //configuracion para deshabilitar descripción
                    lineChartMemoria.description.isEnabled = false
                    //configuracion para deshabilitar referencias
                    val legend: Legend = lineChartMemoria.legend
                    legend.isEnabled = false

                    pos = 1
                    lineaConfigGral()
                    lineaEstiloConfig()
                    lineaReferenciaConfig()
                    lineaEjesConfig()

                    lineDataM = LineData(dataSetM)
                    lineChartMemoria.data = lineDataM

                    //Asignar el set de datos al gráfico de evolución integral

                    dataSets.add(lineDataSetM)
                    lineaIntegral()



                }

            }
        })

        //Buscar los datos del parámetro equilibrio postural
        dbReference.child("Equilibrio Postural").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var sumEquilibrio = 0f
                    valorDatosLineaE.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaE.add(Entry(datoX, datoY))
                            sumEquilibrio += datoY
                        }
                    }
                    promedioEquilibro = sumEquilibrio / i
                    valorDatosRadar.add(RadarEntry(promedioEquilibro))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetE = LineDataSet(valorDatosLineaE, "Equilibrio Postural")
                    dataSetE.add(lineDataSetE)

                    //configuracion para deshabilitar descripción
                    lineChartEquilibrio.description.isEnabled = false

                    pos = 2
                    lineaConfigGral()
                    lineaEstiloConfig()
                    lineaReferenciaConfig()
                    lineaEjesConfig()

                    lineDataE = LineData(dataSetE)
                    lineChartEquilibrio.data = lineDataE


                    dataSets.add(lineDataSetE)
                    lineaIntegral()

                }

            }
        })

        // Buscar los datos del parámetro capacidad de atención
        dbReference.child("Capacidad de Atención").addValueEventListener(object :
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
                    dataSetA.add(lineDataSetA)

                    //configuracion para deshabilitar descripción
                    lineChartAtencion.description.isEnabled = false
                    //configuracion para deshabilitar referencias
                    val legend: Legend = lineChartAtencion.legend
                    legend.isEnabled = false

                    pos = 3
                    lineaConfigGral()
                    lineaEstiloConfig()
                    lineaReferenciaConfig()
                    lineaEjesConfig()

                    lineDataA = LineData(dataSetA)
                    lineChartAtencion.data = lineDataA


                    dataSets.add(lineDataSetA)
                    lineaIntegral()
                }
            }
        })

        // Buscar los datos del parámetro motricidad fina
        dbReference.child("Motricidad Fina").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var i = 0f
                    var sumMFina = 0f
                    valorDatosLineaMF.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaMF.add(Entry(datoX, datoY))
                            sumMFina += datoY
                        }

                    }
                    promedioMFina = sumMFina / i
                    valorDatosRadar.add(RadarEntry(promedioMFina))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetMF = LineDataSet(valorDatosLineaMF, "Motricidad Fina")
                    dataSetMF.add(lineDataSetMF)

                    //configuracion para deshabilitar descripción
                    lineChartMFina.description.isEnabled = false

                    pos = 4
                    lineaConfigGral()
                    lineaEstiloConfig()
                    lineaReferenciaConfig()
                    lineaEjesConfig()

                    lineDataMF = LineData(dataSetMF)
                    lineChartMFina.data = lineDataMF

                    dataSets.add(lineDataSetMF)
                    lineaIntegral()

                }
            }
        })

        // Buscar los datos del parámetro discriminación de colores
        dbReference.child("Discriminación de Colores").addValueEventListener(object :
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
                    lineDataSetColores = LineDataSet(valorDatosLineaColores, "Discriminación de Colores")
                    dataSetColores.add(lineDataSetColores)


                    //configuracion para deshabilitar descripción
                    lineChartColores.description.isEnabled = false

                    pos = 5
                    lineaConfigGral()
                    lineaEstiloConfig()
                    lineaReferenciaConfig()
                    lineaEjesConfig()

                    lineDataColores = LineData(dataSetColores)
                    lineChartColores.data = lineDataColores

                    dataSets.add(lineDataSetColores)
                    lineaIntegral()
                }
            }
        })

        // Buscar los datos del parámetro integración viso-motora
        dbReference.child("Visomotora").addValueEventListener(object :
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
                    dataSetV.add(lineDataSetV)


                    //configuracion para deshabilitar descripción
                    lineChartVisomotora.description.isEnabled = false

                    pos = 6
                    lineaConfigGral()
                    lineaEstiloConfig()
                    lineaReferenciaConfig()
                    lineaEjesConfig()

                    lineDataV = LineData(lineDataSetV)
                    lineChartVisomotora.data = lineDataV
                    
                    dataSets.add(lineDataSetV)
                    lineaIntegral()
                }
            }
        })
    }

    //Funciones gráfico de línea
    private fun lineaIntegral(){
        //Configuración general
        lineChart.setDrawGridBackground(true)
        lineChart.setDrawBorders(false)
        lineChart.setBackgroundColor(Color.parseColor("#E7E9EC"))
        lineChart.xAxis.axisMinimum = 0f
        lineChart.axisLeft.axisMinimum = 0f
        lineChart.isDragEnabled = true
        lineChart.invalidate()

        //Configuracion descripción
        lineChart.description.isEnabled = false

        //Configuración leyenda
        legend = lineChart.legend
        legend.isEnabled = true
        legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.VERTICAL
        }
        legend.textColor = Color.BLACK
        legend.textSize = 15f
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 15f
        legend.isWordWrapEnabled = true
        legend.xOffset =-30f
        legend.yOffset = 35f
        legend.xEntrySpace = 20f
        legend.formToTextSpace = 6f


        //Configuración ejes
        //eje x
        xAxis = lineChart.xAxis
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

        lineData = LineData(dataSets)
        lineChart.data = lineData

    }

    private fun lineaConfigGral() {
        //Configuracion estilo general del gráfico
        //Memoria
        if (pos == 1) {
            lineChartMemoria.setDrawGridBackground(true)
            lineChartMemoria.setDrawBorders(false)
            lineChartMemoria.setBackgroundColor(Color.parseColor("#E7E9EC"))
            lineChartMemoria.xAxis.axisMinimum = 0f
            lineChartMemoria.axisLeft.axisMinimum = 0f
            lineChartMemoria.isDragEnabled = true
            lineChartMemoria.invalidate()
        }

        //Equilibrio Postural
        if (pos == 2) {
            lineChartEquilibrio.setDrawGridBackground(true)
            lineChartEquilibrio.setDrawBorders(false)
            lineChartEquilibrio.setBackgroundColor(Color.parseColor("#E7E9EC"))
            lineChartEquilibrio.xAxis.axisMinimum = 0f
            lineChartEquilibrio.axisLeft.axisMinimum = 0f
            lineChartEquilibrio.isDragEnabled = true
            lineChartEquilibrio.invalidate()
        }

        //Capacidad de Atención
        if (pos == 3) {
            lineChartAtencion.setDrawGridBackground(true)
            lineChartAtencion.setDrawBorders(false)
            lineChartAtencion.setBackgroundColor(Color.parseColor("#E7E9EC"))
            lineChartAtencion.xAxis.axisMinimum = 0f
            lineChartAtencion.axisLeft.axisMinimum = 0f
            lineChartAtencion.isDragEnabled = true
            lineChartAtencion.invalidate()
        }

        //Motricidad Fina
        if (pos == 4) {
            lineChartMFina.setDrawGridBackground(true)
            lineChartMFina.setDrawBorders(false)
            lineChartMFina.setBackgroundColor(Color.parseColor("#E7E9EC"))
            lineChartMFina.xAxis.axisMinimum = 0f
            lineChartMFina.axisLeft.axisMinimum = 0f
            lineChartMFina.isDragEnabled = true
            lineChartMFina.invalidate()
        }

        //Discriminación de Colores
        if (pos == 5) {
            lineChartColores.setDrawGridBackground(true)
            lineChartColores.setDrawBorders(false)
            lineChartColores.setBackgroundColor(Color.parseColor("#E7E9EC"))
            lineChartColores.xAxis.axisMinimum = 0f
            lineChartColores.axisLeft.axisMinimum = 0f
            lineChartColores.isDragEnabled = true
            lineChartColores.invalidate()
        }

        //Integración Viso-Motora
        if (pos == 6) {
            lineChartVisomotora.setDrawGridBackground(true)
            lineChartVisomotora.setDrawBorders(false)
            lineChartVisomotora.setBackgroundColor(Color.parseColor("#E7E9EC"))
            lineChartVisomotora.xAxis.axisMinimum = 0f
            lineChartVisomotora.axisLeft.axisMinimum = 0f
            lineChartVisomotora.isDragEnabled = true
            lineChartVisomotora.invalidate()
        }
    }
    private fun lineaEstiloConfig() {
        //configuración estilo de la línea
        //Memoria
        if (pos == 1) {
            lineDataSetM.lineWidth = 3f
            lineDataSetM.color = Color.parseColor("#204078")
            lineDataSetM.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetM.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetM.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetM.setDrawCircleHole(false) //puntos circulos
            lineDataSetM.setCircleColor(Color.parseColor("#204078")) //color del circulo
            lineDataSetM.circleRadius = 3f
        }

        //Equilibrio Postural
        if (pos == 2) {
            lineDataSetE.lineWidth = 3f
            lineDataSetE.color = Color.parseColor("#0080C8") //DF9B4B
            lineDataSetE.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetE.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetE.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetE.setDrawCircleHole(false) //puntos circulos
            lineDataSetE.setCircleColor(Color.parseColor("#0080C8")) //color del circulo
            lineDataSetE.circleRadius = 3f
        }

        //Capacidad de Atención
        if (pos == 3) {
            lineDataSetA.lineWidth = 3f
            lineDataSetA.color = Color.parseColor("#02D6E4") //DF9B4B
            lineDataSetA.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetA.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetA.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetA.setDrawCircleHole(false) //puntos circulos
            lineDataSetA.setCircleColor(Color.parseColor("#02D6E4")) //color del circulo
            lineDataSetA.circleRadius = 3f
        }

        //Motricidad Fina
        if (pos == 4) {
            lineDataSetMF.lineWidth = 3f
            lineDataSetMF.color = Color.parseColor("#98E088") //DF9B4B
            lineDataSetMF.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetMF.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetMF.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetMF.setDrawCircleHole(false) //puntos circulos
            lineDataSetMF.setCircleColor(Color.parseColor("#98E088")) //color del circulo
            lineDataSetMF.circleRadius = 3f
        }

        //Discriminación de Colores
        if (pos == 5) {
            lineDataSetColores.lineWidth = 3f
            lineDataSetColores.color = Color.parseColor("#F090E0") //DF9B4B
            lineDataSetColores.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetColores.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetColores.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetColores.setDrawCircleHole(false) //puntos circulos
            lineDataSetColores.setCircleColor(Color.parseColor("#F090E0")) //color del circulo
            lineDataSetColores.circleRadius = 3f
        }

        //Integración Viso-Motora
        if (pos == 6) {
            lineDataSetV.lineWidth = 3f
            lineDataSetV.color = Color.parseColor("#B82870") //DF9B4B
            lineDataSetV.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetV.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetV.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetV.setDrawCircleHole(false) //puntos circulos
            lineDataSetV.setCircleColor(Color.parseColor("#B82870")) //color del circulo
            lineDataSetV.circleRadius = 3f
        }
    }

    private fun lineaReferenciaConfig(){
        //configuracion para habilitar referencias
        //Memoria
        if (pos == 1) {
            legendM = lineChartMemoria.legend
            legendM.isEnabled = true
            legendM.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
            legendM.textColor = Color.BLACK
            legendM.textSize = 15f
            legendM.form = Legend.LegendForm.CIRCLE
            legendM.formSize = 15f
            legendM.xEntrySpace = 85f
            legendM.formToTextSpace = 6f
        }

        //Equilibrio Postural
        if (pos == 2) {
            legendE = lineChartEquilibrio.legend
            legendE.isEnabled = true
            legendE.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
            legendE.textColor = Color.BLACK
            legendE.textSize = 15f
            legendE.form = Legend.LegendForm.CIRCLE
            legendE.formSize = 15f
            legendE.xEntrySpace = 85f
            legendE.formToTextSpace = 6f
        }

        //Capacidad de Atención
        if (pos == 3) {
            legendA = lineChartAtencion.legend
            legendA.isEnabled = true
            legendA.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
            legendA.textColor = Color.BLACK
            legendA.textSize = 15f
            legendA.form = Legend.LegendForm.CIRCLE
            legendA.formSize = 15f
            legendA.xEntrySpace = 85f
            legendA.formToTextSpace = 6f
        }

        //Motricidad Fina
        if (pos == 4) {
            legendMF = lineChartMFina.legend
            legendMF.isEnabled = true
            legendMF.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
            legendMF.textColor = Color.BLACK
            legendMF.textSize = 15f
            legendMF.form = Legend.LegendForm.CIRCLE
            legendMF.formSize = 15f
            legendMF.xEntrySpace = 85f
            legendMF.formToTextSpace = 6F
        }

        //Discriminación de colores
        if (pos == 5) {
            legendColores = lineChartColores.legend
            legendColores.isEnabled = true
            legendColores.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
            legendColores.textColor = Color.BLACK
            legendColores.textSize = 15f
            legendColores.form = Legend.LegendForm.CIRCLE
            legendColores.formSize = 15f
            legendColores.xEntrySpace = 85f
            legendColores.formToTextSpace = 6f
        }

        //Integración Viso-motora
        if (pos == 6) {
            legendV = lineChartVisomotora.legend
            legendV.isEnabled = true
            legendV.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
            legendV.textColor = Color.BLACK
            legendV.textSize = 15f
            legendV.form = Legend.LegendForm.CIRCLE
            legendV.formSize = 15f
            legendV.xEntrySpace = 85f
            legendV.formToTextSpace = 6f
        }
    }
    private fun lineaEjesConfig() {
        //Configuración general de los ejes de los gráficos de línea
        //Memoria
        if (pos == 1) {
            //Arreglo para mostrar los dias como labels
            dbReference.child("Memoria").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        fechasArrayM.clear()
                        for (ds in snapshot.children) {
                            if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                                val labelX = ds.child("x").value.toString()
                                fechasArrayM.add(labelX)
                            }
                        }
                        xAxisM.valueFormatter = IndexAxisValueFormatter(fechasArrayM)
                        xAxis.valueFormatter = IndexAxisValueFormatter(fechasArrayM)
                        lineChartMemoria.invalidate()
                    }
                }
            })

            //eje x
            xAxisM = lineChartMemoria.xAxis
            xAxisM.granularity = 1f
            xAxisM.position = XAxis.XAxisPosition.BOTTOM
            xAxisM.axisMinimum = 0f
            xAxisM.labelRotationAngle = 0f
            xAxisM.position = XAxis.XAxisPosition.BOTTOM
            xAxisM.setDrawGridLines(true) //config lineas verticales
            xAxisM.textSize = 12f

            //eje y
            lineChartMemoria.axisRight.isEnabled = false //deshabilita el eje y a la derecha
            yAxisM = lineChartMemoria.axisLeft
            yAxisM.granularity = 1f
            yAxisM.setDrawZeroLine(true)
            yAxisM.setDrawAxisLine(false) // no axis line
            yAxisM.setDrawGridLines(true) // no grid lines horizontales
            yAxisM.setDrawZeroLine(true)
            yAxisM.zeroLineColor = Color.parseColor("#D0D8E0")
            yAxisM.zeroLineWidth = 2f
            yAxisM.axisMaximum = 4.5f
        }

        //Equilibrio Postural
        if (pos == 2) {
            //Arreglo para mostrar los dias como labels
            dbReference.child("Equilibrio Postural").addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        fechasArrayE.clear()
                        for (ds in snapshot.children) {
                            if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                                val labelX = ds.child("x").value.toString()
                                fechasArrayE.add(labelX)
                            }
                        }
                        xAxisE.valueFormatter = IndexAxisValueFormatter(fechasArrayE)
                        lineChartEquilibrio.invalidate()
                    }
                }
            })

            //eje x
            xAxisE = lineChartEquilibrio.xAxis
            xAxisE.granularity = 1f
            xAxisE.position = XAxis.XAxisPosition.BOTTOM
            xAxisE.axisMinimum = 0f
            xAxisE.labelRotationAngle = 0f
            xAxisE.position = XAxis.XAxisPosition.BOTTOM
            xAxisE.setDrawGridLines(true) //config lineas verticales
            xAxisE.textSize = 12f

            //eje y
            lineChartEquilibrio.axisRight.isEnabled = false //deshabilita el eje y a la derecha
            yAxisE = lineChartEquilibrio.axisLeft
            yAxisE.granularity = 1f
            yAxisE.setDrawZeroLine(true)
            yAxisE.setDrawAxisLine(false) // no axis line
            yAxisE.setDrawGridLines(true) // no grid lines horizontales
            yAxisE.setDrawZeroLine(true)
            yAxisE.zeroLineColor = Color.parseColor("#D0D8E0")
            yAxisE.zeroLineWidth = 2f
            yAxisE.axisMaximum = 4.5f

        }

        //Capacidad de Atención
        if (pos == 3) {
            //Arreglo para mostrar los dias como labels
            dbReference.child("Capacidad de Atención").addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        fechasArrayA.clear()
                        for (ds in snapshot.children) {
                            if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                                val labelX = ds.child("x").value.toString()
                                fechasArrayA.add(labelX)
                            }
                        }
                        xAxisA.valueFormatter = IndexAxisValueFormatter(fechasArrayA)
                        lineChartAtencion.invalidate()
                    }
                }
            })

            //eje x
            xAxisA = lineChartAtencion.xAxis
            xAxisA.granularity = 1f
            xAxisA.position = XAxis.XAxisPosition.BOTTOM
            xAxisA.axisMinimum = 0f
            xAxisA.labelRotationAngle = 0f
            xAxisA.position = XAxis.XAxisPosition.BOTTOM
            xAxisA.setDrawGridLines(true) //config lineas verticales
            xAxisA.textSize = 12f

            //eje y
            lineChartAtencion.axisRight.isEnabled = false //deshabilita el eje y a la derecha
            yAxisA = lineChartAtencion.axisLeft
            yAxisA.granularity = 1f
            yAxisA.setDrawZeroLine(true)
            yAxisA.setDrawAxisLine(false) // no axis line
            yAxisA.setDrawGridLines(true) // no grid lines horizontales
            yAxisA.setDrawZeroLine(true)
            yAxisA.zeroLineColor = Color.parseColor("#D0D8E0")
            yAxisA.zeroLineWidth = 2f
            yAxisA.axisMaximum = 4.5f

        }

        //Motricidad Fina
        if (pos == 4) {
            //Arreglo para mostrar los dias como labels
            dbReference.child("Motricidad Fina").addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        fechasArrayMF.clear()
                        for (ds in snapshot.children) {
                            if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                                val labelX = ds.child("x").value.toString()
                                fechasArrayMF.add(labelX)
                            }
                        }
                        xAxisMF.valueFormatter = IndexAxisValueFormatter(fechasArrayMF)
                        lineChartMFina.invalidate()
                    }
                }
            })

            //eje x
            xAxisMF = lineChartMFina.xAxis
            xAxisMF.granularity = 1f
            xAxisMF.position = XAxis.XAxisPosition.BOTTOM
            xAxisMF.axisMinimum = 0f
            xAxisMF.labelRotationAngle = 0f
            xAxisMF.position = XAxis.XAxisPosition.BOTTOM
            xAxisMF.setDrawGridLines(true) //config lineas verticales
            xAxisMF.textSize = 12f

            //eje y
            lineChartMFina.axisRight.isEnabled = false //deshabilita el eje y a la derecha
            yAxisMF = lineChartMFina.axisLeft
            yAxisMF.granularity = 1f
            yAxisMF.setDrawZeroLine(true)
            yAxisMF.setDrawAxisLine(false) // no axis line
            yAxisMF.setDrawGridLines(true) // no grid lines horizontales
            yAxisMF.setDrawZeroLine(true)
            yAxisMF.zeroLineColor = Color.parseColor("#D0D8E0")
            yAxisMF.zeroLineWidth = 2f
            yAxisMF.axisMaximum = 4.5f

        }

        //Discrimicación de Colores
        if (pos == 5) {
            //Arreglo para mostrar los dias como labels
            dbReference.child("Discriminación de Colores").addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        fechasArrayColores.clear()
                        for (ds in snapshot.children) {
                            if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                                val labelX = ds.child("x").value.toString()
                                fechasArrayColores.add(labelX)
                            }
                        }
                        xAxisColores.valueFormatter = IndexAxisValueFormatter(fechasArrayColores)
                        lineChartColores.invalidate()
                    }
                }
            })

            //eje x
            xAxisColores = lineChartColores.xAxis
            xAxisColores.granularity = 1f
            xAxisColores.position = XAxis.XAxisPosition.BOTTOM
            xAxisColores.axisMinimum = 0f
            xAxisColores.labelRotationAngle = 0f
            xAxisColores.position = XAxis.XAxisPosition.BOTTOM
            xAxisColores.setDrawGridLines(true) //config lineas verticales
            xAxisColores.textSize = 12f

            //eje y
            lineChartColores.axisRight.isEnabled = false //deshabilita el eje y a la derecha
            yAxisColores = lineChartColores.axisLeft
            yAxisColores.granularity = 1f
            yAxisColores.setDrawZeroLine(true)
            yAxisColores.setDrawAxisLine(false) // no axis line
            yAxisColores.setDrawGridLines(true) // no grid lines horizontales
            yAxisColores.setDrawZeroLine(true)
            yAxisColores.zeroLineColor = Color.parseColor("#D0D8E0")
            yAxisColores.zeroLineWidth = 2f
            yAxisColores.axisMaximum = 4.5f

        }

        //Integración Viso-Motora
        if (pos == 6) {
            //Arreglo para mostrar los dias como labels
            dbReference.child("Visomotora").addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        fechasArrayV.clear()
                        for (ds in snapshot.children) {
                            if (ds.key!! in fechaInicio.toString()..fechaFin.toString()) {
                                val labelX = ds.child("x").value.toString()
                                fechasArrayV.add(labelX)
                            }
                        }
                        xAxisV.valueFormatter = IndexAxisValueFormatter(fechasArrayV)
                        lineChartVisomotora.invalidate()
                    }
                }
            })

            //eje x
            xAxisV = lineChartVisomotora.xAxis
            xAxisV.granularity = 1f
            xAxisV.position = XAxis.XAxisPosition.BOTTOM
            xAxisV.axisMinimum = 0f
            xAxisV.labelRotationAngle = 0f
            xAxisV.position = XAxis.XAxisPosition.BOTTOM
            xAxisV.setDrawGridLines(true) //config lineas verticales
            xAxisV.textSize = 12f

            //eje y
            lineChartVisomotora.axisRight.isEnabled = false //deshabilita el eje y a la derecha
            yAxisV = lineChartVisomotora.axisLeft
            yAxisV.granularity = 1f
            yAxisV.setDrawZeroLine(true)
            yAxisV.setDrawAxisLine(false) // no axis line
            yAxisV.setDrawGridLines(true) // no grid lines horizontales
            yAxisV.setDrawZeroLine(true)
            yAxisV.zeroLineColor = Color.parseColor("#D0D8E0")
            yAxisV.zeroLineWidth = 2f
            yAxisV.axisMaximum = 4.5f
        }

    }


    //---------------------------------------- GRÁFICO RADAR ------------------------------------------------//
    private fun graficoRadar() {
        buttonDescargarR.setOnClickListener {
            if(radarChart.saveToGallery(System.currentTimeMillis().toString(),85)){
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