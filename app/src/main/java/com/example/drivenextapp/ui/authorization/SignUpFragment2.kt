package com.example.drivenextapp.ui.authorization

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.example.drivenextapp.data.Gender
import com.example.drivenextapp.data.RegisterRepository
import com.example.drivenextapp.util.getTextOrEmpty
import com.example.drivenextapp.util.showError
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class SignUpFragment2 : Fragment() {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastNameLayout = view.findViewById<TextInputLayout>(R.id.lastNameLayout)
        val firstNameLayout = view.findViewById<TextInputLayout>(R.id.firstNameLayout)
        val middleNameLayout = view.findViewById<TextInputLayout>(R.id.middleNameLayout)
        val etLastName = view.findViewById<TextInputEditText>(R.id.etLastName)
        val etFirstName = view.findViewById<TextInputEditText>(R.id.etFirstName)
        val etMiddleName = view.findViewById<TextInputEditText>(R.id.etMiddleName)
        val etBirthDate = view.findViewById<TextInputEditText>(R.id.etBirthDate)
        val birthDateLayout = view.findViewById<TextInputLayout>(R.id.birthDateLayout)
        val rgGender = view.findViewById<RadioGroup>(R.id.rgGender)
        val genderError = view.findViewById<TextView>(R.id.genderError)
        val btnNext = view.findViewById<MaterialButton>(R.id.btnNext)
        val ivBack = view.findViewById<ImageView>(R.id.ivBack)

        // Восстанавливаем данные из репозитория
        etLastName.setText(RegisterRepository.currentData.surname)
        etFirstName.setText(RegisterRepository.currentData.name)
        etMiddleName.setText(RegisterRepository.currentData.patronymic)
        RegisterRepository.currentData.birthDate?.let {
            etBirthDate.setText(dateFormat.format(it))
            selectedDate = it
        }

        when (RegisterRepository.currentData.gender) {
            Gender.MALE -> rgGender.check(R.id.rbMale)
            Gender.FEMALE -> rgGender.check(R.id.rbFemale)
            else -> rgGender.clearCheck()
        }

        // Обработчик выбора даты
        etBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selected = Calendar.getInstance()
                    selected.set(year, month, dayOfMonth)
                    selectedDate = selected.time
                    etBirthDate.setText(dateFormat.format(selectedDate!!))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Сбрасываем ошибку пола при выборе
        rgGender.setOnCheckedChangeListener { _, _ ->
            genderError.visibility = View.GONE
            genderError.text = ""
        }

        btnNext.setOnClickListener {
            // Сохраняем данные в репозиторий
            RegisterRepository.saveSurname(etLastName.getTextOrEmpty().trim())
            RegisterRepository.saveName(etFirstName.getTextOrEmpty().trim())
            RegisterRepository.savePatronymic(etMiddleName.getTextOrEmpty().trim())
            selectedDate?.let { RegisterRepository.saveBirthDate(it) }
            val gender = when (rgGender.checkedRadioButtonId) {
                R.id.rbMale -> Gender.MALE
                R.id.rbFemale -> Gender.FEMALE
                else -> null
            }
            gender?.let { RegisterRepository.saveGender(it) }

            // Валидация через репозиторий
            val errors = RegisterRepository.validateStep2()

            // Сбрасываем ошибки
            lastNameLayout.error = null
            firstNameLayout.error = null
            middleNameLayout.error = null
            birthDateLayout.error = null
            genderError.visibility = View.GONE
            genderError.text = ""

            // Отображаем ошибки
            errors["surname"]?.let { lastNameLayout.error = it }
            errors["name"]?.let { firstNameLayout.error = it }
            errors["patronymic"]?.let { middleNameLayout.error = it }
            errors["birthDate"]?.let { birthDateLayout.showError(it) }
            errors["gender"]?.let {
                genderError.text = it
                genderError.visibility = View.VISIBLE
            }

            if (errors.isEmpty()) {
                findNavController().navigate(R.id.action_signUpFragment2_to_signUpFragment3)
            }
        }

        // Назад
        ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment2_to_signUpFragment1)
        }
    }
}
