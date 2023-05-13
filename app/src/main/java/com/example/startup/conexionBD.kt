package com.example.startup

import android.content.Context
import android.graphics.Color
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
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


    fun extraerPlan(textView : TextView){
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = db.collection("usuariosPagos").document(userId!!)
        docRef.get().addOnSuccessListener { document ->
            val valorCampo = document.getString("plan")
            textView.text = valorCampo
        }.addOnFailureListener { exception ->
            // Ocurrió un error al obtener el documento
            Log.d("TAG", "Error al obtener el documento: $exception")
        }

    }
    fun guardarPagos(contexto: Context, inputName : String, inputNumTarjeta: String, inputMes: String, inputAño: String,inputCvv: String, inputTipo: String, inputCorre: String, inputPlan:String, texto:TextView){
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
                "correo" to inputCorre,
                "plan" to inputPlan
            )
            userDocumentRef.set(userData)
                .addOnSuccessListener {
                    Toast.makeText(contexto, "Pago hecho exitosamente!", Toast.LENGTH_SHORT).show()
                    extraerPlan(texto)

                }.addOnFailureListener { e ->
                    Toast.makeText(contexto, "Error en el pago", Toast.LENGTH_SHORT).show()

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


    fun guardarClase(contexto: Context, fecha: String, buttonAgendar: Button) {
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()

        if (currentUser != null) {
            val userDocumentRef = db.collection("usuariosAgenda").document(currentUser.uid)

            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    // Obtener los datos existentes del documento
                    val userData = documentSnapshot.data

                    if (userData != null) {
                        // Agregar la nueva fecha al campo existente
                        userData[fecha] = "Agendada"

                        userDocumentRef.set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(contexto, "Se agendó exitosamente!", Toast.LENGTH_SHORT).show()
                                buttonAgendar.setText("Cancelar")
                                buttonAgendar.setBackgroundColor(Color.RED)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(contexto, "Error en el pago", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(contexto, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                }
        }
    }


    fun eliminarFechaAgendada(contexto: Context, fecha: String, buttonAgendar: Button) {
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()

        if (currentUser != null) {
            val userDocumentRef = db.collection("usuariosAgenda").document(currentUser.uid)

            userDocumentRef.update(fecha, FieldValue.delete())
                .addOnSuccessListener {
                    Toast.makeText(contexto, "Fecha eliminada exitosamente", Toast.LENGTH_SHORT).show()
                    buttonAgendar.setText("Agendar")
                    buttonAgendar.setBackgroundColor(Color.BLUE)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(contexto, "Error al eliminar la fecha", Toast.LENGTH_SHORT).show()
                }
        }
    }


    fun verificarFechaAgendada(contexto: Context, fecha: String, callback: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()

        if (currentUser != null) {
            val userDocumentRef = db.collection("usuariosAgenda").document(currentUser.uid)

            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    // Obtener los datos existentes del documento
                    val userData = documentSnapshot.data

                    if (userData != null) {
                        // Verificar si la fecha está agendada
                        val agendada = userData.containsKey(fecha) && userData[fecha] == "Agendada"
                        callback(agendada)
                    } else {
                        callback(false)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(contexto, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
        } else {
            callback(false)
        }
    }



    fun cambiarBoton(contexto: Context, fecha: String, buttonAgendar: Button) {
        verificarFechaAgendada(contexto, fecha) { agendada ->
            if (agendada) {
                buttonAgendar.setText("Cancelar")
                buttonAgendar.setBackgroundColor(Color.RED)
            } else {
                buttonAgendar.setText("Agendar")
                buttonAgendar.setBackgroundColor(Color.BLUE)
            }
        }
    }

}