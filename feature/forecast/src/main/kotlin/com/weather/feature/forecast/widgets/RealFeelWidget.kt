package com.weather.feature.forecast.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.theme.WeatherTheme
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin


@Composable
internal fun RealFeelWidget(
    realFeel: Int,
    modifier: Modifier = Modifier,
) {
    WeatherSquareWidget(modifier, icon = Icons.Outlined.Thermostat, title = "Real Feel") {
        RealFeel(realFeel)
        Text(text = "${realFeel}°", modifier = Modifier.padding(32.dp), fontSize = 32.sp)
    }
}

@Composable
private fun RealFeel(realFeel: Int) {
    Canvas(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        val circleSize = 7.dp.toPx()
        val archThickness = 6.dp.toPx()
        val progress = realFeel.coerceIn(minimumValue = 0, maximumValue = 40).toDouble().div(40)
        val radius = size.width / 2
        val angle = (progress * 270) + 45
        val x = -(radius * sin(Math.toRadians(angle)).toFloat()) + size.width / 2
        val y = (radius * cos(Math.toRadians(angle)).toFloat()) + size.height / 2
        // 5 degrees added for a gap between archs and is subtracted
        // from the sweep angle of the last arch
        drawArc(
            color = Color.Blue.copy(green = 0.5f),
            startAngle = 135f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(0f, 0f),
            style = Stroke(width = archThickness),
        )
        drawArc(
            color = Color.Green,
            startAngle = 228f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(0f, 0f),
            style = Stroke(width = archThickness),
        )
        drawArc(
            color = Color.Red.copy(green = 0.5f),
            startAngle = 321f,
            sweepAngle = 84f,
            useCenter = false,
            topLeft = Offset(0f, 0f),
            style = Stroke(width = archThickness),
        )
        drawCircle(
            Color.White,
            radius = circleSize,
            center = Offset(x = x, y = y)
        )
        drawCircle(
            Color.Black,
            radius = circleSize,
            center = Offset(x = x, y = y),
            style = Stroke(archThickness / 5)
        )
    }
}


@Preview
@Composable
private fun RealFeelPrev() {
    WeatherTheme {
        RealFeelWidget(-10)
    }
}