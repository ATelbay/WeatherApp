package com.smqpro.myapplication.model.response

data class WeatherForecast(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Float,
    val lon: Float,
    val timezone: String,
    val timezone_offset: Int
)