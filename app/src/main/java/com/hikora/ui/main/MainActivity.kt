package com.hikora.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hikora.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Connect BottomNav
        bottomNav.setupWithNavController(navController)

        // 🔐 Hide navbar on auth screens ONLY
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                // ❌ Hide navbar here
                R.id.loginFragment,
                R.id.signupFragment,
                R.id.splashFragment -> {
                    bottomNav.visibility = View.GONE
                }

                // ✅ Show everywhere else
                else -> {
                    bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }
}