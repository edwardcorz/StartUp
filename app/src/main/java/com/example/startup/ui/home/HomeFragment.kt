package com.example.startup.ui.home

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.startup.R
import com.example.startup.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Crea el gráfico de barras
        val chart = root.findViewById<BarChart>(R.id.chart)

        val entries = listOf(
            BarEntry(0f, 4f),
            BarEntry(1f, 6f),
            BarEntry(2f, 8f),
            BarEntry(3f, 2f)
        )

        val dataSet = BarDataSet(entries, "Ejemplo de datos")
        dataSet.color = Color.BLUE

        val data = BarData(dataSet)
        data.barWidth = 0.9f

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


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}