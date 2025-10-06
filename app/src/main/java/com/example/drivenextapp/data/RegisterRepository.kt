package com.example.drivenextapp.data

import android.net.Uri
import com.example.drivenextapp.util.ValidationUtils
import java.util.*

object RegisterRepository {

    val currentData = RegisterData()

    // Сохранение данных
    fun saveEmail(email: String) { currentData.email = email.trim() }
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
    fun isSurnameValid(): Boolean = currentData.surname.isNotBlank()
    fun isNameValid(): Boolean = currentData.name.isNotBlank()
    fun isPatronymicValid(): Boolean = currentData.patronymic.isNotBlank()
    fun isBirthDateValid(): Boolean = currentData.birthDate != null
    fun isGenderValid(): Boolean = currentData.gender != null

    fun isDriverLicenseNumberValid(): Boolean = currentData.driverLicenseNumber.length == 10
    fun isDriverLicenseIssueDateValid(): Boolean = currentData.driverLicenseIssueDate != null
    fun isDriverLicensePhotoValid(): Boolean = currentData.driverLicensePhoto != null
    fun isPassportPhotoValid(): Boolean = currentData.passportPhoto != null

    // Валидация
    fun validateStep1(confirmPassword: String, acceptedTerms: Boolean): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (currentData.email.isBlank()) {
            errors["email"] = "Это поле является обязательным"
        } else if (!ValidationUtils.isEmailValid(currentData.email)) {
            errors["email"] = "Введите корректный адрес электронной почты."
        }

        if (currentData.password.isBlank()) {
            errors["password"] = "Это поле является обязательным"
        } else if (!ValidationUtils.isPasswordStrong(currentData.password)) {
            errors["password"] = "Пароль должен быть не менее 8 символов и содержать хотя бы одну цифру."
        }

        if (!ValidationUtils.arePasswordsMatching(currentData.password, confirmPassword)) {
            errors["repeat"] = "Пароли не совпадают."
        }

        if (!acceptedTerms) {
            errors["terms"] = "Необходимо согласиться с условиями обслуживания и политикой конфиденциальности."
        }

        return errors
    }

    fun validateStep2(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (!isSurnameValid()) errors["surname"] = "Это поле является обязательным"
        if (!isNameValid()) errors["name"] = "Это поле является обязательным"
        if (!isPatronymicValid()) errors["patronymic"] = "Это поле является обязательным"
        if (!isBirthDateValid()) errors["birthDate"] = "Введите корректную дату рождения"
        if (!isGenderValid()) errors["gender"] = "Выберите пол"

        return errors
    }

    fun validateStep3(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (currentData.driverLicenseNumber.isBlank()) {
            errors["licenseNumber"] = "Это поле является обязательным"
        } else if (!isDriverLicenseNumberValid()) {
            errors["licenseNumber"] = "Введите корректный номер водительского удостоверения"
        }

        if (!isDriverLicenseIssueDateValid()) errors["licenseDate"] = "Введите корректную дату выдачи"
        if (!isDriverLicensePhotoValid()) errors["licensePhoto"] = "Загрузите фото водительского удостоверения"
        if (!isPassportPhotoValid()) errors["passportPhoto"] = "Загрузите фото паспорта"

        return errors
    }
}
