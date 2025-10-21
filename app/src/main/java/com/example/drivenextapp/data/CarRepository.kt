package com.example.drivenextapp.data

import com.example.drivenextapp.data.CarData
import kotlinx.coroutines.delay

object CarRepository {

    // Эмуляция сетевого запроса
    suspend fun fetchAllCars(): Result<List<CarData>> {
        try {
            delay(1200) // эмуляция задержки сети
            val cars = listOf(
                CarData("1","S 500 Sedan","Mercedes-Benz",2500,"A/T","Бензин", imageResId = null),
                CarData("2","Camry","Toyota",1800,"A/T","Бензин", imageResId = null),
                CarData("3","Model S","Tesla",5000,"A/T","Электро", imageResId = null)
            )
            return Result.Success(cars)
        } catch (e: Exception) {
            return Result.Error("Не удалось загрузить данные. Попробуйте снова.")
        }
    }

    suspend fun searchByBrand(brandQuery: String): Result<List<CarData>> {
        try {
            delay(800)
            val all = (fetchAllCars() as? Result.Success)?.data ?: listOf()
            val filtered = all.filter { it.brand.contains(brandQuery, ignoreCase = true) }
            return Result.Success(filtered)
        } catch (e: Exception) {
            return Result.Error("Не удалось выполнить поиск. Попробуйте снова.")
        }
    }
}
