package com.hikora.ui.reels

import com.hikora.R
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
class ReelsFragment : Fragment(R.layout.fragment_reels) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val desc = view.findViewById<TextView>(R.id.tvDescription)

        title.text = "Reels"
        desc.text = "Watch hiking reels and short clips"
    }
}