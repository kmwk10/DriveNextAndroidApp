package com.example.drivenextapp.data

import android.net.Uri
import java.util.*

data class RegisterData(
    var email: String = "",
    var password: String = "",
    var surname: String = "",
    var name: String = "",
    var patronymic: String = "",
    var birthDate: Date? = null,
    var gender: Gender? = null,
    var driverLicenseNumber: String = "",
    var driverLicenseIssueDate: Date? = null,
    var profilePhoto: Uri? = null,
    var driverLicensePhoto: Uri? = null,
    var passportPhoto: Uri? = null
)

enum class Gender {
    MALE, FEMALE
}
