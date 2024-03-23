package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.White
import com.weather.core.design.theme.Yellow
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SunWidget(sunrise: Int, sunset: Int, currentTime: Int, modifier: Modifier = Modifier) {
    WeatherSquareWidget(modifier = modifier, icon = Icons.Outlined.WbSunny, title = "Sunrise") {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SunGraph(
                modifier = Modifier,
                sunrise = sunrise,
                sunset = sunset,
                currentTime = currentTime
            )
        }
    }
}

@Composable
private fun SunGraph(
    modifier: Modifier = Modifier,
    sunrise: Int,
    sunset: Int,
    currentTime: Int,
) {
    Spacer(modifier = modifier
        .aspectRatio(1f)
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .padding(16.dp)
        .drawWithCache {
            val circleSize = 8.dp.toPx()
            val sunsetColor = Red.copy(green = 0.3f)
            val dayColors = Brush.horizontalGradient(
                0.0f to sunsetColor,
                0.1f to Yellow,
                0.9f to Yellow,
                1.0f to sunsetColor
            )
            val archThickness = 6.dp.toPx()
            val timeRange = sunset.minus(sunrise)
            val progress = currentTime
                .minus(sunrise)
                .times(180)
                .toFloat()
                .div(timeRange)
            val radius = size.width / 2
            val angle = (progress) + 180.0
            val x = (radius * cos(Math.toRadians(angle)).toFloat()) + size.width / 2
            val y = (radius * sin(Math.toRadians(angle)).toFloat()) + size.height / 2
            onDrawBehind {
                drawArc(
                    color = Color.White.copy(alpha = 0.7f),
                    topLeft = Offset.Zero,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(
                        width = archThickness,
                        cap = StrokeCap.Butt,
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(5f, 30f),
                            phase = 20f
                        )
                    )
                )
                drawArc(
                    brush = dayColors,
                    topLeft = Offset.Zero,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(width = archThickness, cap = StrokeCap.Round),
                )
                drawCircle(
                    brush = dayColors,
                    radius = circleSize.times(1.3f),
                    center = Offset(x, y),
                    blendMode = BlendMode.Clear

                )

                val nightColor = Brush.horizontalGradient(listOf(White,White))
                drawCircle(
                    brush = if (currentTime > sunset)
                        nightColor
                    else dayColors,
                    radius = circleSize,
                    center = Offset(x, y)
                )
            }
        })
}


@Preview
@Composable
private fun UVPreview() {
    SunWidget(sunrise = 0, sunset = 100, currentTime = 120)
}