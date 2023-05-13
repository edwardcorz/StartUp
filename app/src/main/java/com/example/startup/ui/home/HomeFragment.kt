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
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

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
            BarEntry(0f, 4f),
            BarEntry(1f, 6f),
            BarEntry(2f, 8f),
            BarEntry(3f, 7f),
            BarEntry(4f, 1f),
            BarEntry(5f, 8f),
            BarEntry(6f, 3f),
            BarEntry(7f, 5f),
            BarEntry(8f, 8f),
            BarEntry(9f, 2f)
        )

        val dataSet = BarDataSet(entries, "Resumen mensual")
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
        chart.xAxis.isEnabled = false

        // Mostrar nombre del usuario

        var textView = root.findViewById<TextView>(R.id.nombre_banner)
        var textViewPlan=root.findViewById<TextView>(R.id.plan)

        conexion.conexionNombre(textView)
        conexion.extraerPlan(textViewPlan)
        conexion.cargarFoto(requireContext(),root)

        chart.axisLeft.isEnabled = false
        chart.setVisibleXRangeMaximum(8F)
        chart.moveViewToX(1F)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}