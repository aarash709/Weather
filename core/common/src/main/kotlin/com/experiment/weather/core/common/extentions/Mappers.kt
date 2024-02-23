package com.experiment.weather.core.common.extentions

import arrow.optics.copy
import com.weather.model.Current
import com.weather.model.Daily
import com.weather.model.Hourly
import com.weather.model.ManageLocationsData
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.WeatherData
import com.weather.model.WindSpeedUnits
import com.weather.model.current
import com.weather.model.currentTemp
import com.weather.model.daily
import com.weather.model.dayTemp
import com.weather.model.dew_point
import com.weather.model.dt
import com.weather.model.feelsLike
import com.weather.model.feels_like
import com.weather.model.hourly
import com.weather.model.isFavorite
import com.weather.model.locationName
import com.weather.model.nightTemp
import com.weather.model.temp
import com.weather.model.time
import com.weather.model.wind_speed
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun Current.convertToUserSettings(
    temperature: TemperatureUnits?,
    windSpeed: WindSpeedUnits?,
): Current {
    return copy {
        Current.dew_point transform { it.convertToUserTemperature(temperature) }
        Current.feels_like transform { it.convertToUserTemperature(temperature) }
        Current.currentTemp transform { it.convertToUserTemperature(temperature) }
        Current.wind_speed transform { it.convertToUserSpeed(windSpeed) }
    }

}

@JvmName("convertListDailyToUserSettings")
fun List<Daily>.convertToUserSettings(temperature: TemperatureUnits): List<Daily> {
    return map { daily ->
        daily.copy {
            Daily.dew_point transform { dewPoint -> dewPoint.convertToUserTemperature(temperature) }
            Daily.dt transform { time -> calculateUIDailyTime(time.toLong()) }
            Daily.dayTemp transform { dayTemp -> dayTemp.convertToUserTemperature(temperature) }
            Daily.nightTemp.transform { nightTemp -> nightTemp.convertToUserTemperature(temperature) }
        }
    }
}

@JvmName("convertListHourlyToUserSettings")
fun List<Hourly>.convertToUserSettings(
    temperature: TemperatureUnits,
    windSpeed: WindSpeedUnits,
): List<Hourly> {
    return map { hourly ->
        hourly.copy {
            Hourly.dew_point transform { dewPoint ->
                dewPoint.convertToUserTemperature(
                    temperature
                )
            }
            Hourly.time transform { timeInSeconds -> calculateUIHourlyTime(timeInSeconds.toLong()) }
            Hourly.temp transform { it.convertToUserTemperature(temperature) }
            Hourly.wind_speed transform { it.convertToUserSpeed(windSpeed) }
        }
    }
}

fun WeatherData.convertToUserSettings(userSettings: SettingsData): WeatherData {
    val tempUnit = userSettings.temperatureUnits
    val windUnit = userSettings.windSpeedUnits
    return copy {
        WeatherData.current transform {
            it.convertToUserSettings(
                tempUnit,
                windUnit
            )
        }
        WeatherData.daily transform {
            it.convertToUserSettings(tempUnit!!)
        }
        WeatherData.hourly transform { it.convertToUserSettings(tempUnit!!, windUnit!!) }

    }
}

fun List<ManageLocationsData>.convertTotoUserSettings(
    tempUnit: TemperatureUnits,
    favoriteCityName: String?,
): List<ManageLocationsData> {
    return map { manageLocations ->
        manageLocations.copy {
            val isFavorite = ManageLocationsData
                .locationName
                .get(manageLocations) == favoriteCityName
            ManageLocationsData.currentTemp transform {
                it.toDouble().convertToUserTemperature(userTempUnit = tempUnit).roundToInt()
                    .toString()
            }
            ManageLocationsData.feelsLike transform {
                it.toDouble().convertToUserTemperature(userTempUnit = tempUnit)
                    .roundToInt().toString()
            }
            ManageLocationsData.isFavorite set isFavorite
        }
    }
}

private fun unixMillisToHumanDate(unixTimeStamp: Long, pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date(unixTimeStamp * 1000) //to millisecond
    return formatter.format(date)
}

private fun calculateUIHourlyTime(hourlyTimeSeconds: Long): String {
    val differenceInMinutes =
        Duration.ofSeconds(hourlyTimeSeconds.minus(Instant.now().epochSecond)).toMinutes()
    return if (differenceInMinutes in hourlyRangeThreshold)
        NOW
    else
        unixMillisToHumanDate(hourlyTimeSeconds, HOURLY_PATTERN)
}

private fun calculateUIDailyTime(hourlyTimeSeconds: Long): String {
    val dayOfWeekOfDailyData = unixMillisToHumanDate(hourlyTimeSeconds, DAILY_PATTERN)
    val localToday = LocalDateTime.now().dayOfWeek.name
    val localTomorrow = LocalDateTime.now().plusDays(1).dayOfWeek.name
    return when (dayOfWeekOfDailyData.uppercase()) {
        localToday -> TODAY
        localTomorrow -> TOMORROW
        else -> dayOfWeekOfDailyData
    }
}