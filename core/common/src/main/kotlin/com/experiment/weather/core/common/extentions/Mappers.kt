package com.experiment.weather.core.common.extentions

import android.util.Log
import arrow.optics.copy
import com.weather.model.Coordinate
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
import com.weather.model.wind_speed
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

fun Current.convertToUserSettings(
    temperature: TemperatureUnits?,
    windSpeed: WindSpeedUnits?,
): Current {
    return copy {
        Current.dew_point transform { it.convertToUserTemperature(temperature) }
        Current.feels_like transform { it.convertToUserTemperature(temperature) }
        Current.temp transform { it.convertToUserTemperature(temperature) }
        Current.wind_speed transform { it.convertToUserSpeed(windSpeed) }
    }

}

@JvmName("convertListDailyToUserSettings")
fun List<Daily>.convertToUserSettings(temperature: TemperatureUnits?): List<Daily> {
    return map { daily ->
        daily.copy {
            Daily.dew_point transform { dewPoint -> dewPoint.convertToUserTemperature(temperature) }
            Daily.dt transform { dt -> unixMillisToHumanDate(dt.toLong(), "EEE") }
            Daily.dayTemp transform { dayTemp -> dayTemp.convertToUserTemperature(temperature) }
            Daily.nightTemp.transform { nightTemp -> nightTemp.convertToUserTemperature(temperature) }
        }
    }
}

@JvmName("convertListHourlyToUserSettings")
fun List<Hourly>.convertToUserSettings(temperature: TemperatureUnits?): List<Hourly> {
    return map { hourly ->
        hourly.copy {
            Hourly.dew_point transform { dewPoint ->
                dewPoint.convertToUserTemperature(
                    temperature
                )
            }
            Hourly.dt transform { dt ->
                val timeMillis = dt.toLong()
                val diffrence = Duration.ofSeconds(timeMillis.minus(Instant.now().epochSecond)).toMinutes()
                Log.e("mapper",diffrence.toString())
                if (diffrence in 0 .. 60)
                    "Now"
                else
                    unixMillisToHumanDate(timeMillis, "HH:mm")
            }
            Hourly.temp transform { temp -> temp.convertToUserTemperature(temperature) }
        }
    }
}

fun WeatherData.convertToUserSettings(userSettings: SettingsData): WeatherData {
    return copy {
        WeatherData.current transform {
            it.convertToUserSettings(
                userSettings.temperatureUnits,
                userSettings.windSpeedUnits
            )
        }
        WeatherData.daily transform {

            it.convertToUserSettings(userSettings.temperatureUnits)


        }
        WeatherData.hourly transform {
            it.convertToUserSettings(userSettings.temperatureUnits)
        }

    }
}

fun List<ManageLocationsData>.convertTotoUserSettings(
    tempUnit: TemperatureUnits,
    favoriteCoordinate: Coordinate?,
): List<ManageLocationsData> {
    return map { manageLocations ->
        manageLocations.copy {
            val isFavorite = ManageLocationsData
                .locationName
                .get(manageLocations) == favoriteCoordinate?.cityName
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