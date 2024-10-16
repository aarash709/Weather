package com.weather.core.database

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.weather.core.database.entities.geoSearch.GeoSearchItemEntity
import com.weather.core.database.entities.onecall.*
import com.weather.core.database.entities.relation.CurrentWithWeather
import com.weather.core.database.entities.relation.OneCallAndCurrent
import com.weather.core.database.entities.relation.OneCallWithCurrentAndDailyAndHourly
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    /**
     * checking if database has at least one data row, otherwise returns 0(counts rows).
     */
    @Query("SELECT count(cityName) FROM one_call")
    fun countOneCall(): Int

    /**
     * method to store data.
     */
    @Upsert
    suspend fun insertOneCall(oneCall: OneCallEntity)

    @Upsert
    suspend fun insertOneCalls(oneCalls: List<OneCallEntity>)

    @Upsert
    suspend fun insertOneCallCurrent(current: CurrentEntity)

    @Upsert
    suspend fun insertOneCallCurrentWeather(weather: List<CurrentWeatherEntity>)

    @Upsert
    suspend fun insertDaily(daily: List<DailyEntity>)

    @Upsert
    suspend fun insertHourly(hourly: List<OneCallHourlyEntity>)

    @Query("SELECT count(*) FROM one_call WHERE cityName = :cityName")
    fun checkIfCityExists(cityName: String): Int

    @Query("SELECT * FROM one_call")
    fun getAllOneCall(): List<OneCallEntity>

    @Query("SELECT * FROM one_call WHERE cityName = :cityName")
    fun getOneCallByCityName(cityName: String): OneCallEntity

    @Transaction
    @Query("SELECT * FROM one_call WHERE cityName = :cityName")
    fun getOneCallAndCurrentByCityName(cityName: String): Flow<OneCallAndCurrent>

    @Transaction
    @Query("SELECT * FROM one_call_current WHERE cityName = :cityName")
    fun getCurrentWithWeatherByCityName(cityName: String): Flow<CurrentWithWeather>

    @Transaction
    @Query("SELECT * FROM one_call")
    fun getAllOneCallAndCurrent(): Flow<List<OneCallAndCurrent>>

    //delete
    @Query("DELETE FROM one_call WHERE cityName in (:cityName)")
    suspend fun deleteWeatherByCityName(cityName: List<String>)

    @Query("select * from one_call_daily where cityName= :cityName")
    fun getDailyByCityName(cityName: String): Flow<List<DailyEntity>>

    // geo
    @Insert(onConflict = REPLACE)
    suspend fun insertGeoSearch(geoSearch: GeoSearchItemEntity)

    @Transaction
    @Query("select * from one_call")
    fun getAllOneCallWithCurrentAndDailyAndHourly(): Flow<List<OneCallWithCurrentAndDailyAndHourly>> //experimental

    /**
     * Delete old data based on timeStamp of the network
     * */
    @Query("delete from one_call_daily where cityName = :cityName and dt < :timeStamp")
    fun deleteDaily(cityName: String, timeStamp: Long)

    /**
     * Delete old data based on timeStamp of the network
     * */
    @Query("delete from one_call_hourly where cityName = :cityName and dt < :timeStamp")
    fun deleteHourly(cityName: String, timeStamp: Int)

    /**
     * insert data atomic, so we don`t get NPE.
     */
    @Transaction
    suspend fun insertData(
        oneCall: OneCallEntity,
        oneCallCurrent: CurrentEntity,
        currentWeatherList: List<CurrentWeatherEntity>,
        daily: List<DailyEntity>,
        hourly: List<OneCallHourlyEntity>,
    ) {
        insertOneCall(oneCall = oneCall)
        insertOneCallCurrent(current = oneCallCurrent)
        insertOneCallCurrentWeather(weather = currentWeatherList)
        insertDaily(daily = daily)
        insertHourly(hourly = hourly)
    }

    @Transaction
    suspend fun reorderOneCallWeatherData(
        from: OneCallEntity,
        to: OneCallEntity,
        fromIndex: Int,
        toIndex: Int,
    ) {
        insertOneCall(from.copy(orderIndex = toIndex))
        insertOneCall(to.copy(orderIndex = fromIndex))
    }

}