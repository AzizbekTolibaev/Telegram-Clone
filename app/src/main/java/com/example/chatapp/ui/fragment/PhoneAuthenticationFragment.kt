package com.example.chatapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentPhoneAuthenticationBinding
import com.example.chatapp.presentation.PhoneAuthViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class PhoneAuthenticationFragment: Fragment(R.layout.fragment_phone_authentication) {
    private lateinit var binding: FragmentPhoneAuthenticationBinding
    private lateinit var viewModel: PhoneAuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhoneAuthenticationBinding.bind(view)

        binding.etPhoneNumber.requestFocus()
        viewModel = PhoneAuthViewModel()

        binding.imgForward.setOnClickListener {
            val phoneNumber = "+998" + binding.etPhoneNumber.text.toString()

            if (phoneNumber.length == 13) {
                binding.progressBar.visibility = View.VISIBLE
                binding.imgForward.visibility = View.GONE

                viewModel.sendVerificationCode(requireActivity(), phoneNumber)
                viewModel.verificationCodeSent.observe(requireActivity()) { codeSent ->
                    if (codeSent) {
                        val verificationId = viewModel.getVerificationId()
                        Log.d("TTTT", "Id: $verificationId")
                        findNavController().navigate(PhoneAuthenticationFragmentDirections.actionPhoneAuthenticationFragmentToPhoneVerificationFragment(verificationId, phoneNumber))
                    }
                }
                viewModel.error.observe(requireActivity()) { error ->
                    binding.progressBar.visibility = View.GONE
                    binding.imgForward.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Qatelik: $error", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "Mag'luwmat qate kiritilgen", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
