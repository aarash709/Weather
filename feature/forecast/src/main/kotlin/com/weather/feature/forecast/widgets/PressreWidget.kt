package com.weather.feature.forecast.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.sourceInformationMarkerStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PressureWidget(pressure: Int, modifier: Modifier = Modifier) {
    WeatherSquareWidget(
        modifier = modifier,
        icon = Icons.Outlined.ArrowDownward,
        title = "Pressure"
    ) {
        PressureGraph(pressure = pressure)
        Text(text = "$pressure mb", fontSize = 32.sp)
    }
}

@Composable
private fun PressureGraph(modifier: Modifier = Modifier, pressure: Int) {
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height
        val halfWidth = size.center.x
        val archThickness = 6.dp.toPx()
        val progress = pressure.coerceIn(870, 1083).toDouble().div(1083)
        val radius = size.width / 2
        val angle = (progress * 270) + 45
        val lineRad = Math.toRadians(angle)
        val innerRadius = halfWidth.times(0.9f)
        val outerRadius = halfWidth.times(1.08f)
        val startLinesX = (innerRadius * cos(lineRad)).plus(halfWidth).toFloat()
        val endLinesX = (outerRadius * cos(lineRad)).plus(halfWidth).toFloat()

        val startLinesY = (innerRadius * -sin(lineRad)).plus(halfWidth).toFloat()
        val endLinesY = (outerRadius * -sin(lineRad)).plus(halfWidth).toFloat()

        val color = Color.Blue
        drawArc(
            color = color,
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = Offset(0f, 0f),
            style = Stroke(width = archThickness, cap = StrokeCap.Round),
        )
        drawLine(
            color = Color.Black,
            start = Offset(startLinesX, startLinesY),
            end = Offset(endLinesX, endLinesY),
            strokeWidth = 15f,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(startLinesX, startLinesY),
            end = Offset(endLinesX, endLinesY),
            strokeWidth = 10f,
            cap = StrokeCap.Round,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun PressurePreview() {
    WeatherTheme {
        FlowRow {
            PressureGraph(Modifier.weight(1f), pressure = 1000)
            PressureGraph(Modifier.weight(1f), pressure = 1000)
        }
    }
}