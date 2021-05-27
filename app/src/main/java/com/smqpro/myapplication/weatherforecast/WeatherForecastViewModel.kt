package com.smqpro.myapplication.weatherforecast

import androidx.lifecycle.ViewModel
import com.smqpro.myapplication.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    val repository: MainRepository
) : ViewModel() {

    suspend fun getWeatherForecast(latitude: Float, longitude: Float) =
        repository.getWeatherForecast(latitude, longitude)
}