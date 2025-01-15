package com.experiment.weather.core.common.extentions

import com.weather.model.TemperatureUnits
import com.weather.model.TemperatureUnits.*
import com.weather.model.WindSpeedUnits
import com.weather.model.WindSpeedUnits.*
import kotlin.math.roundToInt

fun Double.applySettingsTemperatureUnit(
	userTempUnit: TemperatureUnits?,
): Double {
	return when (userTempUnit) {
		C, null -> this
		F -> this.times(1.8f).plus(32)
	}
}

fun Double.convertToUserSpeed(
	userTempUnit: WindSpeedUnits?,
): Double {
	return when (userTempUnit) {
		KM -> this.times(3.6f).times(100).roundToInt().toDouble().div(100)
		MS -> this
		MPH -> this.times(2.2369f).times(100).roundToInt().toDouble().div(100)
		null -> {
			this.times(3.6f).times(100).roundToInt().toDouble().div(100)
		}
	}
}