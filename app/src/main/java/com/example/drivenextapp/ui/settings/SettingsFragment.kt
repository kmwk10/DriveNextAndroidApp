package com.example.drivenextapp.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.example.drivenextapp.data.RegisterRepository
import androidx.core.net.toUri

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val PREFS_NAME = "app_prefs"

    private lateinit var prefs: SharedPreferences

    // UI
    private var imgAvatar: ImageView? = null
    private var tvName: TextView? = null
    private var tvEmail: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        imgAvatar = view.findViewById(R.id.imgAvatar)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)

        val itemProfile = view.findViewById<View>(R.id.itemProfile)
        val itemBookings = view.findViewById<View>(R.id.itemBookings)
        val itemAddCar = view.findViewById<View>(R.id.itemAddCar)

        refreshProfileUi()

        itemProfile.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
            } catch (e: Exception) {
                // fallback
            }
        }

        itemBookings.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_settingsFragment_to_bookingsFragment)
            } catch (e: Exception) {
                // fallback
            }
        }

        itemAddCar.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_settingsFragment_to_becomeHostFragment)
            } catch (e: Exception) {
                // fallback
            }
        }
    }

    // При возвращении на экран (например, после изменения профиля) — обновляем UI
    override fun onResume() {
        super.onResume()
        refreshProfileUi()
    }

    private fun refreshProfileUi() {
        // Берём актуальные данные из RegisterRepository.currentData
        val reg = RegisterRepository.currentData

        // Имя и фамилия
        val name = if (reg.name.isNotBlank()) {
            "${reg.name} ${reg.surname}"
        } else {
            reg.surname
        }

        tvName?.text = name
        tvEmail?.text = reg.email

        // Аватар: если profilePhoto != null — используем setImageURI, иначе placeholder
        try {
            val uri = reg.profilePhoto
            if (uri != null) {
                imgAvatar?.setImageURI(uri)
            } else {
                imgAvatar?.setImageResource(R.drawable.ic_profile_placeholder)
            }
        } catch (e: Exception) {
            imgAvatar?.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }
}
