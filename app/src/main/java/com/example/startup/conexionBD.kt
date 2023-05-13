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
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class conexionBD {

    val auth = FirebaseAuth.getInstance()


    val db = FirebaseFirestore.getInstance()
    //val auth = FirebaseAuth.getInstance()


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

    fun guardarPagos(contexto: Context, inputName : String, inputNumTarjeta: String, inputMes: String, inputAño: String,inputCvv: String, inputTipo: String, inputCorre: String){
        val currentUser = auth.currentUser

        val db = FirebaseFirestore.getInstance()
        // Verifica si el usuario está autenticado y asigna el nombre
        if (currentUser != null) {
            val userDocumentRef = db.collection("usuariosPagos").document(currentUser.uid)
            val userData = hashMapOf(
                "nombreTitular" to inputName,
                "numeroTarjeta" to inputNumTarjeta,
                "mes" to inputAño,
                "año" to inputCvv,
                "cvv" to inputCvv,
                "tipo" to inputTipo,
                "correo" to inputCorre
            )
            userDocumentRef.set(userData)
                .addOnSuccessListener {
                    Toast.makeText(contexto, "Los datos se guardaron correctamente", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener { e ->
                    Toast.makeText(contexto, "Los datos no se guardaron correctamente", Toast.LENGTH_SHORT).show()


                }
            //---------------

            // Crea un objeto UserProfileChangeRequest para actualizar el nombre
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(inputName)
                .build()
            // Actualiza el perfil del usuario con el nuevo nombre

            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener { profileUpdateTask ->
                    if (profileUpdateTask.isSuccessful) {
                        // El nombre se actualizó correctamente
                        // Puedes realizar otras acciones necesarias
                    } else {
                        // Hubo un error al actualizar el nombre
                        // Maneja el error apropiadamente
                    }
                }
        }
    }

    fun cargarFoto( contexto : Context, root : View){
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = db.collection("usuarios").document(userId!!)
        docRef.get().addOnSuccessListener { document ->
            val valorCampo = document.getString("fotoPerfilUrl")

            Glide.with(contexto)
                .load(valorCampo)
                .into(root.findViewById<ImageView>(R.id.imageView))
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