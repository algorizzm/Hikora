package com.hikora.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hikora.R
import com.hikora.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProfileBinding.bind(view)

        viewModel.loadUser()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvEmail.text = it.email
                binding.etName.setText(it.name)
            }
        }

        binding.btnSave.setOnClickListener {
            val newName = binding.etName.text.toString().trim()

            if (newName.isEmpty()) {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.updateName(newName)
        }

        viewModel.updateStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut() // ✅ clear session

            findNavController().navigate(
                R.id.loginFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.profileFragment, true) // clear backstack
                    .build()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}