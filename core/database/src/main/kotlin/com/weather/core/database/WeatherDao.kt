package com.weather.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.weather.core.database.entities.meteo.CurrentEntity
import com.weather.core.database.entities.meteo.DailyEntity
import com.weather.core.database.entities.meteo.HourlyEntity
import com.weather.core.database.entities.meteo.WeatherLocationEntity
import com.weather.core.database.entities.relation.CurrentWeatherWithDailyAndHourly
import com.weather.core.database.entities.relation.WeatherAndCurrent
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

	/**
	 * counts the number of locations stored, returns 0 if database is empty.
	 * also used for incrementing the value of order index of new location.
	 */
	@Query("SELECT count(cityName) FROM weather_location")
	fun countColumns(): Int

	/**
	 * meteo
	 */
	@Upsert
	suspend fun insertWeatherLocation(weatherLocation: WeatherLocationEntity)

	/**
	 * used to update list order
	 */
	@Upsert
	suspend fun insertWeatherLocations(oneCalls: List<WeatherLocationEntity>)

	@Upsert
	suspend fun insertCurrent(currentEntity: CurrentEntity)

	@Upsert
	suspend fun insertDaily(daily: List<DailyEntity>)

	@Upsert
	suspend fun insertHourly(hourly: List<HourlyEntity>)

	@Query("select * from weather_location where cityName= :cityName")
	fun getWeatherLocation(cityName: String): WeatherLocationEntity

	@Transaction
	@Query("select * from weather_location")
	fun getAllWeatherData(): Flow<List<CurrentWeatherWithDailyAndHourly>>

	@Transaction
	@Query("select * from weather_location")
	fun getWeatherLocations(): Flow<List<WeatherAndCurrent>>

	/**
	 * method to store data.
	 */

	@Query("SELECT count(*) FROM weather_location WHERE cityName = :cityName")
	fun checkIfCityExists(cityName: String): Int

	@Transaction
	@Query("SELECT * FROM weather_location")
	fun getAllWeatherAndCurrent(): Flow<List<WeatherAndCurrent>>

	//delete
	@Query("delete from weather_location where cityName= :cityName")
	fun deleteOneWeather(cityName: String)

	@Query("DELETE FROM weather_location WHERE cityName in (:cityName)")
	suspend fun deleteMultipleWeather(cityName: List<String>)

	/**
	 * Delete old data based on timeStamp of the network
//	 * */
	@Query("delete from daily where cityName = :cityName and time < :timeStamp")
	fun deleteDaily(cityName: String, timeStamp: String)

	/**
	 * Delete old data based on timeStamp of the network
	 * */
	@Query("delete from hourly where cityName = :cityName and time < :timeStamp")
	fun deleteHourly(cityName: String, timeStamp: String)

	/**
	 * NPE safe data insert
	 */
	@Transaction
	suspend fun insertData(
		weatherLocation: WeatherLocationEntity,
		current: CurrentEntity,
		daily: List<DailyEntity>,
		hourly: List<HourlyEntity>,
	) {
		insertWeatherLocation(weatherLocation)
		insertCurrent(current)
		insertDaily(daily)
		insertHourly(hourly)
	}

}