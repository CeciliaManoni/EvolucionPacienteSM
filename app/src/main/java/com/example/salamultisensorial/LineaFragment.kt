package com.example.salamultisensorial

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.*
import kotlinx.android.synthetic.*

class LineaFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference

    //---------------Variables gráficos de línea---------------//
    //Parámetro memoria
    private lateinit var lineChartMemoria: LineChart
    private lateinit var lineDataSetM: LineDataSet
    private lateinit var lineDataM: LineData
    private lateinit var xAxisM: XAxis
    private lateinit var yAxisM: YAxis
    private val valorDatosLineaM: MutableList<Entry> = ArrayList()
    private var fechasArrayM: MutableList<String> = ArrayList()
    //Parámetro equilibrio postural
    private lateinit var lineChartEquilibrio: LineChart
    private lateinit var lineDataSetE: LineDataSet
    private lateinit var lineDataE: LineData
    private lateinit var xAxisE: XAxis
    private lateinit var yAxisE: YAxis
    private val valorDatosLineaE: MutableList<Entry> = ArrayList()
    private var fechasArrayE: MutableList<String> = ArrayList()
    //Parámetro capacidad de atención
    private lateinit var lineChartAtencion: LineChart
    private lateinit var lineDataSetA: LineDataSet
    private lateinit var lineDataA: LineData
    private lateinit var xAxisA: XAxis
    private lateinit var yAxisA: YAxis
    private val valorDatosLineaA: MutableList<Entry> = ArrayList()
    private var fechasArrayA: MutableList<String> = ArrayList()
    private var datoX =0f
    private var pos = 0 //Variable de selección de configuración de gráficos
    //Evaluación integral
    private lateinit var lineChart: LineChart
    private lateinit var lineData: LineData
    private lateinit var xAxis: XAxis
    private lateinit var yAxis: YAxis
    private val valorDatosLinea: MutableList<Entry> = ArrayList()
    var dataSets: ArrayList<ILineDataSet> = ArrayList()

    //Variables gráfico radar
    private lateinit var radarChart: RadarChart
    private lateinit var radarDataSet: RadarDataSet
    private lateinit var radarData: RadarData
    private lateinit var xAxisr: XAxis
    private lateinit var yAxisr: YAxis
    private val valorDatosRadar: MutableList<RadarEntry> = ArrayList()
    private var promedioMemoria = 0f
    private var promedioEquilibro = 0f
    private var promedioAtencion = 0f
    private val arrayParametros = arrayListOf("Memoria",
        "Equilibrio",
        "Atención")

    //Variables con datos paciente
    private var pacienteDni:String? = ""
    private var fechaInicio:String? = ""
    private var fechaFin:String? = ""
    private var control = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_linea, container, false)

        lineChartMemoria = view.findViewById(R.id.lineChartMemoria)
        lineChartAtencion = view.findViewById(R.id.lineChartAtencion)
        lineChartEquilibrio = view.findViewById(R.id.lineChartEquilibrio)
        lineChart = view.findViewById(R.id.lineChart)
        radarChart = view.findViewById(R.id.radarChart)

        //Recibir los datos enviados desde el bundle de MainActivity
        pacienteDni = arguments?.getString("pacienteDni")
        fechaInicio = arguments?.getString("fechaInicio")
        fechaFin = arguments?.getString("fechaFin")



        //Ubicar la referencia de la bd en la carpeta del paciente seleccionado
        dbReference = FirebaseDatabase.getInstance().getReference("Pacientes").
        child(pacienteDni.toString())

        sinDatos()
        graficoLinea()
        return view
    }

    private fun sinDatos() {
        //Gráficos de línea
        lineChartMemoria.setNoDataText("No hay datos disponibles")
        lineChartMemoria.setNoDataTextColor(Color.parseColor("#484848"))
        lineChartEquilibrio.setNoDataText("No hay datos disponibles")
        lineChartEquilibrio.setNoDataTextColor(Color.parseColor("#484848"))
        lineChartAtencion.setNoDataText("No hay datos disponibles")
        lineChartAtencion.setNoDataTextColor(Color.parseColor("#484848"))
        lineChart.setNoDataText("No hay datos disponibles")
        lineChart.setNoDataTextColor(Color.parseColor("#484848"))

        //Gráfico radar
        radarChart.setNoDataText("No hay datos disponibles")
        radarChart.setNoDataTextColor(Color.parseColor("#484848"))
    }

    private fun lineaIntegral(){
        //Configuracion descripción
        lineChart.description.isEnabled = false

        //Configuración leyenda
        val legend: Legend = lineChart.legend
        legend.isEnabled = false

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

        //Configuración general
        lineData = LineData(dataSets)
        lineChart.data = lineData
        lineChart.setDrawGridBackground(true)
        lineChart.setDrawBorders(false)
        lineChart.setBackgroundColor(Color.parseColor("#E7E9EC"))
        lineChart.xAxis.axisMinimum = 0f
        lineChart.axisLeft.axisMinimum = 0f
        lineChart.isDragEnabled = true
        lineChart.invalidate()
    }

    //-------------------- GRÁFICO DE LÍNEA ----------------------//
    private fun graficoLinea() {
        //Limpiar datos del arreglo Radar
        valorDatosRadar.clear()

        //Búsqueda de pares ordenados (x,y) en la bd
        //Primero se buscan los datos del parámetro memoria
        dbReference.child("Memoria").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var i = 0f
                    var sumMemoria = 0f
                    valorDatosLineaM.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {
                        if(ds.key!! in fechaInicio.toString()..fechaFin.toString()){
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaM.add(Entry(datoX, datoY))
                            sumMemoria += datoY
                        }
                    }
                    promedioMemoria = sumMemoria/i
                    valorDatosRadar.add(RadarEntry(promedioMemoria))
                    graficoRadar()

                    //Asignar el set de datos al gráfico de línea de memoria
                    lineDataSetM = LineDataSet(valorDatosLineaM,"Memoria")
                    lineDataM = LineData(lineDataSetM)
                    lineChartMemoria.data = lineDataM

                    //Asignar el set de datos al gráfico de evolución integral
                    dataSets.add(lineDataSetM)
                    lineaIntegral()


                    //configuracion para deshabilitar descripción
                    lineChartMemoria.description.isEnabled = false
                    //configuracion para deshabilitar referencias
                    val legend: Legend = lineChartMemoria.legend
                    legend.isEnabled = false

                    pos = 1
                    lineaEjesConfig()
                    lineaEstiloConfig()
                    lineaConfigGral()

                }

            }
        })

        //Segundo se buscan los datos del parámetro equilibrio postural
        dbReference.child("Equilibrio Postural").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var i = 0f
                    var sumEquilibrio = 0f
                    valorDatosLineaE.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if(ds.key!! in fechaInicio.toString()..fechaFin.toString()){
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaE.add(Entry(datoX, datoY))
                            sumEquilibrio += datoY
                        }
                    }
                    promedioEquilibro = sumEquilibrio/i
                    valorDatosRadar.add(RadarEntry(promedioEquilibro))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetE = LineDataSet(valorDatosLineaE,"Equilibrio Postural")
                    lineDataE = LineData(lineDataSetE)
                    lineChartEquilibrio.data = lineDataE

                    dataSets.add(lineDataSetE)
                    lineaIntegral()

                    //configuracion para deshabilitar descripción
                    lineChartEquilibrio.description.isEnabled = false
                    //configuracion para deshabilitar referencias
                    val legend: Legend = lineChartEquilibrio.legend
                    legend.isEnabled = false

                    pos = 2
                    lineaEjesConfig()
                    lineaEstiloConfig()
                    lineaConfigGral()
                }

            }
        })

        //Tercero se buscan los datos del parámetro capacidad de atención
        dbReference.child("Capacidad de Atención").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var i = 0f
                    var sumAtencion = 0f
                    valorDatosLineaA.clear() //Limpiar datos anteriores
                    for (ds in snapshot.children) {

                        if(ds.key!! in fechaInicio.toString()..fechaFin.toString()){
                            val datoY = ds.child("y").value.toString().toFloat()
                            datoX = i
                            i += 1
                            valorDatosLineaA.add(Entry(datoX, datoY))
                            sumAtencion += datoY
                        }

                    }
                    promedioAtencion = sumAtencion/i
                    valorDatosRadar.add(RadarEntry(promedioAtencion))
                    graficoRadar()

                    //Asignar el set de datos al gráfico
                    lineDataSetA = LineDataSet(valorDatosLineaA,"Capacidad de Atención")
                    lineDataA = LineData(lineDataSetA)
                    lineChartAtencion.data = lineDataA

                    dataSets.add(lineDataSetA)
                    lineaIntegral()

                    //configuracion para deshabilitar descripción
                    lineChartAtencion.description.isEnabled = false
                    //configuracion para deshabilitar referencias
                    val legend: Legend = lineChartAtencion.legend
                    legend.isEnabled = false

                    pos = 3
                    lineaEjesConfig()
                    lineaEstiloConfig()
                    lineaConfigGral()


                }

            }
        })

    }

    //Funciones gráfico de línea
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

        //Capacidad de atención
        if (pos == 3) {
            lineChartAtencion.setDrawGridBackground(true)
            lineChartAtencion.setDrawBorders(false)
            lineChartAtencion.setBackgroundColor(Color.parseColor("#E7E9EC"))
            lineChartAtencion.xAxis.axisMinimum = 0f
            lineChartAtencion.axisLeft.axisMinimum = 0f
            lineChartAtencion.isDragEnabled = true
            lineChartAtencion.invalidate()
        }
    }
    private fun lineaEstiloConfig() {
        //configuración estilo de la línea
        //Memoria
        if (pos == 1) {
            lineDataSetM.lineWidth = 3f
            lineDataSetM.color = Color.parseColor("#4F9ECD")
            lineDataSetM.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetM.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetM.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetM.setDrawCircleHole(false) //puntos circulos
            lineDataSetM.setCircleColor(Color.parseColor("#1088D0")) //color del circulo
            lineDataSetM.circleRadius = 3f
        }

        //Equilibrio Postural
        if (pos == 2) {
            lineDataSetE.lineWidth = 3f
            lineDataSetE.color = Color.parseColor("#FFA726") //DF9B4B
            lineDataSetE.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetE.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetE.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetE.setDrawCircleHole(false) //puntos circulos
            lineDataSetE.setCircleColor(Color.parseColor("#F09020")) //color del circulo
            lineDataSetE.circleRadius = 3f
        }

        //Capacidad de Atención
        if (pos == 3) {
            lineDataSetA.lineWidth = 3f
            lineDataSetA.color = Color.parseColor("#90B838") //DF9B4B
            lineDataSetA.setDrawValues(false) //elimina el valor de "y" en el lugar del punto
            lineDataSetA.setDrawFilled(false) //pinta el área debajo de la curva de color
            lineDataSetA.setDrawCircles(true) //puntos de pares ordenados
            lineDataSetA.setDrawCircleHole(false) //puntos circulos
            lineDataSetA.setCircleColor(Color.parseColor("#558B2F")) //color del circulo
            lineDataSetA.circleRadius = 3f
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
        }
    }


    //-------------------- GRÁFICO RADAR ----------------------//
    private fun graficoRadar() {
        //Asignar el set de datos al gráfico
        radarDataSet = RadarDataSet(valorDatosRadar,"")
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
        radarChart.setBackgroundColor(Color.TRANSPARENT)

        //configuración de los ejes
        yAxisr = radarChart.yAxis
        yAxisr.isEnabled = false
        yAxisr.axisMinimum = 0f
        xAxisr = radarChart.xAxis
        radarDataSet.valueTextSize = 10f
        xAxisr.valueFormatter = IndexAxisValueFormatter(arrayParametros)
        xAxisr.textSize = 11f
        radarChart.scaleY = 1.1f
        radarChart.scaleX = 1.1f

        radarChart.textAlignment = View.TEXT_ALIGNMENT_CENTER
        radarChart.setTouchEnabled(false)
        radarChart.invalidate()
    }
}