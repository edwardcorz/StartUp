package com.example.startup.ui.configuration

import android.app.Dialog
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


class ConfigurationFragment : Fragment() {

    private var _binding: FragmentConfigurationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val configurationViewModel =
            ViewModelProvider(this)[ConfigurationViewModel::class.java]

        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val btn = binding.btn // Reemplace 'btnOpenDialog' con el ID de su botón en el diseño
        btn.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.dialog1, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
        }

        return root
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}