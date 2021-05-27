package com.smqpro.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.smqpro.myapplication.ui.theme.MyApplicationTheme
import com.smqpro.myapplication.weatherforecast.WeatherForecastScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "weather_list_screen"
                ) {
                    composable("weather_list_screen") {
                        MaterialTheme {
                            CityWeatherListScreen(navController = navController)
                        }
                    }
                    composable("weather_forecast_screen/{latitude}/{longitude}/{city}",
                        arguments = listOf(
                            navArgument("latitude") { type = NavType.FloatType },
                            navArgument("longitude") { type = NavType.FloatType },
                            navArgument("city") { type = NavType.StringType }
                        )
                    ) {
                        val latitude = remember { it.arguments?.getFloat("latitude") }
                        val longitude = remember { it.arguments?.getFloat("longitude") }
                        val city = remember { it.arguments?.getString("city") }
                        WeatherForecastScreen(
                            latitude ?: 0f,
                            longitude ?: 0f,
                            city ?: "",
                            navController
                        )
                    }
                }
            }
        }
    }
}