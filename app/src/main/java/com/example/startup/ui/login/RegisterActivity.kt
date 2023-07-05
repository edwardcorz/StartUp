package com.example.startup.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.startup.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var controller: RegisterController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //Login Activity -> Hacer que el texto envíe a la interfaz de login.
        val loginText: TextView = findViewById(R.id.textView_login_now)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Inicializar el modelo y el controlador
        controller = RegisterController(this)

        //--- desde aquí modificaciones
        val registerButton: Button = findViewById(R.id.button_register)
        registerButton.setOnClickListener {
            controller.performSignUp()
        }
    }

    // Método de la vista para mostrar un mensaje de éxito
    fun showSuccessMessage() {
        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
    }

    // Método de la vista para mostrar un mensaje de error
    fun showErrorMessage() {
        Toast.makeText(this, "Error de autenticación.", Toast.LENGTH_SHORT).show()
    }
}


// Controlador
class RegisterController(private val view: RegisterActivity) {

    private lateinit var auth: FirebaseAuth

    init {
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    fun performSignUp() {
        val activity = view

        val email = activity.findViewById<EditText>(R.id.editText_email_register)
        val password = activity.findViewById<EditText>(R.id.editText_password_register)
        // Campos que faltan para el registro *********{
        val id = activity.findViewById<EditText>(R.id.identifier)
        val nombre = activity.findViewById<EditText>(R.id.nombre)
        val direccion = activity.findViewById<EditText>(R.id.direccion)
        val telefono = activity.findViewById<EditText>(R.id.telefono)
        //******************************************+*}

        // Validar que todos los campos estén llenos para registrar usuario
        if (email.text.isEmpty() || password.text.isEmpty() || id.text.isEmpty() || direccion.text.isEmpty() || telefono.text.isEmpty()) {
            Toast.makeText(activity, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()
        val inputIdentifier = id.text.toString()
        val inputName = nombre.text.toString()
        val inputDireccion = direccion.text.toString()
        val inputTelefono = telefono.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // ------------------- guardar datos -----------
                    val currentUser = auth.currentUser
                    val db = FirebaseFirestore.getInstance()
                    // Verifica si el usuario está autenticado y asigna el nombre
                    if (currentUser != null) {
                        val userDocumentRef = db.collection("usuarios").document(currentUser.uid)
                        val userData = hashMapOf(
                            "nombre" to inputName,
                            "documento" to inputIdentifier,
                            "direccion" to inputDireccion,
                            "telefono" to inputTelefono
                        )
                        userDocumentRef.set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(activity, "Los datos se guardaron correctamente", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener { e ->
                                Toast.makeText(activity, "Los datos no se guardaron correctamente", Toast.LENGTH_SHORT).show()
                            }

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

                    // -------------------------------------------

                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                    activity.showSuccessMessage()
                } else {
                    activity.showErrorMessage()
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Ocurrió un error ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}
