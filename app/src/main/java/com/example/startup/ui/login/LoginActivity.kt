//package com.example.startup.ui.login
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.EditText
//import android.widget.GridLayout
//import android.widget.TextView
//import android.widget.Toast
//import com.example.startup.R
//import com.example.startup.MainActivity
//import com.example.startup.conexionBD
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//
//class LoginActivity : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        val baseDatos = conexionBD()
//        auth = Firebase.auth
//
//        //Register Activity -> Hacer que el texto envíe a la interfaz de registro.
//        val registertext: TextView = findViewById(R.id.textView_register_now)
//
//        val recuperarContraseña = findViewById<TextView>(R.id.recuperar)
//
//        recuperarContraseña.setOnClickListener {
//            baseDatos.recuperarContraseña(this, findViewById<EditText>(R.id.editText_email_login).text.toString())
//        }
//
//        registertext.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//
//        val loginButton: GridLayout =findViewById(R.id.boton_completo)
//
//        loginButton.setOnClickListener {
//            performLogin()
//        }
//
//
//        //LOGIN AUTHENT
//    }
//
//    private fun performLogin() {
//        //get input from the user
//        val email: EditText = findViewById(R.id.editText_email_login)
//        val password: EditText = findViewById(R.id.editText_password_login)
//
//        //null checks on inputs
//        if (email.text.isEmpty() || password.text.isEmpty()) {
//            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT)
//                .show()
//            return
//        }
//
//        val emailInput = email.text.toString()
//        val passwordInput = password.text.toString()
//
//
//        auth.signInWithEmailAndPassword(emailInput, passwordInput)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, Navigate to the main Activity
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//
//                    Toast.makeText(baseContext, "Bienvenido",
//                        Toast.LENGTH_SHORT).show()
//
//                } else {
//                    // If sign in fails, display a message to the user.
//
//                    Toast.makeText(baseContext, "Error de autenticacón.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(baseContext, "Authentication failed. ${it.localizedMessage}",
//                    Toast.LENGTH_SHORT).show()
//            }
//    }
//}
//

package com.example.startup.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import com.example.startup.R
import com.example.startup.MainActivity
import com.example.startup.conexionBD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var controller: LoginController

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val baseDatos = conexionBD()
        auth = Firebase.auth

        // Register Activity -> Hacer que el texto envíe a la interfaz de registro.
        val registertext: TextView = findViewById(R.id.textView_register_now)
        val recuperarContraseña = findViewById<TextView>(R.id.recuperar)

        controller = LoginController(this, baseDatos)

        recuperarContraseña.setOnClickListener {
            controller.recoverPassword()
        }

        registertext.setOnClickListener {
            controller.navigateToRegister()
        }

        val loginButton: GridLayout = findViewById(R.id.boton_completo)
        loginButton.setOnClickListener {
            controller.performLogin()
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
class LoginController(
    private val view: LoginActivity,
    private val baseDatos: conexionBD
) {

    private lateinit var auth: FirebaseAuth

    init {
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    fun recoverPassword() {
        val activity = view
        val email = activity.findViewById<EditText>(R.id.editText_email_login).text.toString()
        baseDatos.recuperarContraseña(activity, email)
    }

    fun navigateToRegister() {
        val activity = view
        val intent = Intent(activity, RegisterActivity::class.java)
        activity.startActivity(intent)
    }

    fun performLogin() {
        val activity = view
        val email: EditText = activity.findViewById(R.id.editText_email_login)
        val password: EditText = activity.findViewById(R.id.editText_password_login)

        // null checks on inputs
        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(activity, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        val emailInput = email.text.toString()
        val passwordInput = password.text.toString()

        auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    activity.showSuccessMessage()
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                } else {
                    activity.showErrorMessage()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    activity,
                    "Authentication failed. ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
