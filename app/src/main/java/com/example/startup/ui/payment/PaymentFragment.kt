package com.example.startup.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.startup.R
import com.example.startup.conexionBD
import com.example.startup.databinding.FragmentPaymentBinding
import com.example.startup.ui.configuration.Listeners
import com.stripe.android.PaymentConfiguration


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

        PaymentConfiguration.init(requireContext(),
            "pk_test_51NOmWOKW6CpV4SSu2Quztenqyh9ibxikHNvMF9HZVF47Zcn8zNMuj8NGIT2dI4nV13CPuGN1HXU24ekJSiERGr3u00Nq70nAKb"
        )

        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var textView=root.findViewById<TextView>(R.id.nombre_banner)
        var textViewPlan=root.findViewById<TextView>(R.id.plan)


        conexion.conexionNombre(textView)
        conexion.extraerPlan(textViewPlan)
        conexion.cargarFoto(requireContext(),root)



        var buttonPagar = root.findViewById<Button>(R.id.button_pagar)
        dialogPayListener(buttonPagar, root)

        var buttonSemanal = root.findViewById<Button>(R.id.button_semanal)
        dialogPayListener(buttonSemanal, root)

        var buttonPersonalizado = root.findViewById<Button>(R.id.button_personalizado)
        dialogPayListener(buttonPersonalizado, root)

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}