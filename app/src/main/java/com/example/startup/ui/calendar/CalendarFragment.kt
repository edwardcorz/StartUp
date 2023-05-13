package com.example.startup.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import com.example.startup.R
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.startup.conexionBD
import com.example.startup.databinding.FragmentCalendarBinding
import com.example.startup.ui.configuration.Listeners
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarFragment : Listeners() {
    private val conexion = conexionBD()
    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Mostrar nombre del usuario

        var textView = root.findViewById<TextView>(R.id.nombre_banner)
        var textViewPlan = root.findViewById<TextView>(R.id.plan)


        conexion.conexionNombre(textView)
        conexion.extraerPlan(textViewPlan)
        conexion.cargarFoto(requireContext(), root)

        val calendarView = root.findViewById<CalendarView>(R.id.calendarView)

        val calendarInstance = Calendar.getInstance()

        val defaultDateInMillis: Long = calendarView.date

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = defaultDateInMillis
        }

        val defaultDate = calendar.time

        val dateFormat = SimpleDateFormat("EEE dd MMM", Locale("es", "ES"))
        val formattedDate = dateFormat.format(defaultDate)

        root.findViewById<TextView>(R.id.fecha).setText(formattedDate.toString())

        val formato1 = SimpleDateFormat("EEEE", Locale("es", "ES"))
        val diaAux = formato1.format(calendarInstance.time)

        cargarDatos(diaAux , root)

        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->

            calendarInstance.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("EEE dd MMM", Locale("es", "ES"))

            val fechaSeleccionada = dateFormat.format(calendarInstance.time)
            root.findViewById<TextView>(R.id.fecha).setText(fechaSeleccionada.toString())
            // fecha para la BD
            val dateFormat2 = SimpleDateFormat("EEEE", Locale("es", "ES"))
            val dayOfWeekString = dateFormat2.format(calendarInstance.time)
            cargarDatos(dayOfWeekString , root)

            val agendar = root.findViewById<Button>(R.id.agendar_clase)
            agendarClase(requireContext(),agendar, fechaSeleccionada)
            conexion.cambiarBoton(requireContext(),fechaSeleccionada, agendar)
            //conexion.verificarFechaAgendada(requireContext(),fechaSeleccionada, agendar)

        }
        return root
    }

    fun cargarDatos(dayOfWeekString:String, root:View){
        val entrenamiento = "warm up"
        conexion2(dayOfWeekString ,entrenamiento) { seriesSalida ->
            root.findViewById<TextView>(R.id.seriesWarmUp).text = seriesSalida
        }
        val entrenamiento2 = "wod"
        conexion2(dayOfWeekString ,entrenamiento2) { seriesSalida ->
            root.findViewById<TextView>(R.id.seriesWod).text = seriesSalida
        }
    }

    fun conexion2(dia: String, entrenamiento:String, callback: (String) -> Unit) {

        val mDatabase = FirebaseDatabase.getInstance().getReference(" Ejercicios/$dia")
        var seriesWod = ""
        var seriesWarmUp = ""
        mDatabase.child(entrenamiento).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (ejercicioSnapshot in dataSnapshot.children) {
                        val ejercicio = ejercicioSnapshot.key
                        val repeticiones = ejercicioSnapshot.getValue(String::class.java)
                        seriesWarmUp+= ejercicio+" - "+repeticiones+"\n"

                    }
                    callback(seriesWarmUp)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("TAG", "Error al leer los datos.")
            }
        })


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}