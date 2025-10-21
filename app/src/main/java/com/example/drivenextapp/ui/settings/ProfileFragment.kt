package com.example.drivenextapp.ui.settings

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.example.drivenextapp.data.AuthRepository
import com.example.drivenextapp.data.RegisterRepository
import com.example.drivenextapp.data.RegisterData

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var avatarIv: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvGoogleAccount: TextView
    private lateinit var itemLogout: LinearLayout
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { newUri ->
                RegisterRepository.saveProfilePhoto(newUri)

                AuthRepository.getCurrentUser()?.profilePhoto = newUri

                if (this::avatarIv.isInitialized) {
                    avatarIv.setImageURI(newUri)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        avatarIv = view.findViewById(R.id.imgAvatar)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvGender = view.findViewById(R.id.tvGender)
        tvGoogleAccount = view.findViewById(R.id.tvGoogleAccount)
        itemLogout = view.findViewById(R.id.itemLogout)

        val user = AuthRepository.getCurrentUser()
        if (user == null) {
            findNavController().navigate(R.id.action_profileFragment_to_gettingStartedFragment)
            return
        }

        displayUser(user)

        avatarIv.setOnClickListener { pickImageLauncher.launch("image/*") }

        itemLogout.setOnClickListener {
            AuthRepository.logout()
            findNavController().navigate(R.id.action_profileFragment_to_gettingStartedFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        AuthRepository.getCurrentUser()?.let { displayUser(it) }
    }

    private fun displayUser(user: RegisterData) {
        // Имя
        val name = listOfNotNull(
            user.name.takeIf { it.isNotBlank() },
            user.surname.takeIf { it.isNotBlank() }
        ).joinToString(" ")
        tvName.text = name

        // Email
        tvEmail.text = user.email

        // Пол
        tvGender.text = when (user.gender) {
            com.example.drivenextapp.data.Gender.MALE -> "Мужской"
            com.example.drivenextapp.data.Gender.FEMALE -> "Женский"
            else -> "Не указан"
        }

        // Google-почта (можно дублировать email пользователя)
        tvGoogleAccount.text = user.email

        // Аватар
        val uri = user.profilePhoto
        if (uri != null) {
            try {
                avatarIv.setImageURI(uri)
            } catch (e: Exception) {
                avatarIv.setImageResource(R.drawable.ic_profile_placeholder)
            }
        } else {
            avatarIv.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }
}
