package com.weather.core.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.weather.entities.geoSearch.GeoSearchItemEntity
import com.weather.entities.onecall.*
import com.weather.entities.relation.CurrentWithWeather
import com.weather.entities.relation.OneCallAndCurrent
import com.weather.entities.relation.OneCallWithCurrentAndDailyAndHourly
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select count(cityName) from one_call")
    fun databaseIsEmpty(): Int

    //ONE CALL
    @Insert(onConflict = REPLACE)
    suspend fun insertOneCall(oneCall: OneCallEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertOneCallCurrent(current: CurrentEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertOneCallCurrentWeather(weather: List<CurrentWeatherEntity>)

    @Upsert()
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
    fun getOneCallCurrentByCityName(cityName: String): Flow<CurrentEntity>

    @Query("select * from one_call_daily where cityName= :cityName")
    fun getDailyByCityName(cityName: String): Flow<List<DailyEntity>>

    @Query("select * from one_call_current order by cityName")
    fun getAllOneCallCurrent(): Flow<List<CurrentEntity>>

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
    @Query("select * from one_call")
    fun getAllOneCallWithCurrentAndDailyAndHourly(): Flow<List<OneCallWithCurrentAndDailyAndHourly>> //experimental

    @Transaction
    suspend fun insertData(
        oneCall: OneCallEntity,
        oneCallCurrent: CurrentEntity,
        currentWeatherList: List<CurrentWeatherEntity>,
        daily: List<DailyEntity>,
    ) {
        insertOneCall(oneCall = oneCall)
        insertOneCallCurrent(current = oneCallCurrent)
        insertOneCallCurrentWeather(weather = currentWeatherList)
        insertDaily(daily = daily)
    }


}