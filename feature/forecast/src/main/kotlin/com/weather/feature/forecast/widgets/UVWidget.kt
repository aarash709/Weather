package com.weather.feature.forecast.widgets

import androidx.compose.foundation.Canvas
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
fun UVWidget(uvIndex: Int, modifier: Modifier = Modifier) {
    val uvLevel by remember {
        val value = when (uvIndex) {
            in 0..2 -> "Low"
            in 3..5 -> "Moderate"
            in 6..7 -> "High"
            in 8..10 -> "Very High"
            else -> "Extreme"
        }
        mutableStateOf(value)
    }
    WeatherSquareWidget(modifier = modifier, icon = Icons.Outlined.WbSunny, title = "UV Index") {
        UVGraph(Modifier, uvIndex)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "$uvIndex\n$uvLevel", fontSize = 24.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun UVGraph(modifier: Modifier = Modifier, uvIndex: Int) {
    Spacer(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
            .drawWithCache {
                val circleSize = 8.dp.toPx()
                val archThickness = 7.dp.toPx()
                val progress = uvIndex
                    .coerceAtMost(11)
                    .toDouble()
                    .div(11)
                val radius = size.width / 2
                val angle = (progress * 270) + 45
                val x = -(radius * sin(Math.toRadians(angle)).toFloat()) + size.width / 2
                val y = (radius * cos(Math.toRadians(angle)).toFloat()) + size.height / 2
                val colors = Brush.linearGradient(
                    listOf(Color.Green, Color.Yellow, Color.Red, Color.Blue),
                )
                onDrawBehind {
                    drawArc(
                        brush = colors,
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        style = Stroke(width = archThickness, cap = StrokeCap.Round),
                    )
                    drawCircle(
                        Color.Blue.copy(green = 0.4f),
                        radius = circleSize,
                        center = Offset(x = x, y = y)
                    )
                    drawCircle(
                        Color.Black,
                        radius = circleSize,
                        center = Offset(x = x, y = y),
                        style = Stroke(circleSize / 3)
                    )

                }
            }
    )
}

@Preview
@Composable
private fun UVPreview() {
    UVWidget(uvIndex = 2)
}