package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.theme.WeatherTheme


@Composable
internal fun RealFeelWidget(
    realFeel: Float,
    modifier: Modifier = Modifier,
) {
    WeatherSquareWidget(modifier, icon = Icons.Outlined.Thermostat, title = "Real Feel") {
        Text(text = "$realFeel°", modifier = Modifier.padding(32.dp), fontSize = 32.sp)
    }
}


@Preview
@Composable
private fun RealFeelPrev() {
    WeatherTheme {
        RealFeelWidget(19f)
    }
}