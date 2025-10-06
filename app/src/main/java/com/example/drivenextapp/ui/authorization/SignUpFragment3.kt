package com.example.drivenextapp.ui.authorization

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.example.drivenextapp.data.RegisterRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import androidx.activity.result.contract.ActivityResultContracts

class SignUpFragment3 : Fragment() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private var selectedLicenseDate: Date? = null

    private lateinit var ivProfile: ImageView
    private lateinit var ivLicense: ImageView
    private lateinit var ivPassport: ImageView

    private lateinit var etLicense: TextInputEditText
    private lateinit var etLicenseDate: TextInputEditText
    private lateinit var licenseLayout: TextInputLayout
    private lateinit var licenseDateLayout: TextInputLayout

    private var currentPicker: Picker = Picker.PROFILE

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                when (currentPicker) {
                    Picker.PROFILE -> {
                        ivProfile.setImageURI(it)
                        RegisterRepository.saveProfilePhoto(it)
                    }
                    Picker.LICENSE -> {
                        ivLicense.setImageURI(it)
                        RegisterRepository.saveDriverLicensePhoto(it)
                    }
                    Picker.PASSPORT -> {
                        ivPassport.setImageURI(it)
                        RegisterRepository.savePassportPhoto(it)
                    }
                }
            }
        }

    private enum class Picker { PROFILE, LICENSE, PASSPORT }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivProfile = view.findViewById(R.id.ivProfilePhoto)
        ivLicense = view.findViewById(R.id.btnUploadLicense)
        ivPassport = view.findViewById(R.id.btnUploadPassport)

        etLicense = view.findViewById(R.id.etLicense)
        etLicenseDate = view.findViewById(R.id.etLicenseDate)
        licenseLayout = view.findViewById(R.id.licenseLayout)
        licenseDateLayout = view.findViewById(R.id.licenseDateLayout)

        val btnNext = view.findViewById<MaterialButton>(R.id.btnNext)
        val ivBack = view.findViewById<ImageView>(R.id.ivBack)

        // Восстанавливаем данные
        etLicense.setText(RegisterRepository.currentData.driverLicenseNumber)
        RegisterRepository.currentData.driverLicenseIssueDate?.let {
            selectedLicenseDate = it
            etLicenseDate.setText(dateFormat.format(it))
        }
        RegisterRepository.currentData.profilePhoto?.let { ivProfile.setImageURI(it) }
        RegisterRepository.currentData.driverLicensePhoto?.let { ivLicense.setImageURI(it) }
        RegisterRepository.currentData.passportPhoto?.let { ivPassport.setImageURI(it) }

        // Кликабельные кнопки для фото
        ivProfile.setOnClickListener { pickImage(Picker.PROFILE) }
        ivLicense.setOnClickListener { pickImage(Picker.LICENSE) }
        ivPassport.setOnClickListener { pickImage(Picker.PASSPORT) }

        // Выбор даты
        etLicenseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selected = Calendar.getInstance()
                    selected.set(year, month, dayOfMonth)
                    selectedLicenseDate = selected.time
                    etLicenseDate.setText(dateFormat.format(selectedLicenseDate!!))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnNext.setOnClickListener {
            // Сохраняем данные
            RegisterRepository.saveDriverLicenseNumber(etLicense.text?.toString()?.trim() ?: "")
            selectedLicenseDate?.let { RegisterRepository.saveDriverLicenseIssueDate(it) }

            val errors = RegisterRepository.validateStep3()
            var hasErrors = false

            // Текстовые поля
            if (errors.containsKey("licenseNumber")) {
                licenseLayout.error = errors["licenseNumber"]
                hasErrors = true
            } else licenseLayout.error = null

            if (errors.containsKey("licenseDate")) {
                licenseDateLayout.error = errors["licenseDate"]
                hasErrors = true
            } else licenseDateLayout.error = null

            if (errors.containsKey("licensePhoto")) hasErrors = true
            if (errors.containsKey("passportPhoto")) hasErrors = true

            if (!hasErrors) {
                findNavController().navigate(R.id.action_signUpFragment3_to_signUpSuccessFragment)
            }
        }

        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment3_to_signUpFragment2)
        }
    }
    private fun pickImage(picker: Picker) {
        currentPicker = picker
        pickImageLauncher.launch("image/*")
    }
}
