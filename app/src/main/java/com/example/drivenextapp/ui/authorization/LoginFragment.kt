package com.example.drivenextapp.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.drivenextapp.R
import com.example.drivenextapp.data.AuthRepository
import com.example.drivenextapp.util.PrefsManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var emailEdit: TextInputEditText
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var loginButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEdit = view.findViewById(R.id.etEmail)
        passwordEdit = view.findViewById(R.id.etPassword)
        emailLayout = view.findViewById(R.id.emailLayout)
        passwordLayout = view.findViewById(R.id.passwordLayout)
        loginButton = view.findViewById(R.id.btnLogin)

        loginButton.setOnClickListener {
            validateAndLogin()
        }

        view.findViewById<View>(R.id.tvRegister).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment1)
        }
    }

    private fun validateAndLogin() {
        val email = emailEdit.text?.toString().orEmpty()
        val password = passwordEdit.text?.toString().orEmpty()

        var valid = true

        if (email.isEmpty()) {
            emailLayout.error = "Это поле является обязательным"
            valid = false
        } else {
            emailLayout.error = null
        }

        if (password.isEmpty()) {
            passwordLayout.error = "Это поле является обязательным"
            valid = false
        } else {
            passwordLayout.error = null
        }

        if (!valid) return

        if (AuthRepository.checkCredentials(email, password)) {
            // Сохраняем тестовый токен для проверок между запусками
            val prefs = PrefsManager(requireContext())
            val testToken = "test_token_for_$email" // <- временный токен
            prefs.saveAccessToken(testToken)

            // Навигация на главный экран и очистка back stack
            val options = navOptions {
                popUpTo(R.id.loginFragment) {
                    inclusive = true
                }
            }
            findNavController().navigate(R.id.action_loginFragment_to_homepageFragment, null, options)
        } else {
            passwordLayout.error = "Неверные данные"
        }
    }
}
