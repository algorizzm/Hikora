package com.hikora.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hikora.R

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🎬 Pure splash screen only
        // No logic, no navigation, no ViewModel
        // Navigation is handled globally by MainActivity
    }
}