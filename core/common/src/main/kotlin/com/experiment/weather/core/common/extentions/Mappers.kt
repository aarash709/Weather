package com.experiment.weather.core.common.extentions

import arrow.optics.copy
import com.weather.model.Current
import com.weather.model.Daily
import com.weather.model.DailyPreview
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
import com.weather.model.feelsLike
import com.weather.model.hourly
import com.weather.model.isFavorite
import com.weather.model.locationName
import com.weather.model.nightTemp
import com.weather.model.temp
import com.weather.model.time
import com.weather.model.windSpeed
import kotlin.math.roundToInt

fun WeatherData.applySettings(userSettings: SettingsData): WeatherData {
	val tempUnit = userSettings.temperatureUnits
	val windUnit = userSettings.windSpeedUnits
	val timeOffset = coordinates.timezoneOffset.toLong()
	return copy {
		WeatherData.current transform {
			it.applySettings(
				tempUnit,
				windUnit
			)
		}
		WeatherData.daily transform {
			it.applySettings(tempUnit)
		}
		WeatherData.hourly transform { it.applySettings(tempUnit, windUnit, timeOffset) }

	}
}

fun Current.applySettings(
	temperatureUnit: TemperatureUnits?,
	windSpeedUnit: WindSpeedUnits?,
): Current {
	return copy {
		Current.feelsLike transform { it.applySettingsTemperatureUnit(temperatureUnit) }
		Current.currentTemp transform { it.applySettingsTemperatureUnit(temperatureUnit) }
		Current.windSpeed transform { it.convertToUserSpeed(windSpeedUnit) }
	}

}

@JvmName("convertListDailyToUserSettings")
fun List<Daily>.applySettings(temperature: TemperatureUnits): List<Daily> {
	return map { daily ->
		daily.copy {
			Daily.time transform { isoDate -> calculateUIDailyTime(isoDate) }
			Daily.dayTemp transform { dayTemp -> dayTemp.applySettingsTemperatureUnit(temperature) }
			Daily.nightTemp.transform { nightTemp -> nightTemp.applySettingsTemperatureUnit(temperature) }
		}
	}
}

@JvmName("convertListHourlyToUserSettings")
fun List<Hourly>.applySettings(
	temperature: TemperatureUnits,
	windSpeed: WindSpeedUnits,
	timeOffset: Long,
): List<Hourly> {
	return map { hourly ->
		hourly.copy {
			Hourly.time transform { isoDateTime ->
				calculateUIHourlyTime(
					isoTime = isoDateTime,
					offsetSeconds = timeOffset
				)
			}
			Hourly.temp transform { it.applySettingsTemperatureUnit(temperature) }
			Hourly.windSpeed transform { it.convertToUserSpeed(windSpeed) }
		}
	}
}

fun List<ManageLocationsData>.applySettings(
	tempUnit: TemperatureUnits,
	favoriteCityName: String?,
): List<ManageLocationsData> {
	return map { manageLocations ->
		manageLocations.copy {
			val isFavorite = ManageLocationsData
				.locationName
				.get(manageLocations) == favoriteCityName
			ManageLocationsData.currentTemp transform {
				it.toDouble().applySettingsTemperatureUnit(userTempUnit = tempUnit).roundToInt()
					.toString()
			}
			ManageLocationsData.feelsLike transform {
				it.toDouble().applySettingsTemperatureUnit(userTempUnit = tempUnit)
					.roundToInt().toString()
			}
			ManageLocationsData.isFavorite set isFavorite
		}
	}
}

fun List<DailyPreview>.convertTimeAndTemperature(): List<DailyPreview> {
	return map { dailyPreview ->
		dailyPreview.copy(
			time = calculateUIDailyTime(dailyPreview.time),
			tempDay = dailyPreview.tempDay.minus(273.15).roundToInt(),
			tempNight = dailyPreview.tempNight.minus(273.15).roundToInt()
		)
	}
}
