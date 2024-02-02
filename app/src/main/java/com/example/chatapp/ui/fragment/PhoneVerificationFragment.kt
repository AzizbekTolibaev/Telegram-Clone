package com.example.chatapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chatapp.R
import com.example.chatapp.data.ContactsData
import com.example.chatapp.databinding.FragmentPhoneVerificationBinding
import com.example.chatapp.presentation.PhoneAuthViewModel
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase

class PhoneVerificationFragment: Fragment(R.layout.fragment_phone_verification) {
    private lateinit var binding: FragmentPhoneVerificationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: PhoneAuthViewModel
    private lateinit var database: FirebaseDatabase
    private val args: PhoneVerificationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhoneVerificationBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        viewModel = PhoneAuthViewModel()
        val verificationId = args.verificationId
        val phoneNumber = args.phoneNumber
        Log.d("TTTT", "Id2: $verificationId")

        val etDigits = listOf(binding.etDigit1, binding.etDigit2, binding.etDigit3, binding.etDigit4, binding.etDigit5, binding.etDigit6)

        database = FirebaseDatabase.getInstance()
        val password = StringBuilder()

        binding.imgForward.setOnClickListener {

            if (password.length == 6) {
                binding.progressBar.visibility = View.VISIBLE
                binding.imgForward.visibility = View.GONE
                viewModel.signInWithVerificationCode(verificationId, password.toString())
                viewModel.signInSuccess.observe(requireActivity()) { signInSuccess ->
                    if (signInSuccess) {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.imgForward.visibility = View.GONE
                        val shPref = requireActivity().getSharedPreferences("shPref", Context.MODE_PRIVATE)
                        shPref.edit().putBoolean("isActive", true).apply()


                        database.getReference("users").push().setValue(
                            ContactsData(
                                auth.currentUser?.uid.toString(), "Batir", phoneNumber
                            )
                        )
                        findNavController().navigate(R.id.action_phoneVerificationFragment_to_homeFragment2)
                        findNavController().popBackStack(R.id.action_startFragment_to_phoneAuthenticationFragment2, true)
                        findNavController().popBackStack(R.id.action_phoneAuthenticationFragment_to_phoneVerificationFragment, true)
                        findNavController().popBackStack(R.id.action_phoneVerificationFragment_to_homeFragment2, true)
                    }
                }
                viewModel.error.observe(requireActivity()) { error ->
                    binding.progressBar.visibility = View.GONE
                    binding.imgForward.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Qatelik: $error", Toast.LENGTH_SHORT).show()
                }

            }
        }

        for (i in etDigits.indices) {

            etDigits[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        password.append(s)
                        if (i < etDigits.size - 1) {
                            etDigits[i + 1].requestFocus()
                        }
                    } else if (s?.length == 0 && i > 0) {
                        etDigits[i - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }
}
