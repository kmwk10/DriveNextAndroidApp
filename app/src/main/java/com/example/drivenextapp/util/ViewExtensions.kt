package com.example.drivenextapp.util

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(message: String?) {
    this.error = message
}

fun TextInputEditText.getTextOrEmpty(): String = this.text?.toString().orEmpty()
