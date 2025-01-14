package com.weather.core.database

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.weather.core.database.entities.geoSearch.GeoSearchItemEntity
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
//	@Upsert
//	suspend fun insertOneCall(oneCall: OneCallEntity)

//	@Upsert
//	suspend fun insertOneCalls(oneCalls: List<OneCallEntity>)

//	@Upsert
//	suspend fun insertOneCallCurrent(current: com.weather.core.database.entities.onecall.CurrentEntity)
//
//	@Upsert
//	suspend fun insertOneCallCurrentWeather(weather: List<CurrentWeatherEntity>)
//
//	@Upsert
//	suspend fun insertDaily(daily: List<com.weather.core.database.entities.onecall.DailyEntity>)
//
//	@Upsert
//	suspend fun insertHourly(hourly: List<OneCallHourlyEntity>)

	@Query("SELECT count(*) FROM weather_location WHERE cityName = :cityName")
	fun checkIfCityExists(cityName: String): Int

//	@Query("SELECT * FROM one_call")
//	fun getAllOneCall(): List<OneCallEntity>
//
//	@Query("SELECT * FROM one_call WHERE cityName = :cityName")
//	fun getOneCallByCityName(cityName: String): OneCallEntity
//
//	@Transaction
//	@Query("SELECT * FROM one_call WHERE cityName = :cityName")
//	fun getOneCallAndCurrentByCityName(cityName: String): Flow<OneCallAndCurrent>
//
//	@Transaction
//	@Query("SELECT * FROM one_call_current WHERE cityName = :cityName")
//	fun getCurrentWithWeatherByCityName(cityName: String): Flow<CurrentWithWeather>
//
	@Transaction
	@Query("SELECT * FROM weather_location")
	fun getAllWeatherAndCurrent(): Flow<List<WeatherAndCurrent>>

	//delete

	@Query("delete from weather_location where cityName= :cityName")
	fun deleteOneWeather(cityName: String)

	@Query("DELETE FROM weather_location WHERE cityName in (:cityName)")
	suspend fun deleteMultipleWeather(cityName: List<String>)
//
//	@Query("select * from one_call_daily where cityName= :cityName")
//	fun getDailyByCityName(cityName: String): Flow<List<DailyEntity>>

	// geo
	@Insert(onConflict = REPLACE)
	suspend fun insertGeoSearch(geoSearch: GeoSearchItemEntity)

//	@Transaction
//	@Query("select * from one_call")
//	fun getAllOneCallWithCurrentAndDailyAndHourly(): Flow<List<OneCallWithCurrentAndDailyAndHourly>> //experimental

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

//	@Transaction
//	suspend fun reorderOneCallWeatherData(
//		from: OneCallEntity,
//		to: OneCallEntity,
//		fromIndex: Int,
//		toIndex: Int,
//	) {
//		insertOneCall(from.copy(orderIndex = toIndex))
//		insertOneCall(to.copy(orderIndex = fromIndex))
//	}

}