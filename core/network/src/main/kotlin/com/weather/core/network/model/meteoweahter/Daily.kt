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
	@SerialName("sunrise")
	val sunrise: List<String>,
	@SerialName("sunset")
	val sunset: List<String>,
	@SerialName("uv_index_max")
	val uvIndex: List<Double>,
	@SerialName("weather_code")
	val weatherCode: List<Int>
)