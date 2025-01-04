package com.weather.core.network.model.meteoweahter

import kotlinx.serialization.SerialName

data class NetworkHourly(
	@SerialName("elevation")
	val elevation: Double?,
	@SerialName("generationtime_ms")
	val generationTimeMs: Double?,
	@SerialName("latitude")
	val latitude: Double?,
	@SerialName("longitude")
	val longitude: Double?,
	@SerialName("timezone")
	val timezone: String?,
	@SerialName("timezone_abbreviation")
	val timezoneAbbreviation: String?,
	@SerialName("utc_offset_seconds")
	val utcOffsetSeconds: Int?,
	@SerialName("daily")
	val daily: Daily,
	@SerialName("hourly_units")
	val hourlyUnits: HourlyUnits,
)
