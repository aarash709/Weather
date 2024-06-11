package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.experiment.weather.core.common.R.*
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun UVWidget(uvIndex: Int, modifier: Modifier = Modifier, surfaceColor: Color) {
    val context = LocalContext.current
    val uvLevel by remember(uvIndex) {
        val uvIntensity = context.resources.getStringArray(array.uv_intensity)
        val value = when (uvIndex) {
            in 0..2 -> uvIntensity[0]
            in 3..5 -> uvIntensity[1]
            in 6..7 -> uvIntensity[2]
            in 8..10 -> uvIntensity[3]
            else -> uvIntensity[4]
        }
        mutableStateOf(value)
    }
    WeatherSquareWidget(
        modifier = modifier,
        title = stringResource(id = string.uv_index),
        surfaceColor = surfaceColor,
        infoText = uvLevel
    ) {
        UVGraph(modifier = Modifier, uvIndex = uvIndex)
    }
}

@Composable
fun UVGraph(modifier: Modifier = Modifier, uvIndex: Int) {
    val textMeasurer = rememberTextMeasurer()
    val textColor = LocalContentColor.current
    Spacer(
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .padding(12.dp)
            .aspectRatio(1f)
            .drawWithCache {
                val width = size.width
                val height = size.height
                val indicatorRadius = (width / 15f)
                val archThickness = (width / 12f)
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
                        brush = colors,
                        radius = indicatorRadius,
                        center = Offset(x = x, y = y)
                    )
                    drawCircle(
                        Color.Black,
                        radius = indicatorRadius,
                        center = Offset(x = x, y = y),
                        style = Stroke(indicatorRadius / 2),
                        blendMode = BlendMode.Clear

                    )
                    val textSize = (size.width * 0.30f).toSp()
                    val textLayoutResult = textMeasurer.measure(
                        text = "$uvIndex",
                        maxLines = 2,
                        style = TextStyle(fontSize = textSize, textAlign = TextAlign.Center)
                    )
                    drawText(
                        textLayoutResult,
                        color = textColor,
                        topLeft = Offset(
                            x = (width / 2).minus(textLayoutResult.size.width.div(2)),
                            y = (height / 2).minus(textLayoutResult.size.height.div(2))

                        ),
                    )
                }
            }
    )
}

@Preview
@Composable
private fun UVPreview() {
    WeatherTheme {
        val color = MaterialTheme.colorScheme.background
        UVWidget(uvIndex = 5, surfaceColor = color)
    }

}