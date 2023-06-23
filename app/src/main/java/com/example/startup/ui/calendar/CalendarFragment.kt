package com.example.startup.ui.calendar

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import com.example.startup.conexionBD
import com.example.startup.databinding.FragmentCalendarBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {
    private lateinit var model: CalendarModel
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        model = CalendarModel(requireContext())

        binding.fecha.text = model.getCurrentDate()
        binding.agendarClase?.setOnClickListener {
            model.agendarClase()
        }

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            binding.fecha.text = model.formatDate(selectedDate)
            model.loadDatos(selectedDate)
        }

        model.loadDatos(Calendar.getInstance())

        return root
    }
}

class CalendarModel(private val context: Context) {
    private val conexion = conexionBD()
    private lateinit var binding: FragmentCalendarBinding

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE dd MMM", Locale("es", "ES"))
        return dateFormat.format(calendar.time)
    }

    fun formatDate(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("EEE dd MMM", Locale("es", "ES"))
        return dateFormat.format(calendar.time)
    }

    fun loadDatos(calendar: Calendar) {
        val dayOfWeekString = SimpleDateFormat("EEEE", Locale("es", "ES")).format(calendar.time)
        conexion2(dayOfWeekString, "warm up") { seriesSalida ->
            binding.seriesWarmUp.text = seriesSalida
        }
        conexion2(dayOfWeekString, "wod") { seriesSalida ->
            binding.seriesWod.text = seriesSalida
        }
    }

    fun conexion2(dia: String, entrenamiento: String, callback: (String) -> Unit) {
        val mDatabase = FirebaseDatabase.getInstance().getReference("Ejercicios/$dia")
        var seriesWarmUp = ""
        mDatabase.child(entrenamiento).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (ejercicioSnapshot in dataSnapshot.children) {
                        val ejercicio = ejercicioSnapshot.key
                        val repeticiones = ejercicioSnapshot.getValue(String::class.java)
                        seriesWarmUp += "$ejercicio - $repeticiones\n"
                    }
                    callback(seriesWarmUp)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("TAG", "Error al leer los datos.")
            }
        })
    }

    fun agendarClase() {
        TODO("Not yet implemented")
    }
}
