package com.hikora.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hikora.R

class SignupFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etConfirm = view.findViewById<EditText>(R.id.etConfirmPassword)
        val btnSignup = view.findViewById<Button>(R.id.btnSignup)
        val tvError = view.findViewById<TextView>(R.id.tvError)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackLogin)
        val etName = view.findViewById<EditText>(R.id.etName)
        val cbTerms = view.findViewById<CheckBox>(R.id.cbTerms)
        val tvLoginRedirect = view.findViewById<TextView>(R.id.tvLoginRedirect)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        btnSignup.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirm = etConfirm.text.toString().trim()
            val name = etName.text.toString().trim()

            // 🔴 Validation
            if (!cbTerms.isChecked) {
                tvError.text = "You must accept the terms"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                tvError.text = "Please fill in all fields"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                tvError.text = "All fields are required"
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

            viewModel.signup(name, email, password)
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        tvLoginRedirect.setOnClickListener {
            findNavController().navigate(
                R.id.loginFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.signupFragment, true)
                    .build()
            )
        }

        viewModel.authState.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Signup Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            tvError.text = it
            tvError.visibility = View.VISIBLE
        }
    }
}