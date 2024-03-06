package com.weather.feature.forecast.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.WeatherSquareWidget

@Composable
fun PressureWidget(pressure: Int, modifier: Modifier = Modifier) {
    WeatherSquareWidget(modifier = modifier, icon = Icons.Outlined.Info, title = "Pressure") {
        Text(text = "$pressure mb", fontSize = 32.sp)
    }
}

@Preview
@Composable
private fun PressurePreview() {
    PressureWidget(pressure = 1000)
}