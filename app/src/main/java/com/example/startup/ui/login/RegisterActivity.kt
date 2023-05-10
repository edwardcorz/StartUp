package com.example.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
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

        //--- desde aquí modificaciones
        val registerButton: Button = findViewById(R.id.button_register)

        registerButton.setOnClickListener {
            performSignUp()
        }

        //lets get the email and password from the user
    }

    private fun performSignUp() {
        val email = findViewById<EditText>(R.id.editText_email_register)
        val password = findViewById<EditText>(R.id.editText_password_register)
        //Campos que faltan para el registro *********{
        val id = findViewById<EditText>(R.id.editText_ID_register)
        val direccion = findViewById<EditText>(R.id.editText_direccion_register)
        val telefono = findViewById<EditText>(R.id.editText_telefono_register)
        //******************************************+*}

        //Validar que todos los campos estén llenos para registrar usuario
        if (email.text.isEmpty() || password.text.isEmpty() || id.text.isEmpty() || direccion.text.isEmpty() || telefono.text.isEmpty()){
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()
        val inputName = id.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, move to the next activity i.e. MainActivity

                    // ------------------- guardar datos -----------

                    val currentUser = auth.currentUser
                    // Verifica si el usuario está autenticado y asigna el nombre
                    if (currentUser != null) {
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

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(baseContext, "Exitoso",
                        Toast.LENGTH_SHORT).show()

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Ocurrió un error ${it.localizedMessage}",Toast.LENGTH_SHORT)
                    .show()
            }

    }


}