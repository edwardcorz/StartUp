package com.example.startup.ui.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.startup.R
import com.example.startup.databinding.FragmentConfigurationBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.TextView
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

        //val aux = root.findViewById<ImageView>(R.id.imageView)
        val imageView = binding.encabezado.imageView // Reemplace 'btnOpenDialog' con el ID de su botón en el diseño
        dialogPhotoListener(imageView, root)


        var textView = root.findViewById<TextView>(R.id.nombre_banner)
        var textViewPlan=root.findViewById<TextView>(R.id.plan)


        var foto = root.findViewById<ImageView>(R.id.imageView)
        conexion.conexionNombre(textView)
        conexion.extraerPlan(textViewPlan)
        conexion.cargarFoto(requireContext(), root)

        return root
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}