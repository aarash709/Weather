package com.experiment.weather.core.common.extentions

import com.weather.model.Current
import com.weather.model.Daily
import com.weather.model.Hourly
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Current.convertToUserSettings(
    temperature: TemperatureUnits?,
    windSpeed: WindSpeedUnits?,
): Current {
    return this.let {
        it.copy(
            dew_point = it.dew_point.convertToUserTemperature(
                temperature
            ),
            feels_like = it.feels_like.convertToUserTemperature(
                temperature
            ),
            temp = it.temp.convertToUserTemperature(
                temperature
            ),
            visibility = it.visibility,
            wind_speed = it.wind_speed.convertToUserSpeed(
                windSpeed
            ),
        )
    }
}

fun Daily.convertToUserSettings(temperature: TemperatureUnits?): Daily {
    return this.let {
        it.copy(
            dew_point = it.dew_point.convertToUserTemperature(
                temperature
            ),
            dt = unixMillisToHumanDate(it.dt.toLong(), "EEE"),
            dayTemp = it.dayTemp.convertToUserTemperature(
                temperature
            ),
            nightTemp = it.nightTemp.convertToUserTemperature(
                temperature
            )
        )
    }
}
fun Hourly.convertToUserSettings(temperature: TemperatureUnits?): Hourly {
    return this.let {
        it.copy(
            dew_point = it.dew_point.convertToUserTemperature(
                temperature
            ),
            dt = unixMillisToHumanDate(it.dt.toLong(), "HH:mm"),
            temp = it.temp.convertToUserTemperature(
                temperature
            )
        )
    }
}


private fun unixMillisToHumanDate(unixTimeStamp: Long, pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date(unixTimeStamp * 1000) //to millisecond
    return formatter.format(date)
}