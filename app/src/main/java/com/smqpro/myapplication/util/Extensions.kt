package com.smqpro.myapplication.util

import java.text.SimpleDateFormat
import java.util.*

fun String.asCelsius() = "$this°"

fun Long.getHourFromUnixTime(): String? {
    return try {
        val sdf = SimpleDateFormat("HH", Locale.getDefault())
        val netDate = Date(this * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

fun Long.getDayTimeFromUnixTime(timeZone: TimeZone = TimeZone.getDefault()): String? {
    return try {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.timeZone = timeZone
        val netDate = Date(this * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

fun Long.getDayOfWeekFromUnixTime(): String? {
    return try {
        val sdf = SimpleDateFormat("E", Locale.getDefault())
        val netDate = Date(this * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

