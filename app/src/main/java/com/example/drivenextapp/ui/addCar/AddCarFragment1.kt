package com.example.drivenextapp.ui.addCar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddCarFragment1 : Fragment() {

    private val vm: AddCarViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_car_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivBack = view.findViewById<ImageView>(R.id.ivBack)
        val etAddress = view.findViewById<TextInputEditText>(R.id.etAddress)
        val btnNext = view.findViewById<MaterialButton>(R.id.btnNext)

        // Восстанавливаем сохранённое значение
        vm.address?.let {
            etAddress.setText(it)
            btnNext.isEnabled = it.isNotBlank()
        }

        // Слушатель текста
        etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnNext.isEnabled = !s.isNullOrBlank()
                vm.address = s?.toString()   // Сохраняем значение
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_addCarFragment1_to_becomeHostFragment)
        }

        btnNext.setOnClickListener {
            if (btnNext.isEnabled) {
                findNavController().navigate(R.id.action_addCarFragment1_to_addCarFragment2)
            }
        }
    }
}

