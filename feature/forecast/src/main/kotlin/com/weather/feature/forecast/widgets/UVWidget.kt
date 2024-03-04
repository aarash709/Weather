package com.weather.feature.forecast.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun UVWidget(uvIndex: Int, modifier: Modifier = Modifier) {
    WeatherSquareWidget(modifier = modifier, icon = Icons.Outlined.WbSunny, title = "UV Index") {
        Text(text = "$uvIndex", fontSize = 32.sp)
    }
}

@Preview
@Composable
private fun UVPreview() {
    UVWidget(uvIndex = 2)
}