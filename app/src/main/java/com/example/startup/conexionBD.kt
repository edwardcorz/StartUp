package com.example.startup

import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class conexionBD {

    //val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()


    fun conexionNombre(textView : TextView){
        Log.i("TAG", "conexion de nombre exitosa-----------")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = db.collection("usuarios").document(userId!!)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val nombre = documentSnapshot.getString("nombre")
                    Log.i("TAG", "conexion de nombre $nombre-----------")
                    textView.text = nombre

                    // Hacer algo con el nombre obtenido
                } else {
                    // El documento no existe o no tiene el campo "nombre"
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el error de lectura del documento
            }

        //val currentUser = auth.currentUser

        /*
        if (currentUser != null) {
            val name = currentUser.displayName
            Log.i("TAG", "----------- $name")
            if (name != null) {

                Log.i("TAG", "conexion de nombre exitosa ")
                textView.text = name
            } else {
                Log.i("TAG", "no tiene un nombre establecido")
            }
        } else {
            Log.i("TAG", "El usuario no está autenticado")
        }*/
    }
}