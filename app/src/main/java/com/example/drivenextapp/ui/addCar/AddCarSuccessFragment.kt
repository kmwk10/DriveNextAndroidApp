package com.example.drivenextapp.ui.addCar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.google.android.material.button.MaterialButton

class AddCarSuccessFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_car_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivBack = view.findViewById<ImageView>(R.id.ivBack)
        val btnToHome = view.findViewById<MaterialButton>(R.id.btnToHome)

        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_addCarSuccessFragment_to_addCarPhotosFragment)
        }

        btnToHome.setOnClickListener {
            findNavController().navigate(R.id.action_addCarSuccessFragment_to_homepageFragment)
        }
    }
}
