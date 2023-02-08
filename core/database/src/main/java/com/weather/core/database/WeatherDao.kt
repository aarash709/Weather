package com.weather.core.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.weather.entities.geoSearch.GeoSearchItemEntity
import com.weather.entities.onecall.*
import com.weather.entities.relation.CurrentWithWeather
import com.weather.entities.relation.OneCallAndCurrent
import com.weather.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface WeatherDao {
    @Query("select count(cityName) from one_call")
    fun databaseIsEmpty(): Int

    //ONE CALL
    @Insert(onConflict = REPLACE)
    suspend fun insertOneCall(oneCall: OneCallEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertOneCallCurrent(current: OneCallCurrentEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertOneCallCurrentWeather(weather: List<OneCallWeatherEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun insertDaily(daily: List<DailyEntity>)

//    @Insert(onConflict = REPLACE)
//    suspend fun insertOneCallMinutely(minutely: List<OneCallMinutelyEntity>)

//    @Insert(onConflict = REPLACE)
//    suspend fun insertOneCallHourly(oneCallHourly: List<OneCallHourlyEntity>)

    //new
    @Transaction
    @Query("SELECT * FROM one_call WHERE cityName = :cityName")
    fun getOneCallAndCurrentByCityName(cityName: String): Flow<OneCallAndCurrent>

    @Transaction
    @Query("SELECT * FROM one_call_current WHERE cityName = :cityName")
    fun getCurrentWithWeatherByCityName(cityName: String): Flow<CurrentWithWeather>

//    @Transaction
//    @Query("SELECT * FROM one_call WHERE cityName = :cityName")
//    fun getOneCallAndMinutelyByCityName(cityName: String): Flow<OneCallWithMinutely>

    @Transaction
    @Query("SELECT * FROM one_call")
    fun getAllOneCallAndCurrent(): Flow<List<OneCallAndCurrent>>

//    @Transaction
//    @Query("SELECT * FROM one_call_current")
//    fun getAllCurrentWithWeather(): Flow<List<CurrentWithWeather>>


    //delete
    @Query("DELETE FROM one_call WHERE cityName = :cityName")
    suspend fun deleteOneCallByCityName(cityName: String)

    @Query("DELETE FROM one_call_current WHERE cityName = :cityName")
    suspend fun deleteOneCallCurrentByCityName(cityName: String)

    @Query("DELETE FROM current_weather WHERE cityName = :cityName")
    suspend fun deleteCurrentWeatherByCityName(cityName: String)

    @Query("DELETE FROM one_call_minutely WHERE cityName = :cityName")
    suspend fun deleteMinutelyByCityName(cityName: String)

    //

    @Query("select * from one_call where cityName= :cityName")
    fun getOneCallByCityName(cityName: String): Flow<OneCallEntity>

    @Query("select * from one_call_current where cityName= :cityName")
    fun getOneCallCurrentByCityName(cityName: String): Flow<OneCallCurrentEntity>

    @Query("select * from one_call_daily where cityName= :cityName")
    fun getDailyByCityName(cityName: String): Flow<List<DailyEntity>>

    @Query("select * from one_call_current order by cityName")
    fun getAllOneCallCurrent(): Flow<List<OneCallCurrentEntity>>

    @Query("select * from one_call order by cityName")
    fun getAllOneCall(): LiveData<List<OneCallEntity>>

    @Query("select * from one_call_daily order by cityName")
    fun getAllOneCallDaily(): LiveData<List<DailyEntity>>

    @Query("select * from one_call_hourly order by cityName")
    fun getAllOneCallHourly(): LiveData<List<OneCallHourlyEntity>>


    // geo
    @Insert(onConflict = REPLACE)
    suspend fun insertGeoSearch(geoSearch: GeoSearchItemEntity)

    @Transaction
    suspend fun insertData(
        oneCall: OneCallEntity,
        oneCallCurrent: OneCallCurrentEntity,
        currentWeatherList: List<OneCallWeatherEntity>,
        daily: List<DailyEntity>,
    ) {
        insertOneCall(oneCall = oneCall)
        insertOneCallCurrent(current = oneCallCurrent)
        insertOneCallCurrentWeather(weather = currentWeatherList)
        insertDaily(daily = daily)
    }

//    @Transaction
//    fun getWeatherDataByCityName(
//        cityName: String,
//    ): Flow<WeatherData> =
//        combine(
//            getOneCallAndCurrentByCityName(cityName = cityName),
//            getCurrentWithWeatherByCityName(cityName = cityName),
//            getDailyByCityName(cityName = cityName)
//        ) { oneCallAndCurrent, currentWeather, daily ->
//            WeatherData(
//                coordinates = oneCallAndCurrent.oneCall.asDomainModel(),
//                current = currentWeather.current.asDomainModel(
//                    weather = currentWeather.weather.map {
//                        it.asDomainModel()
//                    }
//                ),
//                daily = daily.map {
//                    it.asDomainModel()
//                }
//            )
//        }

}