package com.example.startup.ui.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.startup.R
import com.example.startup.conexionBD
import com.example.startup.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class HomeFragment : Fragment() {
    private val conexion =conexionBD()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Crea el gráfico de barras
        val chart = root.findViewById<BarChart>(R.id.chart)

        val entries = listOf(
            BarEntry(0f, 12f),
            BarEntry(1f, 23f),
            BarEntry(2f, 14f),
            BarEntry(3f, 15f),
            BarEntry(4f, 21f),
            BarEntry(5f, 0f),
            BarEntry(6f, 0f),
            BarEntry(7f, 0f),
            BarEntry(8f, 0f),
            BarEntry(9f, 0f),
            BarEntry(10f, 0f),
            BarEntry(11f,0f)
        )

        val dataSet = BarDataSet(entries, "Resumen de entrenamiento")
        dataSet.color = Color.rgb(120, 16, 2)

        val data = BarData(dataSet)
        data.barWidth = 0.3f

        chart.data = data
        chart.setFitBars(true)
        chart.animateY(1500)
        chart.description.textColor = Color.BLACK
        chart.description.textSize = 14f
        chart.legend.isEnabled = false
        chart.description.text = ""
        chart.axisLeft.axisMinimum = 0f
        chart.axisRight.isEnabled = false

        // Configura el eje X
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value.toInt()) {
                    0 -> "Ene"
                    1 -> "Feb"
                    2 -> "Mar"
                    3 -> "Abr"
                    4 -> "May"
                    5 -> "Jun"
                    6 -> "Jul"
                    7 -> "Ago"
                    8 -> "Sep"
                    9 -> "Oct"
                    10 -> "Nov"
                    11 -> "Dic"
                    else -> ""
                }
            }
        }

        // Configura el eje Y
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.axisLeft.axisMinimum = 0f
        chart.axisRight.axisMinimum = 0f
        chart.setVisibleXRangeMaximum(8f)
        chart.moveViewToX(1f)

        // Mostrar nombre del usuario

        var textView = root.findViewById<TextView>(R.id.nombre_banner)
        var textViewPlan=root.findViewById<TextView>(R.id.plan)

        conexion.conexionNombre(textView)
        conexion.extraerPlan(textViewPlan)
        conexion.cargarFoto(requireContext(),root)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}