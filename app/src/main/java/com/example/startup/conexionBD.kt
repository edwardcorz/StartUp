package com.example.startup

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class conexionBD {

    //val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    fun recuperarContraseña(contexto : Context, email:String){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(contexto, "Se ha enviado un correo electrónico de restablecimiento de contraseña", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(contexto, "Error al enviar el correo electrónico de restablecimiento de contraseña", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun cargarFoto(root : View, contexto : Context){
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = db.collection("usuarios").document(userId!!)
        val foto = root.findViewById<ImageView>(R.id.imageView)
        docRef.get().addOnSuccessListener { document ->
            val valorCampo = document.getString("fotoPerfilUrl")

            Glide.with(contexto)
                .load(valorCampo)
                .into(foto)
            Log.i("TAG", "?????????????????????????? $valorCampo")
        }.addOnFailureListener { exception ->
            // Ocurrió un error al obtener el documento
            Log.d("TAG", "Error al obtener el documento: $exception")
        }

    }

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


    }
}