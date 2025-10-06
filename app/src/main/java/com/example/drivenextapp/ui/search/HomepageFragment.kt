package com.example.drivenextapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drivenextapp.R
import com.example.drivenextapp.util.PrefsManager
import com.google.android.material.button.MaterialButton
import androidx.navigation.fragment.findNavController

class HomepageFragment : Fragment() {

    private lateinit var prefs: PrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_homepage, container, false)
        prefs = PrefsManager(requireContext())

        val logoutButton: MaterialButton = view.findViewById(R.id.btnLogout)
        logoutButton.setOnClickListener {
            prefs.clearAccessToken()
            findNavController().navigate(R.id.action_homepageFragment_to_gettingStartedFragment)
        }

        return view
    }
}
