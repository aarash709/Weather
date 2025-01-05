package com.weather.core.network.model.meteoweahter


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Daily(
	@SerialName("precipitation_sum")
	val precipitationSum: List<Double>,
	@SerialName("temperature_2m_max")
	val temperature2mMax: List<Double>,
	@SerialName("temperature_2m_min")
	val temperature2mMin: List<Double>,
	@SerialName("time")
	val time: List<String>,
	@SerialName("weather_code")
	val weatherCode: List<Int>
)