package com.hikora.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hikora.R

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser

        if (user != null) {
            // user is logged in → go to profile
            findNavController().navigate(R.id.homeFragment)
        } else {
            // not logged in → go to login
            findNavController().navigate(R.id.loginFragment)
        }
    }
}