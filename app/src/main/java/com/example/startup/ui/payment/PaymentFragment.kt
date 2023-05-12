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
import com.stripe.android.Stripe


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

        val stripe = Stripe(requireContext(), "pk_test_51N4ntfFMwr78vVxVjQ0xXvsz1tDzqAsWlT3TpxRdkhBq1zL7ryrh767kO9xbqKecVLqd8fOn7zRiAaRL6fkmciWV00R5Wh852w")

        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var textView=root.findViewById<TextView>(R.id.nombrePayment)

        conexion.conexionNombre(textView)

        return root

        var buttonPagar = root.findViewById<Button>(R.id.button_pagar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}