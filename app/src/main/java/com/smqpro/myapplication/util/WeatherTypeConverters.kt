package com.smqpro.myapplication.util

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import com.smqpro.myapplication.model.response.Weather
import java.lang.reflect.Type

class WeatherTypeConverters {
    var gson = Gson()

    @TypeConverter
    fun stringToWeatherList(data: String): List<Weather> {
        val listType: Type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun WeatherListToString(Weathers: List<Weather>): String {
        return gson.toJson(Weathers)
    }
}