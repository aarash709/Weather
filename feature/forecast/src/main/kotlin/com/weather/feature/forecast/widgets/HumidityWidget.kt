package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.WeatherSquareWidget

@Composable
internal fun HumidityWidget(
    humidity: Int,
    modifier: Modifier = Modifier,
) {
    WeatherSquareWidget(
        modifier = modifier,
        icon = Icons.Outlined.WaterDrop, title = "Humidity"
    ) {
        HumidityGraph(humidity = humidity)
        Text(text = "$humidity%", fontSize = 24.sp)
    }
}

@Composable
fun HumidityGraph(humidity: Int) {
    Spacer(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(16.dp)
            .drawWithCache {
                val archThickness = 6.dp.toPx()
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
                        color = Color.Blue.copy(green = 0.5f),
                        startAngle = 135f,
                        sweepAngle = progress,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        style = Stroke(width = archThickness, cap = StrokeCap.Round),
                    )

                }
            }
    )
}

@Preview
@Composable
private fun HumidityPreview() {
    HumidityWidget(humidity = 50)
}