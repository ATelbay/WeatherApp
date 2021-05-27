package com.smqpro.myapplication.model.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.smqpro.myapplication.util.WeatherTypeConverters

@Entity(tableName = "weather")
data class CurrentWeather(
    val base: String,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    val cod: Int,
    @Embedded(prefix = "coord_")
    val coord: Coord,
    val dt: Long,
    @PrimaryKey
    val id: Int,
    @Embedded(prefix = "main_")
    val main: Main,
    val name: String,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    val timezone: Long,
    val visibility: Int,
    @TypeConverters(WeatherTypeConverters::class)
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind
)