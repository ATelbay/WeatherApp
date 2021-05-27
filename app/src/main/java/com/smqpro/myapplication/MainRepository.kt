package com.smqpro.myapplication

import com.smqpro.myapplication.model.api.WeatherApi
import com.smqpro.myapplication.model.response.CurrentWeather
import com.smqpro.myapplication.model.response.WeatherForecast
import com.smqpro.myapplication.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MainRepository @Inject constructor(
    private val api: WeatherApi
) {
    suspend fun getWeatherList(
        cityNameList: List<String>,
        units: String = "metric"
    ): Resource<List<CurrentWeather>> {
        val response = try {
            val cityList = mutableListOf<CurrentWeather>()
            for (i in 0..1)
                cityList.add(api.getCurrentWeather(cityNameList[i], units))
            cityList
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An unknown error")
        }
        return Resource.Success(response)
    }

    suspend fun getCurrentWeather(
        cityName: String,
        units: String = "metric"
    ): Resource<CurrentWeather> {
        val response = try {
            api.getCurrentWeather(cityName, units)
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An unknown error occurred.")
        }
        return Resource.Success(response)
    }

    suspend fun getWeatherForecast(
        latitude: Float,
        longitude: Float,
        units: String = "metric"
    ): Resource<WeatherForecast> {
        val response = try {
            api.getWeatherForecast(latitude, longitude, units)
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An unknown error occurred.")
        }
        return Resource.Success(response)
    }
}