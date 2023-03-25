package com.eweather.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eweather.core.datastore.DataStoreKeys.EMPTY_STRING
import com.eweather.core.datastore.DataStoreKeys.FAVORITE_CITY_COORDINATE_STRING_KEY
import com.eweather.core.datastore.DataStoreKeys.TEMP_UNIT_SETTING_KEY
import com.eweather.core.datastore.DataStoreKeys.WIND_SPEED_UNIT_SETTING_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences
import javax.inject.Inject

class LocalUserPreferences @Inject constructor(private val datastore: DataStore<Preferences>) {

    fun getFavoriteCity(): Flow<String> {
        return datastore.data.map { preferences ->
            preferences.get(FAVORITE_CITY_COORDINATE_STRING_KEY.name, EMPTY_STRING)
        }
    }

    fun getTimeUnitSetting(): Flow<String> {
        return datastore.data.map { preferences ->
            preferences.get(TEMP_UNIT_SETTING_KEY.name, EMPTY_STRING)
        }
    }

    fun getWindSpeedUnitSetting(): Flow<String> {
        return datastore.data.map { preferences ->
            preferences.get(WIND_SPEED_UNIT_SETTING_KEY.name, EMPTY_STRING)
        }
    }

    suspend fun setFavoriteCity(value: String) {
        datastore.updateData { preferences ->
            preferences.apply {
                put(FAVORITE_CITY_COORDINATE_STRING_KEY.name, value)
            }
        }
    }

    suspend fun setTemperatureUnit(value: String) {
        datastore.updateData { preferences ->
            preferences.apply {
                put(TEMP_UNIT_SETTING_KEY.name, value)
            }
        }
    }

    suspend fun setWindSpeedUnit(value: String) {
        datastore.updateData { preferences ->
            preferences.apply {
                put(WIND_SPEED_UNIT_SETTING_KEY.name, value)
            }
        }
    }
}

object DataStoreKeys {
    const val EMPTY_STRING = ""
    val FAVORITE_CITY_COORDINATE_STRING_KEY = stringPreferencesKey("favoriteCityCoordinate")
    val TEMP_UNIT_SETTING_KEY = stringPreferencesKey("settingsTempUnit")
    val WIND_SPEED_UNIT_SETTING_KEY = stringPreferencesKey("settingsWindSpeed")
}
