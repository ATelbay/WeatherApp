package com.smqpro.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smqpro.myapplication.currentweather.WeatherViewModel
import com.smqpro.myapplication.model.response.CurrentWeather
import com.smqpro.myapplication.util.asCelsius
import com.smqpro.myapplication.util.getDayTimeFromUnixTime
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun CityWeatherListScreen(navController: NavController) {
    Column {
        CityAddInput()
        CityWeatherList(navController)
    }
}

@ExperimentalComposeUiApi
@Composable
fun CityAddInput(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val (text, onTextChange) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val submit = {
        viewModel.addCity(text)
        onTextChange("")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                submit()
                keyboardController?.hide()
            }),
            modifier = modifier.weight(0.8f)
        )
        TextButton(onClick = submit, enabled = text.isNotBlank()) {
            Text("Add")
        }
    }

}

@Composable
fun CityWeatherList(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val currentWeatherList by remember { viewModel.weatherList }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        items(currentWeatherList) {
            WeatherItem(it) { weather ->
                navController.navigate(
                    "weather_forecast_screen/${weather.coord.lat}/${weather.coord.lon}/${weather.name}"
                )
            }
        }

    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun WeatherItem(
    weather: CurrentWeather,
    onItemClicked: (CurrentWeather) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onItemClicked(weather) }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            val mindOffset = weather.dt + weather.timezone
            val hour = mindOffset.getDayTimeFromUnixTime(TimeZone.getTimeZone("GMT")) ?: ""
            Text(hour)
            Text(text = weather.name, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
        }
        val temp = weather.main.temp.toInt().toString().asCelsius()
        Text(text = temp, fontSize = 48.sp)
    }
}