package com.hikora.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hikora.R
import com.hikora.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setupWithNavController(navController)

            // 🔐 GLOBAL AUTH OBSERVER (FIXED VERSION)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.authState.collect { user ->

                    when {
                        user == null -> {
                            navController.navigate(
                                R.id.loginFragment,
                                null,
                                navOptions()
                            )
                        }

                        // 🔥 If user exists but cache NOT ready → fetch from Firestore
                        !viewModel.isUserDataReady() -> {

                            viewModel.syncUser {

                                navController.navigate(
                                    R.id.homeFragment,
                                    null,
                                    navOptions()
                                )
                            }
                        }

                        else -> {
                            navController.navigate(
                                R.id.homeFragment,
                                null,
                                navOptions()
                            )
                        }
                    }
                }
            }
        }

        // 🔐 Hide navbar on auth screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.loginFragment,
                R.id.signupFragment,
                R.id.splashFragment -> {
                    bottomNav.visibility = View.GONE
                }

                else -> {
                    bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }

    // ✅ Clean NavOptions helper
    private fun navOptions() = androidx.navigation.NavOptions.Builder()
        .setPopUpTo(R.id.splashFragment, true)
        .setLaunchSingleTop(true)
        .build()
}