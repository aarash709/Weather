package com.weather.feature.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.Hourly
import kotlin.math.roundToInt


@Composable
fun HourlyForecast(
    modifier: Modifier = Modifier,
    data: List<Hourly>,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(data) { hourly ->
            HourlyItem(modifier = Modifier.padding(end = 24.dp), hourly)
        }
    }
}

@Composable
fun HourlyItem(
    modifier: Modifier = Modifier,
    hourly: Hourly,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = hourly.dt.toString())
        }
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${hourly.icon}@2x.png",
            contentDescription = "Weather Icon",
            modifier = Modifier
        )
//        Icon(imageVector = Icons.Default.WbSunny, contentDescription = "Weather Icon")
        Text(
            text = "${hourly.temp.toFloat().minus(273.15).roundToInt()}°",
            color = Color.Gray
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HourlyPreview() {
    WeatherTheme {
        HourlyForecast(data = Hourly)
    }
}

val Hourly = listOf(
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "13:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 8.0,
        uvi = 0.0,
        visibility = 100,
       description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ),
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "14:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 11.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ),
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "15:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 9.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ),
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "16:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 11.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ),
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "17:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 15.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    )
)