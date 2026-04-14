package com.hikora.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hikora.R
import com.hikora.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        viewModel.loadUser()

        // 🔥 Observe real user data
        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvGreeting.text = "Hello, ${user.name} 👋"
            } else {
                binding.tvGreeting.text = "Hello, Hiker 👋"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}