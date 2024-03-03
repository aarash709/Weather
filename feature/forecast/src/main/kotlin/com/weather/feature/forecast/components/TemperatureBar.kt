package com.weather.feature.forecast.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

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
    Canvas(modifier = Modifier.size(width = 80.dp, height = 5.dp)) {
        val gradient = Brush.horizontalGradient(
            listOf(
                tempColor(tempData.currentLow),
                tempColor(tempData.currentHigh)
            )
        )
        val width = size.width
        val height = size.height
        val backgroundColor = Color.Black.copy(alpha = 0.15f)
        val tempRange = tempData.maxTemp - tempData.minTemp
        val stepsInPixels = width / tempRange
        val leftIndent = (tempData.minTemp - tempData.currentLow).times(stepsInPixels).div(width)
        val rightIndent = width - (tempData.maxTemp - tempData.currentHigh).times(stepsInPixels)
        val currentTempCirclePosition = Offset(
            x = width - (tempData.maxTemp - tempData.currentTemp).times(stepsInPixels),
            y = height / 2
        )
        val strokeWidth = 5.dp.toPx()
        // background
        drawLine(
            color = backgroundColor,
            start = Offset(0f, height / 2),
            end = Offset(width, height / 2),
            cap = StrokeCap.Round,
            strokeWidth = strokeWidth
        )
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
                color = Color.White,
                radius = 4.dp.toPx(),
                center = currentTempCirclePosition,
            )
        }
    }
}