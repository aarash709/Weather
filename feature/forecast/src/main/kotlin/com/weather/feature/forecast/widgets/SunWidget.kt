package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.core.design.components.WeatherSquareWidget
import timber.log.Timber
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SunWidget(sunrise: Int, sunset: Int, currentTime: Int, modifier: Modifier = Modifier) {
    WeatherSquareWidget(
        modifier = modifier.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen),
        icon = Icons.Outlined.WbSunny,
        title = "Sunrise"
    ) {
        SunGraph(
            modifier = Modifier,
            sunrise = sunrise,
            sunset = sunset,
            currentTime = currentTime
        )
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
        .padding(16.dp)
        .drawWithCache {
            val width = size.width
            val height = size.height
            val circleSize = 8.dp.toPx()
            val sunsetColor = Red.copy(green = 0.3f)
            val darkBlue = Color(5, 20, 50)
            val daylightColor = Color(244, 250, 127)
            val daylightBrush = Brush.horizontalGradient(
                0.0f to sunsetColor,
                0.05f to daylightColor,
                0.95f to daylightColor,
                1.0f to sunsetColor
            )
            val nightColor = Brush.horizontalGradient(listOf(darkBlue, darkBlue))
            val archThickness = 6.dp.toPx()
            val timeRange = sunset.minus(sunrise)
            val progress = currentTime
                .minus(sunrise)
                .toFloat()
                .div(timeRange)
                .times(180)
            val radius = size.width / 2
            val angle = (progress) + 180.0
            val x = (radius * cos(Math.toRadians(angle)).toFloat()) + size.width / 2
            val y = (radius * sin(Math.toRadians(angle)).toFloat()) + size.height / 2
            onDrawBehind {
                drawLine(
                    Color.White.copy(alpha = 0.5f),
                    start = Offset(
                        width - (width.times(1.25f)),
                        height / 2
                    ),
                    end = Offset(width.times(1.25f), height / 2)
                )
                drawSundial(dayBrush = daylightBrush, archThickness = archThickness)
                val position = if (x
                        .isNaN()
                        .not() && y
                        .isNaN()
                        .not()
                ) {
                    Offset(x = x, y = y)
                } else Offset.Zero
                drawCircleIndicator(
                    brush = if (currentTime > sunset)
                        nightColor
                    else daylightBrush,
                    circleSize = circleSize,
                    position = position,
                    shouldShowBorder = true
                )
            }
        })
}

private fun DrawScope.drawSundial(dayBrush: Brush, archThickness: Float) {
    drawArc(
        color = Color.White.copy(alpha = 0.5f),
        topLeft = Offset.Zero,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        style = Stroke(
            width = archThickness,
            cap = StrokeCap.Butt,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(5f, 20f),
                phase = 15f
            )
        )
    )
    drawArc(
        brush = dayBrush,
        topLeft = Offset.Zero,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        style = Stroke(width = archThickness),
    )
}

private fun DrawScope.drawCircleIndicator(
    brush: Brush,
    circleSize: Float,
    position: Offset = Offset.Zero,
    shouldShowBorder: Boolean = true,
) {
    if (shouldShowBorder) {
        drawCircle(
            color = Color.Black,
            radius = circleSize.times(1.3f),
            center = position,
            blendMode = BlendMode.Clear

        )
    }
    drawCircle(
        brush = brush,
        radius = circleSize,
        center = position
    )
}

@Preview
@Composable
private fun UVPreview() {
    SunWidget(sunrise = 0, sunset = 100, currentTime = 120)
}