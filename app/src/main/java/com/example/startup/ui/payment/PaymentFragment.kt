package com.example.startup.ui.payment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.startup.R
import com.example.startup.conexionBD
import com.example.startup.databinding.FragmentHomeBinding
import com.example.startup.databinding.FragmentPaymentBinding
import com.example.startup.ui.home.HomeViewModel

class PaymentFragment : Fragment() {

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
        val paymentViewModel =
            ViewModelProvider(this)[PaymentViewModel::class.java]

        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var textView=root.findViewById<TextView>(R.id.nombrePayment)

        conexion.conexionNombre(textView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}