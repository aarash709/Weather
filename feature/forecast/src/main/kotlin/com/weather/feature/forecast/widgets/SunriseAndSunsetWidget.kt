package com.weather.feature.forecast.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessHigh
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SunriseAndSunsetWidget(modifier: Modifier = Modifier) {
    //changes based on time
    val sunriseOrSunset = "Sunrise"
    WeatherSquareWidget(modifier = modifier,icon = Icons.Outlined.BrightnessHigh, title = sunriseOrSunset) {
        SunMoonPosition()
    }
}


@Composable
private fun SunMoonPosition() {
    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Canvas(modifier = Modifier) {
            drawRect(color = Color.Red)
            drawArc(
                brush = Brush.linearGradient(listOf(Color.Red, Color.Blue)),
                startAngle = 40f, sweepAngle = 50f, useCenter = false, topLeft = Offset(80f, 20f)
            )
        }
    }
}

@Preview
@Composable
private fun SunrisePreview() {
    SunriseAndSunsetWidget()
}

