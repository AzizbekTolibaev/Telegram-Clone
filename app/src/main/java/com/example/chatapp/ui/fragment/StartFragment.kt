package com.example.chatapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private lateinit var binding: FragmentStartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)

        binding.btnStart.setOnClickListener{
            findNavController().navigate(R.id.action_startFragment_to_phoneAuthenticationFragment2)
        }
    }
}