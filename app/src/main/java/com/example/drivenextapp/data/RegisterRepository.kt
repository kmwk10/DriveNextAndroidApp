package com.example.drivenextapp.data

import android.net.Uri
import java.util.Date

object RegisterRepository {

    val currentData = RegisterData()

    // Сохранение данных
    fun saveEmail(email: String) { currentData.email = email }
    fun savePassword(password: String) { currentData.password = password }
    fun saveSurname(surname: String) { currentData.surname = surname }
    fun saveName(name: String) { currentData.name = name }
    fun savePatronymic(patronymic: String) { currentData.patronymic = patronymic }
    fun saveBirthDate(date: Date) { currentData.birthDate = date }
    fun saveGender(gender: Gender) { currentData.gender = gender }
    fun saveDriverLicenseNumber(number: String) { currentData.driverLicenseNumber = number }
    fun saveDriverLicenseIssueDate(date: Date) { currentData.driverLicenseIssueDate = date }
    fun saveDriverLicensePhoto(uri: Uri) { currentData.driverLicensePhoto = uri }
    fun savePassportPhoto(uri: Uri) { currentData.passportPhoto = uri }
    fun saveProfilePhoto(uri: Uri) { currentData.profilePhoto = uri }

    // Валидация полей
    fun isEmailValid(): Boolean = currentData.email.isNotBlank()
    fun isPasswordValid(): Boolean = currentData.password.isNotBlank()
    fun isSurnameValid(): Boolean = currentData.surname.isNotBlank()
    fun isNameValid(): Boolean = currentData.name.isNotBlank()
    fun isPatronymicValid(): Boolean = currentData.patronymic.isNotBlank()
    fun isBirthDateValid(): Boolean = currentData.birthDate != null
    fun isGenderValid(): Boolean = currentData.gender != null
    fun isDriverLicenseNumberValid(): Boolean = currentData.driverLicenseNumber.isNotBlank()
    fun isDriverLicenseIssueDateValid(): Boolean = currentData.driverLicenseIssueDate != null
    fun isDriverLicensePhotoValid(): Boolean = currentData.driverLicensePhoto != null
    fun isPassportPhotoValid(): Boolean = currentData.passportPhoto != null
    fun isProfilePhotoValid(): Boolean = currentData.profilePhoto != null
}
