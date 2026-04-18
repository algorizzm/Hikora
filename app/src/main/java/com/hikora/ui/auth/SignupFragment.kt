package com.hikora.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hikora.R
import androidx.navigation.fragment.findNavController

class SignupFragment : Fragment(R.layout.fragment_signup) {

    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etConfirm = view.findViewById<EditText>(R.id.etConfirmPassword)
        val etName = view.findViewById<EditText>(R.id.etName)
        val cbTerms = view.findViewById<CheckBox>(R.id.cbTerms)

        val btnSignup = view.findViewById<Button>(R.id.btnSignup)
        val tvError = view.findViewById<TextView>(R.id.tvError)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackLogin)
        val tvLoginRedirect = view.findViewById<TextView>(R.id.tvLoginRedirect)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        btnSignup.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirm = etConfirm.text.toString().trim()
            val name = etName.text.toString().trim()

            // Validation
            if (!cbTerms.isChecked) {
                tvError.text = "You must accept the terms"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                tvError.text = "Please fill in all fields"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvError.text = "Invalid email format"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (password.length < 8) {
                tvError.text = "Password must be at least 8 characters"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (password != confirm) {
                tvError.text = "Passwords do not match"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            tvError.visibility = View.GONE

            // ✅ AUTH ONLY (NO NAVIGATION)
            viewModel.signup(name, email, password) { success, error ->

                if (!success) {
                    tvError.text = error ?: "Signup failed"
                    tvError.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Signup successful", Toast.LENGTH_SHORT).show()
                    // Navigation handled by AuthState observer
                }
            }
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        tvLoginRedirect.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }
}