package com.example.startup.ui.configuration

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.startup.LoginActivity
import com.example.startup.R
import com.example.startup.conexionBD
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

open class Listeners : Fragment() {

    fun dialog1Listener(btn: Button) {
        btn.setOnClickListener {
            val inflater  = LayoutInflater.from(requireActivity())
            val dialogView = inflater.inflate(R.layout.dialog1, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogView)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            guardar(dialogView,myDialog)
        }
    }

    fun guardar(dialogView : View , myDialog: Dialog) {
        val guardar = dialogView.findViewById<Button>(R.id.guardar)
        val contraseñaEditText = dialogView.findViewById<EditText>(R.id.contraseña)
        val contraseñaConfirmarEditText = dialogView.findViewById<EditText>(R.id.confirmarContraseña)

        guardar.setOnClickListener {
            val contraseña = contraseñaEditText.text.toString()
            val contraseñaConfirmar = contraseñaConfirmarEditText.text.toString()

            if (contraseña == contraseñaConfirmar) {
                val user = FirebaseAuth.getInstance().currentUser

                user?.updatePassword(contraseña)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("TAG", "Contraseña actualizada exitosamente")
                            Toast.makeText(requireContext(), "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)

                        } else {
                            Log.e("TAG", "Error al actualizar la contraseña", task.exception)
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Verifique que las contraseñas sean iguales", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun dialog2Listener(btnx: Button) {
        btnx.setOnClickListener {
            val inflater  = LayoutInflater.from(requireActivity())
            val dialogView = inflater.inflate(R.layout.dialog2, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogView)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            logOutListener(dialogView)

            volverListener(dialogView, myDialog)
        }
    }

    fun logOutListener(dialogView : View ){
        val logOut = dialogView.findViewById<Button>(R.id.logOut)
        logOut.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun volverListener(dialogView : View ,  myDialog : Dialog){
        val volver = dialogView.findViewById<Button>(R.id.volver)
        volver.setOnClickListener {
            myDialog.dismiss()
        }
    }

    fun dialog3Listener(editBtn: TextView, root : View) {

        editBtn.setOnClickListener {
            val inflater  = LayoutInflater.from(requireActivity())
            val dialogView = inflater.inflate(R.layout.dialog3, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogView)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            cambiarNombre(dialogView,myDialog, root)
        }
    }

    fun dialogPayListener(button_pagar: Button, root : View) {

        button_pagar.setOnClickListener {
            val inflater  = LayoutInflater.from(requireActivity())
            val dialogView = inflater.inflate(R.layout.dialog_payment, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogView)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()


        }
    }

    fun cambiarNombre(dialogView : View , myDialog: Dialog, root:View){

        val guardar = dialogView.findViewById<Button>(R.id.guardarNuevoNombre)
        val nombreEdit = dialogView.findViewById<EditText>(R.id.nuevoNombre)

        guardar.setOnClickListener {
            var nombre = nombreEdit.text.toString()

            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val userRef = db.collection("usuarios").document(userId!!)

            val nuevoNombre = nombre // El nuevo nombre que deseas asignar

            val actualizaciones = hashMapOf<String, Any>(
                "nombre" to nuevoNombre
            )

            userRef.update(actualizaciones)
                .addOnSuccessListener {
                    Log.i("TAG", "El nombre se actualizó correctamente ; $nuevoNombre")
                    Toast.makeText(requireContext(), "El nombre se guardó correctamente", Toast.LENGTH_SHORT).show()
                    myDialog.dismiss()
                    var conexion = conexionBD()

                    conexion.conexionNombre(root.findViewById(R.id.nombreConfiguration))
                    // El nombre se actualizó correctamente
                }
                .addOnFailureListener { exception ->
                    // Manejar el error al actualizar el nombre
                }



            fun dialogPhotoListener(btn: Button) {
                btn.setOnClickListener {
                    val inflater  = LayoutInflater.from(requireActivity())
                    val dialogView = inflater.inflate(R.layout.dialog1, null)
                    val myDialog = Dialog(requireContext())
                    myDialog.setContentView(dialogView)
                    myDialog.setCancelable(true)
                    myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    myDialog.show()

                    guardar(dialogView,myDialog)
                }
            }

            //prueba
            /*
            val currentUser = FirebaseAuth.getInstance().currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(nombre)
                .build()

            currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("TAG", "El nombre se actualizó correctamente")
                        Toast.makeText(requireContext(), "El nombre se guardó correctamente", Toast.LENGTH_SHORT).show()

                    } else {
                        Log.i("TAG", "Hubo un error al actualizar el nombre")
                    }
                }*/
        }

    }

    fun dialogPhotoListener(imageView: ShapeableImageView, root: View) {
        imageView.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.dialog_photo, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
            editarListener(dialogBinding)
        }
    }


    val storageReference = FirebaseStorage.getInstance().reference
    fun  editarListener(dialogView: View){


        val editar = dialogView.findViewById<Button>(R.id.editar)
        editar.setOnClickListener{
            subirFoto()
        }

    }

    companion object{
        private const val PICK_IMAGE_REQUEST = 1
    }


    fun subirFoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!

            // Genera un nombre de archivo único para la imagen de perfil
            val fileName = "${System.currentTimeMillis()}_${imageUri.lastPathSegment}"

            // Crea una referencia al archivo en Firebase Storage
            val profileImageRef = storageReference.child("profile_images/$fileName")

            // Sube la imagen al almacenamiento de Firebase Storage
            profileImageRef.putFile(imageUri)
                .addOnSuccessListener {
                    // La imagen se ha subido exitosamente
                    Toast.makeText(requireContext(), "Foto de perfil subida correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Error al subir la imagen
                    Toast.makeText(requireContext(), "Error al subir la foto de perfil", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
