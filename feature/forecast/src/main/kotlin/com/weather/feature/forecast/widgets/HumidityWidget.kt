package com.weather.feature.forecast.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
internal fun HumidityWidget(
    humidity: Int,
    modifier: Modifier = Modifier,
) {
    WeatherSquareWidget(
        modifier = modifier,
        icon = Icons.Outlined.WaterDrop, title = "Humidity"
    ) {
        Text(text = "$humidity%", fontSize = 32.sp)
    }
}

@Preview
@Composable
private fun HumidityPreview() {
    HumidityWidget(humidity = 25)
}