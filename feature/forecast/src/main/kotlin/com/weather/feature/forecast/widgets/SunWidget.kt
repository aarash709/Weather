package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.WeatherSquareWidget
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SunWidget(sunrise: Int, sunset: Int, modifier: Modifier = Modifier) {
    WeatherSquareWidget(modifier = modifier, icon = Icons.Outlined.WbSunny, title = "UV Index") {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}

@Composable
private fun SunGraph(modifier: Modifier = Modifier, sunrise: Int, sunset: Int) {
    Spacer(modifier = modifier
        .aspectRatio(1f)
        .padding(16.dp)
        .drawWithCache {
//            val circleSize = 8.dp.toPx()
//            val archThickness = 7.dp.toPx()
//            val progress = uvIndex
//                .coerceAtMost(11)
//                .toDouble()
//                .div(11)
//            val radius = size.width / 2
//            val angle = (progress * 270) + 45
//            val x = -(radius * sin(Math.toRadians(angle)).toFloat()) + size.width / 2
//            val y = (radius * cos(Math.toRadians(angle)).toFloat()) + size.height / 2
//            val colors = Brush.linearGradient(
//                listOf(Color.Green, Color.Yellow, Color.Red, Color.Blue),
//            )
            onDrawBehind {

            }
        })
}


@Preview
@Composable
private fun UVPreview() {
    SunWidget(sunrise = 100, sunset = 100)
}