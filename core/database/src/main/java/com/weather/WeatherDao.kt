package com.weather

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.weather.entities.geoSearch.GeoSearchItemEntity
import com.weather.entities.onecall.*
import com.weather.entities.relation.CurrentWithWeather
import com.weather.entities.relation.OneCallAndCurrent
import com.weather.entities.relation.OneCallWithMinutely
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    //database check
    @Query("select count(cityName) from one_call")
    fun databaseIsEmpty(): Int

    //ONE CALL
    @Insert(onConflict = REPLACE)
    suspend fun insertOneCall(oneCall: OneCallEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertOneCallCurrent(oneCallCurrent: OneCallCurrentEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertOneCallMinutely(minutely: List<OneCallMinutelyEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun insertOneCallCurrentWeather(weather: List<OneCallWeatherEntity>)

//    @Insert(onConflict = REPLACE)
//    suspend fun insertOneCallDaily(oneCallCallDaily: List<OneCallDailyEntity>)
//
//    @Insert(onConflict = REPLACE)
//    suspend fun insertOneCallHourly(oneCallHourly: List<OneCallHourlyEntity>)

    //new
    @Transaction
    @Query("SELECT * FROM one_call WHERE cityName = :cityName")
    fun getOneCallAndCurrentByCityName(cityName: String): Flow<OneCallAndCurrent>

    @Transaction
    @Query("SELECT * FROM one_call_current WHERE cityName = :cityName")
    fun getOneCallCurrentWithWeatherByCityName(cityName: String): Flow<CurrentWithWeather>

    @Transaction
    @Query("SELECT * FROM one_call WHERE cityName = :cityName")
    fun getOneCallAndMinutelyByCityName(cityName: String): Flow<OneCallWithMinutely>

    @Transaction
    @Query("SELECT * FROM one_call")
    fun getAllOneCallAndCurrent(): Flow<List<OneCallAndCurrent>>

    @Transaction
    @Query("SELECT * FROM one_call_current")
    fun getAllCurrentWithWeather(): Flow<List<CurrentWithWeather>>


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

    @Query("select * from one_call order by cityName")
    fun getAllOneCall(): LiveData<List<OneCallEntity>>

    @Query("select * from one_call_current order by cityName")
    fun getAllOneCallCurrent(): Flow<List<OneCallCurrentEntity>>

    @Query("select * from one_call_daily order by cityName")
    fun getAllOneCallDaily(): LiveData<List<OneCallDailyEntity>>

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
    ){
        insertOneCall(oneCall)
        insertOneCallCurrent(oneCallCurrent)
        insertOneCallCurrentWeather(currentWeatherList)
    }
}