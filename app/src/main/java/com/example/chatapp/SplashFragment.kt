package com.example.chatapp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlin.random.Random

class SplashFragment: Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val random = Random.nextInt(1000,2000)
        val shPref = requireActivity().getSharedPreferences("shPref", Context.MODE_PRIVATE)
        val isActive = shPref.getBoolean("isActive", false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isActive) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment2)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_startFragment)
            }
        }, random.toLong())
    }
}
