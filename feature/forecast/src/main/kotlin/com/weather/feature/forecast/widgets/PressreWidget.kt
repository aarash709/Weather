package com.weather.feature.forecast.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.experiment.weather.core.common.R.string
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PressureWidget(modifier: Modifier = Modifier, pressure: Int, surfaceColor: Color) {
    WeatherSquareWidget(
        modifier = modifier,
        title = stringResource(id = string.pressure),
        surfaceColor = surfaceColor,
        infoText = "$pressure"

    ) {
        PressureGraph(
            pressure = pressure,
            pressureUnit = stringResource(id = string.pressure_symbol)
        )
    }
}

@Composable
private fun PressureGraph(
    modifier: Modifier = Modifier,
    pressure: Int,
    pressureUnit: String,
    minPressure: Int = 870,
    maxPressure: Int = 1080,
) {
    val textMeasurer = rememberTextMeasurer()
    val painter = rememberVectorPainter(image = Icons.Outlined.ArrowDownward)
    val paleOnSurfaceColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    Spacer(
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .padding(12.dp)
            .aspectRatio(1f)
            .drawWithCache {
                val width = size.width
                val height = size.height
                val halfWidth = size.center.x
                val archThickness = (width / 12f)
                val indicatorThickness = (width / 30f)
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

                val blueColor = Color.Blue.copy(green = 0.6f)
                onDrawBehind {
                    drawArc(
                        color = blueColor,
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
                        strokeWidth = indicatorThickness * 3f,
                        cap = StrokeCap.Round,
                        blendMode = BlendMode.Clear
                    )
                    drawLine(
                        color = blueColor,
                        start = Offset(startLinesX, startLinesY),
                        end = Offset(endLinesX, endLinesY),
                        strokeWidth = indicatorThickness,
                        cap = StrokeCap.Round,
                    )
                    val textLayoutResult = textMeasurer.measure(
                        text = pressureUnit,
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = (size.width * 0.16f).toSp(),
                            textAlign = TextAlign.Center
                        )
                    )
                    drawText(
                        textLayoutResult,
                        color = paleOnSurfaceColor,
                        topLeft = Offset(
                            x = (width / 2).minus(textLayoutResult.size.width.div(2)),
                            y = (height / 1.2f).minus(textLayoutResult.size.height.div(2))

                        ),
                    )
                    val size = Size(width / 3, height / 3)
                    translate(
                        left = (width / 2) - size.width / 2,
                        top = (height / 2) - size.height / 2
                    ) {
                        with(painter) {
                            draw(
                                size = Size(width / 3, height / 3),
                                colorFilter = ColorFilter.tint(blueColor)
                            )
                        }
                    }
                }
            }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun PressurePreview() {
    WeatherTheme {
        val color = MaterialTheme.colorScheme.background
        FlowRow(
            Modifier.background(Color.Blue.copy(green = 0.35f)),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PressureWidget(Modifier.weight(1f), pressure = 890, surfaceColor = color)
            PressureWidget(Modifier.weight(1f), pressure = 1080, surfaceColor = color)
        }
    }
}