package com.example.startup.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.startup.R
import com.example.startup.conexionBD
import com.example.startup.databinding.FragmentPaymentBinding
import com.example.startup.ui.configuration.Listeners


class PaymentFragment : Listeners() {

    private val conexion = conexionBD()
    private var _binding: FragmentPaymentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView=root.findViewById<TextView>(R.id.nombrePayment)

        conexion.conexionNombre(textView)



        var buttonPagar = root.findViewById<Button>(R.id.button_pagar)
        dialogPayListener(buttonPagar, root)

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}