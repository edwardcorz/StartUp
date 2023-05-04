package com.example.startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.TextView
import com.google.firebase.database.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        //mDatabase.child("domingo")
        /*
        val calendar = findViewById<CalendarView>(R.id.calendarView)

        calendar.setOnDateChangeListener { calendar, year, month, dayOfMonth ->
            val calendarInstance = Calendar.getInstance()
            calendarInstance.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("EEE dd MMM", Locale("es", "ES"))
            val fechaSeleccionada = dateFormat.format(calendarInstance.time)
            findViewById<TextView>(R.id.fecha).setText(fechaSeleccionada.toString())
            // fecha para la BD
            val dateFormat2 = SimpleDateFormat("EEEE", Locale("es", "ES"))
            val dayOfWeekString = dateFormat2.format(calendarInstance.time)

            //conexion(dayOfWeekString)
            //conexion2(dayOfWeekString)
        }*/
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
                    findViewById<TextView>(R.id.serie).setText(seriesSalida)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Código que se ejecuta cuando ocurre un error al obtener los datos de Firebase
                Log.i("TAG", "Error al leer los datos.")
            }
        })
    }
    fun conexion(dia :String){
        val mDatabase = FirebaseDatabase.getInstance().getReference(" Ejercicios/$dia")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.i("TAG",dataSnapshot.toString())
                if (dataSnapshot.exists()) {
                    var salida = StringBuilder()

                    for (ejercicios in dataSnapshot.children) {
                        salida.append(ejercicios.key).append(": ").append(ejercicios.value).append("\n")
                    }

                    findViewById<TextView>(R.id.ejercicios).setText(salida)
                    /*
                    val ejerciciosMap = dataSnapshot.value
                    for ((dia, ejercicios) in ejerciciosMap) {
                        Log.i("TAG","Ejercicios para el día $dia:")
                        for ((nombre, series) in ejercicios as Map<*, *>) {
                            Log.i("TAG","Ejercicios para el día $nombre $series:")
                        }
                    }*/
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Código que se ejecuta cuando ocurre un error al obtener los datos de Firebase
                Log.i("TAG", "Error al leer los datos.")
            }
        })

    }


}

