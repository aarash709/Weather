package com.weather.feature.forecast.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import timber.log.Timber
import kotlin.math.absoluteValue

@Stable
internal data class TempData(
    val minTemp: Int,
    val maxTemp: Int,
    val currentLow: Int,
    val currentHigh: Int,
    val shouldShowCurrentTemp: Boolean = false,
    val currentTemp: Int,
)

/**
 * Calculated colors for temperature range in temp bar for daily
 * Support for °C only for now
 **/
internal fun tempColor(temp: Int): Color {
    return when {
        temp <= 0 -> Color(25, 165, 221, 255)
        temp in 1..15 -> Color(25, 205, 221, 255)
        temp in 16..19 -> Color(67, 221, 25, 255)
        temp in 20..24 -> Color(218, 215, 19, 255)
        temp in 25..29 -> Color(255, 150, 21, 255) //Orange
        temp in 30..70 -> Color(238, 68, 26, 255)
        else -> Color.White

    }
}

@Composable
internal fun TempBar(tempData: TempData) {
    Spacer(modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(Color.Black.copy(alpha = 0.2f))
        .size(width = 80.dp, height = 5.dp)
        .graphicsLayer {
            // should be set to `CompositingStrategy.Offscreen` when
            // using blend modes for transparency in indicators
            //otherwise transparency with BlendMode.Clear will render black color
//            compositingStrategy = CompositingStrategy.Offscreen
            compositingStrategy = CompositingStrategy.Offscreen
        }
        .drawWithCache {
            onDrawBehind {
                val gradient = Brush.horizontalGradient(
                    listOf(
                        tempColor(tempData.currentLow),
                        tempColor(tempData.currentHigh)
                    )
                )
                val width = size.width
                val height = size.height
                val indicatorSize = 5.dp.toPx()
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
                        color = Color.White,
                        radius = indicatorSize * 0.7f,
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
            minTemp = -1,
            maxTemp = 10,
            currentLow = 0,
            currentHigh = 8,
            shouldShowCurrentTemp = true,
            currentTemp = 3
        )
    )
}