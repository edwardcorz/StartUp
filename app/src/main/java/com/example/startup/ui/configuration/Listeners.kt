package com.example.startup.ui.configuration

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.startup.LoginActivity
import com.example.startup.MainActivity
import com.example.startup.R
import com.example.startup.conexionBD
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

open class Listeners : Fragment() {
    private val conexion = conexionBD()
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
    var ButtonSelected :Int? = null
    fun dialogPayListener(button_pagar: Button, root : View) {

        button_pagar.setOnClickListener {
            ButtonSelected = button_pagar.id
            val inflater  = LayoutInflater.from(requireActivity())
            val dialogView = inflater.inflate(R.layout.dialog_payment, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogView)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()


            validarCheckBox(dialogView)
            pagarListener(dialogView, myDialog)

        }
    }

    fun validarCheckBox(dialogView: View){
        val creditoCheck = dialogView.findViewById<CheckBox>(R.id.checkCredito)
        val debitoCheck = dialogView.findViewById<CheckBox>(R.id.checkDebito)

        creditoCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                debitoCheck.isChecked = false
            }
        }

        debitoCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                creditoCheck.isChecked = false
            }
        }
    }

    fun pagarListener(root: View, myDialog : Dialog, ){
        val pagar = root.findViewById<Button>(R.id.pagar)
        pagar.setOnClickListener{
            validarCampos(root,myDialog)
        }
    }
    fun validarTipoTarjeta(dialogView: View, myDialog: Dialog){
        val creditoCheck = dialogView.findViewById<CheckBox>(R.id.checkCredito)
        val debitoCheck = dialogView.findViewById<CheckBox>(R.id.checkDebito)
        if (!creditoCheck.isChecked and !debitoCheck.isChecked){
            Toast.makeText(context, "Seleccione al menos un tipo de tarjeta", Toast.LENGTH_SHORT).show()
        }
        else{
            if (creditoCheck.isChecked) {
                val credito = "credito"
                cargarBD(credito,dialogView)
                myDialog.dismiss()

            }
            else{
                val credito = "debito"
                cargarBD(credito,dialogView)
                myDialog.dismiss()
            }
        }


    }

    fun cargarBD(tipo:String,dialogView: View) {
        val nombreTarjeta =
            dialogView.findViewById<EditText>(R.id.name_person).text.toString()
        val numeroTarjeta =
            dialogView.findViewById<EditText>(R.id.card_number).text.toString()
        val mesTarjeta = dialogView.findViewById<EditText>(R.id.mes).text.toString()
        val añoTarjeta = dialogView.findViewById<EditText>(R.id.year).text.toString()
        val cvvNumber = dialogView.findViewById<EditText>(R.id.cvv).text.toString()
        val correo = dialogView.findViewById<EditText>(R.id.correo).text.toString()
        var plan = ""
        Log.i("TAG", "plan $ButtonSelected")
        if (ButtonSelected == 2131361906){
            Log.i("TAG", "basico $ButtonSelected")

            plan = "Plan básico"
        }
        if (ButtonSelected == 2131361909){
            Log.i("TAG", "Semanal $ButtonSelected")

            plan = "Plan Semanal"
        }
        if (ButtonSelected == 2131361907){
            Log.i("TAG", "Personalizado $ButtonSelected")

            plan = "Plan Personalizado"
        }

        conexion.guardarPagos(requireContext(),
            nombreTarjeta,
            numeroTarjeta,
            mesTarjeta,
            añoTarjeta,
            cvvNumber,
            tipo,
            correo,
            plan
        )
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

                    conexion.conexionNombre(root.findViewById(R.id.nombre_banner))
                    // El nombre se actualizó correctamente
                }
                .addOnFailureListener { exception ->
                    // Manejar el error al actualizar el nombre
                }
        }

    }

    fun dialogPhotoListener(imageView: ImageView, root: View) {
        imageView.setOnClickListener {
            val inflater  = LayoutInflater.from(requireActivity())
            val dialogView = inflater.inflate(R.layout.dialog_photo, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogView)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            conexion.cargarFoto(requireContext(), dialogView)
            myDialog.show()
            editarButtom(dialogView, myDialog,root)
            volverListener(dialogView, myDialog)
        }
    }
    fun editarButtom(dialogView : View, myDialog: Dialog, root: View){
        val subir = dialogView.findViewById<Button>(R.id.editar)
        subir.setOnClickListener{
            myDialog.dismiss()
            editarListener.launch("image/*")
        }
    }

    private val editarListener = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            // Aquí puedes subir la imagen a Firebase Storage y asociarla al usuario
            subirFotoPerfil(imageUri)
        }
    }

    fun subirFotoPerfil(imageUri: Uri){
        val storageRef = FirebaseStorage.getInstance().reference
        val profileImagesRef = storageRef.child("profile_images")

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val uid = currentUser?.uid

        val imageRef = profileImagesRef.child("$uid.jpg") // uid es el identificador del usuario

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // La imagen se ha subido exitosamente
                // Puedes obtener la URL de descarga de la imagen y asociarla al usuario
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    // Aquí puedes guardar la URL de descarga en la base de datos junto con los detalles del usuario
                    guardarURLFotoPerfil(downloadUrl)


                }
            }
            .addOnFailureListener { exception ->
                // Ocurrió un error al subir la imagen
                // Puedes manejar el error según tus necesidades
            }
    }

    fun guardarURLFotoPerfil(downloadUrl: String){
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            val usuarioRef = db.collection("usuarios").document(uid)

            usuarioRef.update("fotoPerfilUrl", downloadUrl)
                .addOnSuccessListener {
                    // La URL de la foto de perfil se guardó exitosamente en Cloud Firestore
                }
                .addOnFailureListener { e ->
                    // Ocurrió un error al guardar la URL de la foto de perfil
                }
        }
    }

    fun validarCampos(dialogView: View,myDialog: Dialog){
        val nombreTarjeta =
            dialogView.findViewById<EditText>(R.id.name_person).text
        val numeroTarjeta =
            dialogView.findViewById<EditText>(R.id.card_number).text
        val mesTarjeta = dialogView.findViewById<EditText>(R.id.mes).text
        val añoTarjeta = dialogView.findViewById<EditText>(R.id.year).text
        val cvvNumber = dialogView.findViewById<EditText>(R.id.cvv).text
        val correo = dialogView.findViewById<EditText>(R.id.correo).text

        val pagar = dialogView.findViewById<Button>(R.id.pagar)
        pagar.setOnClickListener{
            if (nombreTarjeta.isEmpty() || numeroTarjeta.isEmpty() || mesTarjeta.isEmpty()
                || añoTarjeta.isEmpty() || cvvNumber.isEmpty() || correo.isEmpty()){

                Toast.makeText(requireContext(), "LLena todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                validarTipoTarjeta(dialogView, myDialog)
            }
        }
    }

    fun agendarClase(contexto:Context,buttonAgendar: Button, fecha:String){

        buttonAgendar.setOnClickListener{
            conexion.verificarFechaAgendada(contexto, fecha) { agendada ->
                if (agendada) {
                    conexion.eliminarFechaAgendada(contexto, fecha)
                    //conexion.cambiarBoton(contexto,fecha, buttonAgendar)
                } else {
                    conexion.guardarClase(contexto, fecha)

                }
            }


            //if  (conexion.verificarFechaAgendada(contexto, fecha, buttonAgendar)){
            //conexion.eliminarFechaAgendada(contexto, fecha)
                //conexion.eliminarFechaAgendada(contexto, fecha)
                /*
                Log.i("TAG", "Clase agendad para el dia $fecha")
                conexion.guardarClase(requireContext(),fecha)
                //conexion.onButtonClick(requireContext(),fecha)
                buttonAgendar.setText("Cancelar")
                buttonAgendar.setBackgroundColor(Color.RED)
                */

            //}


            //conexion.eliminarFechaAgendada(requireContext(),fecha)
            conexion.cambiarBoton(contexto,fecha, buttonAgendar)

        }
    }
}
