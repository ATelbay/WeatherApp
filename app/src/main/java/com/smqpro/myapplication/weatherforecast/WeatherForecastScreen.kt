package com.smqpro.myapplication.weatherforecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smqpro.myapplication.R
import com.smqpro.myapplication.model.response.Daily
import com.smqpro.myapplication.model.response.Hourly
import com.smqpro.myapplication.model.response.WeatherForecast
import com.smqpro.myapplication.util.Resource
import com.smqpro.myapplication.util.asCelsius
import com.smqpro.myapplication.util.getDayOfWeekFromUnixTime
import com.smqpro.myapplication.util.getHourFromUnixTime
import timber.log.Timber

@Composable
fun WeatherForecastScreen(
    latitude: Float,
    longitude: Float,
    city: String,
    navController: NavController,
    viewModel: WeatherForecastViewModel = hiltViewModel()
) {

    val weatherForecast =
        produceState<Resource<WeatherForecast>>(initialValue = Resource.Loading()) {
            value = viewModel.getWeatherForecast(latitude, longitude)
        }.value

    Timber.d(weatherForecast.toString())
    Column {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "back_button"
            )
        }
        when (weatherForecast) {
            is Resource.Success -> {
                weatherForecast.data?.let { wf ->
                    CurrentWeather(city, wf)
                    Divider()
                    HourlyWeatherForecast(hourlyList = wf.hourly)
                    Divider()
                    DailyWeatherForecast(dailyForecast = wf.daily)
                }
            }
            is Resource.Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Error -> {
                Text(text = "Произошла ошибка", Modifier.padding(top = 16.dp))
                Timber.e(weatherForecast.message)
            }
        }
    }
}

@Composable
fun CurrentWeather(
    city: String,
    weatherForecast: WeatherForecast?
) {
    weatherForecast?.let { wf ->
        val currentWeather = wf.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = city,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                fontSize = 16.sp,
                text = currentWeather.weather[0].description.capitalize()
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                fontSize = 48.sp,
                text = currentWeather.temp.toInt().toString().asCelsius()
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "H:${weatherForecast.daily[0].temp.max.toInt()}".asCelsius())
                Text(
                    text = "L:${weatherForecast.daily[0].temp.min.toInt()}".asCelsius(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Timber.d(city) // TODO: 27.05.2021 delete

    }
}

@Composable
fun HourlyWeatherForecast(
    hourlyList: List<Hourly>
) {
    LazyRow {
        items(hourlyList) { item ->
            Column(modifier = Modifier.padding(all = 16.dp)) {
                if (item.hashCode() == hourlyList[0].hashCode()) {
                    Text(text = "Now", fontWeight = FontWeight.Bold)
                    Text(
                        text = item.temp.toInt().toString().asCelsius(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    val hour = item.dt.getHourFromUnixTime() ?: ""
                    Text(text = hour)
                    Text(
                        text = item.temp.toInt().toString().asCelsius(),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DailyWeatherForecast(
    dailyForecast: List<Daily>
) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(all = 16.dp)) {
        items(dailyForecast.drop(1)) { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val dayOfWeek = item.dt.getDayOfWeekFromUnixTime() ?: ""
                Text(text = dayOfWeek, modifier = Modifier.weight(.2f))
                Text(text = item.weather[0].main, modifier = Modifier.weight(.25f))
                val pop = (item.pop * 100).toInt()
                Text(text = "rain - $pop%", modifier = Modifier.weight(.3f))
                Text(
                    text = item.temp.max.toInt().toString().asCelsius(),
                    modifier = Modifier.weight(.1f),
                    textAlign = TextAlign.End
                )
                Text(
                    text = item.temp.min.toInt().toString().asCelsius(),
                    modifier = Modifier.weight(.1f),
                    textAlign = TextAlign.End
                )
            }

        }
    }
}