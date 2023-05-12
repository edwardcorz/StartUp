package com.example.startup.ui.configuration

import android.app.Dialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.startup.R
import com.example.startup.databinding.FragmentConfigurationBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.startup.LoginActivity
import com.example.startup.conexionBD

// Importar las bibliotecas necesarias para cambiar foto de perfil

class   ConfigurationFragment : Listeners(){
    private val conexion = conexionBD()
    private var _binding: FragmentConfigurationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val btn = binding.btn
        dialog1Listener(btn)

        val btnx = binding.btnx
        dialog2Listener(btnx)

        val editBtn = binding.guardarNombre
        dialog3Listener(editBtn, root)


        val imageView = binding.imageView // Reemplace 'btnOpenDialog' con el ID de su botón en el diseño
        imageView.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.dialog_photo, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
        }

        var textView = root.findViewById<TextView>(R.id.nombreConfiguration)

        conexion.conexionNombre(textView)
        return root
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}