package com.example.startup.ui.calendar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import com.example.startup.R
import android.util.Log
import android.widget.TextView
import com.example.startup.databinding.FragmentCalendarBinding
import com.example.startup.databinding.FragmentHomeBinding
import com.example.startup.ui.home.HomeViewModel
import com.google.firebase.database.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this)[CalendarViewModel::class.java]

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
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

        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->

            calendarInstance.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("EEE dd MMM", Locale("es", "ES"))

            val fechaSeleccionada = dateFormat.format(calendarInstance.time)
            root.findViewById<TextView>(R.id.fecha).setText(fechaSeleccionada.toString())
            // fecha para la BD
            val dateFormat2 = SimpleDateFormat("EEEE", Locale("es", "ES"))
            val dayOfWeekString = dateFormat2.format(calendarInstance.time)

            //conexion2(dayOfWeekString)
            conexion2(dayOfWeekString)
        }

        return root

    }

    fun conexion2(dia :String){


        val mDatabase = FirebaseDatabase.getInstance().getReference(" Ejercicios/$dia")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var seriesSalida = StringBuilder()
                if (dataSnapshot.exists()) {
                    val ejerciciosMap = dataSnapshot.value as Map<*, *>
                    Log.i("TAG","Ejercicios para el día $dia:")
                    for ((nombre, series) in ejerciciosMap ) {
                        seriesSalida.append(series).append("\n")
                        //Log.i("FLAG"," $nombre $series:")
                    }
                    Log.i("TAG"," $seriesSalida")
                    //findViewById<TextView>(R.id.serie).setText("Este es un texto largo que se desea mostrar en un TextView. El texto puede tener muchas líneas y ser muy extenso. Para mostrar el texto completo, se puede utilizar el atributo android:maxLines=\"unlimited\" en el archivo XML del layout del TextView.")
                    //root.findViewById<TextView>(R.id.serie).setText(seriesSalida)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Código que se ejecuta cuando ocurre un error al obtener los datos de Firebase
                Log.i("TAG", "Error al leer los datos.")
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}