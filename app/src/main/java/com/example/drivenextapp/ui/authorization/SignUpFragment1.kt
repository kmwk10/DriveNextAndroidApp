package com.example.drivenextapp.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.checkbox.MaterialCheckBox
import com.example.drivenextapp.data.RegisterRepository

class SignUpFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailLayout = view.findViewById<TextInputLayout>(R.id.emailLayout)
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.passwordLayout)
        val confirmPasswordLayout = view.findViewById<TextInputLayout>(R.id.confirmPasswordLayout)

        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val etConfirmPassword = view.findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val chkAgree = view.findViewById<MaterialCheckBox>(R.id.chkAgree)
        val btnNext = view.findViewById<MaterialButton>(R.id.btnNext)

        // Восстанавливаем данные из RegisterRepository (чтобы не терялись при навигации)
        etEmail.setText(RegisterRepository.currentData.email)
        etPassword.setText(RegisterRepository.currentData.password)
        etConfirmPassword.setText("")
        chkAgree.isChecked = false

        btnNext.setOnClickListener {
            // Получаем значения
            val email = etEmail.text?.toString()?.trim() ?: ""
            val password = etPassword.text?.toString() ?: ""
            val confirmPassword = etConfirmPassword.text?.toString() ?: ""
            val isChecked = chkAgree.isChecked
            val chkError = view.findViewById<TextView>(R.id.chkError)

            // Сброс ошибок
            emailLayout.error = null
            passwordLayout.error = null
            confirmPasswordLayout.error = null

            // Сохраняем во временное хранилище
            RegisterRepository.saveEmail(email)
            RegisterRepository.savePassword(password)

            // Валидация шага 1
            val errors = RegisterRepository.validateStep1(confirmPassword = confirmPassword, acceptedTerms = isChecked)

            if (errors.isEmpty()) {
                findNavController().navigate(R.id.action_signUpFragment1_to_signUpFragment2)
            } else {
                errors["email"]?.let { emailLayout.error = it }
                errors["password"]?.let { passwordLayout.error = it }
                errors["repeat"]?.let { confirmPasswordLayout.error = it }
                if (errors["terms"] != null) {
                    chkError.text = errors["terms"]
                    chkError.visibility = View.VISIBLE
                } else {
                    chkError.text = ""
                    chkError.visibility = View.GONE
                }
            }
        }

        view.findViewById<android.widget.ImageView>(R.id.ivBack).setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment1_to_loginFragment)
        }
    }
}
