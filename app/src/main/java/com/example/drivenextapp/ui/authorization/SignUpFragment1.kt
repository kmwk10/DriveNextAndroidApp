package com.example.drivenextapp.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.checkbox.MaterialCheckBox

class SignUpFragment1 : Fragment() {

    private var emailText: String? = null
    private var passwordText: String? = null
    private var confirmPasswordText: String? = null
    private var isAgreeChecked: Boolean = false

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

        // Восстанавливаем данные, если они есть
        etEmail.setText(emailText)
        etPassword.setText(passwordText)
        etConfirmPassword.setText(confirmPasswordText)
        chkAgree.isChecked = isAgreeChecked

        btnNext.setOnClickListener {
            var hasError = false

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val isChecked = chkAgree.isChecked

            // Сброс ошибок
            emailLayout.error = null
            passwordLayout.error = null
            confirmPasswordLayout.error = null

            // Проверка email
            if (email.isEmpty()) {
                emailLayout.error = "Это поле является обязательным"
                hasError = true
            } else if (!isEmailValid(email)) {
                emailLayout.error = "Введите корректный email"
                hasError = true
            }

            // Проверка пароля
            if (password.isEmpty()) {
                passwordLayout.error = "Это поле является обязательным"
                hasError = true
            }

            // Проверка повторного пароля
            if (confirmPassword.isEmpty()) {
                confirmPasswordLayout.error = "Это поле является обязательным"
                hasError = true
            } else if (password != confirmPassword) {
                confirmPasswordLayout.error = "Пароли не совпадают"
                hasError = true
            }

            // Проверка соглашения
            if (!isChecked) {
                chkAgree.error = "Необходимо согласие"
                hasError = true
            } else {
                chkAgree.error = null
            }

            if (!hasError) {
                // Сохраняем данные для следующего фрагмента
                emailText = email
                passwordText = password
                confirmPasswordText = confirmPassword
                isAgreeChecked = isChecked

                findNavController().navigate(R.id.action_signUpFragment1_to_signUpFragment2)
            }
        }

        view.findViewById<android.widget.ImageView>(R.id.ivBack).setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment1_to_loginFragment)
        }
    }

    // Проверка email
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
