package com.experiment.weather.presentation.ui.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun HourlyForecast() {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Today",
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WeatherForecastItemPreview()
            WeatherForecastItemPreview()
            WeatherForecastItemPreview()
            WeatherForecastItemPreview()
            WeatherForecastItemPreview()
        }
    }
}

@Composable
private fun WeatherForecastItemPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "11am",
            fontSize = 10.sp
        )
        Icon(imageVector = Icons.Default.Cloud, contentDescription = "Weather Icon")
        Text(
            text = "32°",
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}