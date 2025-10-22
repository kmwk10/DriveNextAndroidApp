package com.example.drivenextapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarData(
    val id: String,
    val model: String,
    val brand: String,
    val pricePerDay: Int,
    val gearbox: String,
    val fuel: String,
    val imageResId: Int? = null,
    val imageUrl: String? = null
) : Parcelable
