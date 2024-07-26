package com.weather.feature.forecast

import androidx.compose.ui.graphics.Color
import com.weather.model.TemperatureUnits
import kotlin.math.roundToInt


/**
 * Calculated colors for temperature range in temp bar for daily
 * Support for °C only for now
 **/
internal fun tempColor(temp: Int, tempUnit: TemperatureUnits): Color {
    val tempOffset =
        if (tempUnit == TemperatureUnits.F) ((temp - 32) * 0.555).roundToInt() else temp

    return when {
        tempOffset <= 0 -> Color(25, 165, 221, 255)
        tempOffset in 1..15 -> Color(25, 205, 221, 255)
        tempOffset in 16..19 -> Color(67, 221, 25, 255)
        tempOffset in 20..24 -> Color(218, 215, 19, 255)
        tempOffset in 25..29 -> Color(255, 150, 21, 255) //Orange
        tempOffset in 30..70 -> Color(238, 68, 26, 255)
        else -> Color.White

    }
}