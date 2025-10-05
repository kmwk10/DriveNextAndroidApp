package com.example.drivenextapp.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.google.android.material.button.MaterialButton

class SignUpFragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.btnNext)
            .setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment1_to_signUpFragment2)
            }

        view.findViewById<ImageView>(R.id.ivBack)
            .setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment1_to_loginFragment)
            }
    }
}
