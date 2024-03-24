package com.weather.feature.forecast.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PressureWidget(modifier: Modifier = Modifier, pressure: Int) {
    WeatherSquareWidget(
        modifier = modifier,
        icon = Icons.Outlined.ArrowDownward,
        title = "Pressure",
        infoText = "$pressure"
    ) {
        PressureGraph(
            pressure = pressure
        )
    }
}

@Composable
private fun PressureGraph(
    modifier: Modifier = Modifier,
    pressure: Int,
    minPressure: Int = 870,
    maxPressure: Int = 1080,
) {
    val textMeasurer = rememberTextMeasurer()
    Spacer(
        modifier = modifier
            .aspectRatio(1f)
            .drawWithCache {
                val width = size.width
                val height = size.height
                val halfWidth = size.center.x
                val archThickness = 7.dp.toPx()
                val range = maxPressure
                    .minus(minPressure)
                    .toFloat()
                //normalized
                val progress =
                    (pressure
                        .coerceIn(minPressure, maxPressure)
                        .minus(minPressure))
                        .div(range)
                val angle = (progress * 270) + 135.0
                val lineRad = Math.toRadians(angle)
                val innerRadius = halfWidth.times(0.9f)
                val outerRadius = halfWidth.times(1.08f)

                val startLinesX = (innerRadius * cos(lineRad))
                    .plus(halfWidth)
                    .toFloat()
                val endLinesX = (outerRadius * cos(lineRad))
                    .plus(halfWidth)
                    .toFloat()

                val startLinesY = (innerRadius * sin(lineRad))
                    .plus(halfWidth)
                    .toFloat()
                val endLinesY = (outerRadius * sin(lineRad))
                    .plus(halfWidth)
                    .toFloat()

                val color = Color.Blue.copy(green = 0.5f)
                onDrawBehind {
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
                        strokeWidth = 30f,
                        cap = StrokeCap.Round,
                    )
                    drawLine(
                        color = color.copy(green = 0.4f),
                        start = Offset(startLinesX, startLinesY),
                        end = Offset(endLinesX, endLinesY),
                        strokeWidth = 15f,
                        cap = StrokeCap.Round,
                    )
                    val textLayoutResult = textMeasurer.measure(
                        text = "$pressure",
                        maxLines = 1,
                        style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center)
                    )
                    drawText(
                        textLayoutResult,
                        color = Color.White,
                        topLeft = Offset(
                            x = (width / 2).minus(textLayoutResult.size.width.div(2)),
                            y = (height / 2).minus(textLayoutResult.size.height.div(2))

                        ),
                    )
                }
            }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun PressurePreview() {
    WeatherTheme {
        FlowRow(Modifier.background(Color.Blue.copy(green = 0.35f))) {
            PressureWidget(Modifier.weight(1f), pressure = 890)
            PressureWidget(Modifier.weight(1f), pressure = 1080)
        }
    }
}