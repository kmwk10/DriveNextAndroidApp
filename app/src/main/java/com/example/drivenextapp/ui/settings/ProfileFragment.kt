package com.example.drivenextapp.ui.settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.example.drivenextapp.data.RegisterRepository
import com.example.drivenextapp.util.PrefsManager

class ProfileFragment : Fragment() {
    private lateinit var prefs: PrefsManager
    private lateinit var avatarIv: ImageView
    private lateinit var itemLogout: LinearLayout
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Регистрируем системный выбор изображения (галерея / файлы)
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                // Сохраняем фото в репозиторий
                RegisterRepository.saveProfilePhoto(uri)
                // Отображаем новое изображение
                avatarIv.setImageURI(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = PrefsManager(requireContext())
        super.onViewCreated(view, savedInstanceState)

        avatarIv = view.findViewById(R.id.imgAvatar)
        itemLogout = view.findViewById(R.id.itemLogout)

        // Отображаем текущее фото профиля, если оно сохранено
        RegisterRepository.currentData.profilePhoto?.let { uri ->
            avatarIv.setImageURI(uri)
        }

        // Нажатие на аватар - выбрать новое изображение
        avatarIv.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Нажатие на кнопку выхода
        itemLogout.setOnClickListener {
            // Очистка данных пользователя
            clearUserData()
            prefs.clearAccessToken()
            findNavController().navigate(R.id.action_profileFragment_to_gettingStartedFragment)
        }
    }

    private fun clearUserData() {
        with(RegisterRepository.currentData) {
            email = ""
            password = ""
            surname = ""
            name = ""
            patronymic = ""
            birthDate = null
            gender = null
            driverLicenseNumber = ""
            driverLicenseIssueDate = null
            driverLicensePhoto = null
            passportPhoto = null
            profilePhoto = null
        }
    }
}
