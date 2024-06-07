package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.experiment.weather.core.common.R.string
import com.weather.core.design.components.WeatherSquareWidget

@Composable
internal fun HumidityWidget(
    humidity: Int,
    modifier: Modifier = Modifier,
    surfaceColor: Color,
) {
    WeatherSquareWidget(
        modifier = modifier,
        icon = Icons.Outlined.WaterDrop,
        title = stringResource(id = string.humidity),
        infoText = "$humidity%",
        surfaceColor = surfaceColor
    ) {
        HumidityGraph(humidity = humidity)
    }
}

@Composable
fun HumidityGraph(humidity: Int) {
    val painter = rememberVectorPainter(image = Icons.Filled.WaterDrop)
    Spacer(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(12.dp)
            .drawWithCache {
                val width = size.width
                val height = size.height
                val archThickness = (width / 12f)
                val progress = humidity
                    .times(270)
                    .div(100)
                    .toFloat()
                onDrawBehind {
                    drawArc(
                        color = Color.Black.copy(alpha = 0.2f),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        style = Stroke(width = archThickness, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = Color.Blue.copy(green = 0.6f),
                        startAngle = 135f,
                        sweepAngle = progress,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        style = Stroke(width = archThickness, cap = StrokeCap.Round),
                    )
                    val size = Size(width / 3, height / 3)
                    translate(
                        left = (width / 2) - size.width / 2,
                        top = (height / 2) - size.height / 2
                    ) {
                        with(painter) {
                            draw(
                                size = size,
                                colorFilter = ColorFilter.tint(Color.Blue.copy(green = 0.6f))
                            )
                        }
                    }
                }
            }
    )
}

@Preview
@Composable
private fun HumidityPreview() {
    val color = MaterialTheme.colorScheme.background
    HumidityWidget(humidity = 50, surfaceColor = color)
}