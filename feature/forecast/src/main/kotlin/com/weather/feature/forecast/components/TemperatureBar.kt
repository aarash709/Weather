package com.weather.feature.forecast.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.feature.forecast.tempColor
import com.weather.model.TemperatureUnits
import kotlin.math.absoluteValue

@Stable
internal data class TempData(
    val tempUnit: TemperatureUnits,
    val minTemp: Int,
    val maxTemp: Int,
    val currentLow: Int,
    val currentHigh: Int,
    val shouldShowCurrentTemp: Boolean = false,
    val currentTemp: Int,
)

@Composable
internal fun TempBar(tempData: TempData) {
    val indicatorColor = LocalContentColor.current
    Spacer(modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(Color.Black.copy(alpha = 0.2f))
        .size(width = 80.dp, height = 6.dp)
        .graphicsLayer {
            // should be set to `CompositingStrategy.Offscreen` when
            // using blend modes for transparency in indicators
            //otherwise transparency with BlendMode.Clear will render black color
            compositingStrategy = CompositingStrategy.Offscreen
        }
        .drawWithCache {
            onDrawBehind {
                val gradient = Brush.horizontalGradient(
                    listOf(
                        tempColor(tempData.currentLow, tempData.tempUnit),
                        tempColor(tempData.currentHigh, tempData.tempUnit)
                    )
                )
                val width = size.width
                val height = size.height
                val indicatorSize = height * .7f
                val tempRange = tempData.maxTemp - tempData.minTemp
                val stepsInPixels = width / tempRange
                val leftIndent = (tempData.currentLow - tempData.minTemp).times(stepsInPixels)
                val rightIndent =
                    width - (tempData.currentHigh - tempData.maxTemp).times(stepsInPixels).absoluteValue
                val currentTempCirclePosition = Offset(
                    x = width - (tempData.currentTemp - tempData.maxTemp).times(stepsInPixels).absoluteValue,
                    y = height / 2
                )
                val strokeWidth = 5.dp.toPx()
                //temp bar
                drawLine(
                    brush = gradient,
                    start = Offset(leftIndent, height / 2),
                    end = Offset(rightIndent, height / 2),
                    cap = StrokeCap.Round,
                    strokeWidth = strokeWidth
                )
                //current temp indicator on first item
                if (tempData.shouldShowCurrentTemp) {
                    drawCircle(
                        color = Color.Black,
                        radius = indicatorSize,
                        center = currentTempCirclePosition,
                        blendMode = BlendMode.Clear
                    )
                    drawCircle(
                        color = indicatorColor,
                        radius = indicatorSize * 0.6f,
                        center = currentTempCirclePosition,
                    )
                }
            }
        })
}

@Preview
@Composable
private fun BarPreview() {
    TempBar(
        tempData = TempData(
            tempUnit = TemperatureUnits.F,
            minTemp = -1,
            maxTemp = 10,
            currentLow = 0,
            currentHigh = 8,
            shouldShowCurrentTemp = true,
            currentTemp = 3
        )
    )
}