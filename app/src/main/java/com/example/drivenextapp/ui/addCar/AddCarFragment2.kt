package com.example.drivenextapp.ui.addCar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddCarFragment2 : Fragment() {

    private val vm: AddCarViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_car_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivBack = view.findViewById<ImageView>(R.id.ivBack)
        val etYear = view.findViewById<TextInputEditText>(R.id.etYear)
        val etBrand = view.findViewById<TextInputEditText>(R.id.etBrand)
        val etModel = view.findViewById<TextInputEditText>(R.id.etModel)
        val actTransmission = view.findViewById<AutoCompleteTextView>(R.id.actTransmission)
        val etMileage = view.findViewById<TextInputEditText>(R.id.etMileage)
        val etDescription = view.findViewById<TextInputEditText>(R.id.etDescription)
        val btnSubmit = view.findViewById<MaterialButton>(R.id.btnSubmit)

        btnSubmit.isEnabled = false

        // Настройка AutoCompleteTextView
        val transmissionOptions = listOf("A/T", "M/T")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            transmissionOptions
        )
        actTransmission.setAdapter(adapter)

        actTransmission.setOnClickListener {
            actTransmission.showDropDown()
        }

        // Восстанавливаем данные из ViewModel
        vm.year?.let { etYear.setText(it) }
        vm.brand?.let { etBrand.setText(it) }
        vm.model?.let { etModel.setText(it) }
        vm.transmission?.let { actTransmission.setText(it, false) }
        vm.mileage?.let { etMileage.setText(it) }
        vm.description?.let { etDescription.setText(it) }

        btnSubmit.isEnabled = areAllFieldsFilled(etYear, etBrand, etModel, actTransmission, etMileage)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnSubmit.isEnabled = areAllFieldsFilled(
                    etYear, etBrand, etModel, actTransmission, etMileage
                )
                vm.year = etYear.text?.toString()
                vm.brand = etBrand.text?.toString()
                vm.model = etModel.text?.toString()
                vm.mileage = etMileage.text?.toString()
                vm.description = etDescription.text?.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etYear.addTextChangedListener(watcher)
        etBrand.addTextChangedListener(watcher)
        etModel.addTextChangedListener(watcher)
        etMileage.addTextChangedListener(watcher)
        etDescription.addTextChangedListener(watcher)

        actTransmission.setOnItemClickListener { _, _, position, _ ->
            vm.transmission = transmissionOptions[position]
            btnSubmit.isEnabled = areAllFieldsFilled(
                etYear, etBrand, etModel, actTransmission, etMileage
            )
        }

        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_addCarFragment2_to_addCarFragment1)
        }

        btnSubmit.setOnClickListener {
            if (btnSubmit.isEnabled) {
                findNavController().navigate(R.id.action_addCarFragment2_to_addCarPhotosFragment)
            }
        }
    }

    private fun areAllFieldsFilled(
        etYear: TextInputEditText,
        etBrand: TextInputEditText,
        etModel: TextInputEditText,
        actTransmission: AutoCompleteTextView,
        etMileage: TextInputEditText
    ): Boolean {
        return !etYear.text.isNullOrBlank() &&
                !etBrand.text.isNullOrBlank() &&
                !etModel.text.isNullOrBlank() &&
                !actTransmission.text.isNullOrBlank() &&
                !etMileage.text.isNullOrBlank()
    }
}
