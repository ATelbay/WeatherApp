package com.smqpro.myapplication.model.api

import com.smqpro.myapplication.model.response.CurrentWeather
import com.smqpro.myapplication.model.response.WeatherForecast
import com.smqpro.myapplication.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String = Constants.API_KEY
    ): CurrentWeather

    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("units") units: String = "metric",
        @Query("exclude") excludedFields: String = "minutely,alerts",
        @Query("appid") apiKey: String = Constants.API_KEY
    ): WeatherForecast
}