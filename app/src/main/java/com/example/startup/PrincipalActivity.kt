package com.example.startup

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.echo.holographlibrary.Bar
import com.echo.holographlibrary.BarGraph
import com.example.startup.databinding.ActivityPrincipalBinding
import kotlin.math.roundToInt

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val puntos = ArrayList<Bar>()
        val mes = arrayOf<String>("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" )
        for (i in mes) {
            graficarBarras(puntos, i)
        }

    }

    fun graficarBarras(puntos: ArrayList<Bar>, mes: String){

        var cant = (Math.random()* 15).roundToInt()// Cambia segun lo almacenado en base de datos

        val barra = Bar()
        var color = "#C81D25"
        barra.color = Color.parseColor(color)
        barra.name = mes
        barra.value = cant.toFloat()
        barra.valueString = cant.toString()

        puntos.add(barra)

        val grafica = findViewById<View>(R.id.graphBar) as BarGraph
        grafica.bars = puntos
    }
}