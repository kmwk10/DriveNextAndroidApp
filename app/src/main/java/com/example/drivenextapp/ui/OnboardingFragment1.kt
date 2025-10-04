package com.example.drivenextapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drivenextapp.R
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton

class OnboardingFragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.btnNext)
            .setOnClickListener {
                findNavController().navigate(R.id.action_onboardingFragment1_to_onboardingFragment2)
            }
    }
}
