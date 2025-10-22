package com.example.drivenextapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.drivenextapp.data.CarRepository
import com.example.drivenextapp.data.Result
import com.example.drivenextapp.data.CarData
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _cars = MutableLiveData<List<CarData>>(emptyList())
    val cars: LiveData<List<CarData>> = _cars

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadCars() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            when (val r = CarRepository.fetchAllCars()) {
                is Result.Success -> {
                    _cars.value = r.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _cars.value = emptyList()
                    _isLoading.value = false
                    _errorMessage.value = r.message
                }
            }
        }
    }

    fun searchBrand(brand: String, onResult: (Result<List<CarData>>) -> Unit) {
        if (brand.isBlank()) {
            onResult(Result.Success(emptyList()))
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            when (val r = CarRepository.searchByBrand(brand.trim())) {
                is Result.Success -> {
                    _isLoading.value = false
                    onResult(r)
                }
                is Result.Error -> {
                    _isLoading.value = false
                    onResult(r)
                }
            }
        }
    }
}
