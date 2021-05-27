package com.smqpro.myapplication.currentweather

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smqpro.myapplication.MainRepository
import com.smqpro.myapplication.model.response.CurrentWeather
import com.smqpro.myapplication.model.response.WeatherForecast
import com.smqpro.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    var weatherList = mutableStateOf<List<CurrentWeather>>(listOf())
    var isLoading = mutableStateOf(false)

    init {
        getWeatherList(listOf("Almaty", "Astana"))
    }

    fun getWeatherList(cityList: List<String>) {
        viewModelScope.launch {
            isLoading.value = true
            val response = repository.getWeatherList(cityList)
            when (response) {
                is Resource.Success -> {
                    weatherList.value = response.data ?: listOf()
                    isLoading.value = false
                }
                is Resource.Error -> {
                    Timber.e(response.message)
                    isLoading.value = false
                }
            }
        }
    }

    fun addCity(city: String) {
        viewModelScope.launch {
            isLoading.value = true
            val response = repository.getCurrentWeather(city)
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        weatherList.value += it
                        isLoading.value = false
                    }
                }
                is Resource.Error -> {
                    Timber.e(response.message)
                    isLoading.value = false
                }
            }
        }
    }
}