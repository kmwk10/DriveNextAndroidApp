package com.example.drivenextapp.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.google.android.material.button.MaterialButton

class GettingStartedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_getting_started, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.btnLogin)
            .setOnClickListener {
                findNavController().navigate(R.id.action_gettingStartedFragment_to_loginFragment)
            }

        view.findViewById<MaterialButton>(R.id.btnRegister)
            .setOnClickListener {
                findNavController().navigate(R.id.action_gettingStartedFragment_to_signUpFragment1)
            }
    }
}