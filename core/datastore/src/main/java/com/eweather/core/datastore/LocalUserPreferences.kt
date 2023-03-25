package com.eweather.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eweather.core.datastore.DataStoreKeys.FAVORITE_CITY_COORDINATE_STRING_KEY
import com.eweather.core.datastore.DataStoreKeys.TEMP_UNIT_SETTING_KEY
import com.eweather.core.datastore.DataStoreKeys.WIND_SPEED_UNIT_SETTING_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserPreferences @Inject constructor(
    private val datastore: DataStore<Preferences>,
) {

    fun getFavoriteCity(): Flow<String?> {
        return datastore.data.map { preferences ->
            preferences[FAVORITE_CITY_COORDINATE_STRING_KEY]
        }
    }

    fun getTimeUnitSetting(): Flow<String?> {
        return datastore.data.map { preferences ->
            preferences[TEMP_UNIT_SETTING_KEY]
        }
    }

    fun getWindSpeedUnitSetting(): Flow<String?> {
        return datastore.data.map { preferences ->
            preferences[WIND_SPEED_UNIT_SETTING_KEY]
        }
    }

    suspend fun setFavoriteCity(value: String) {
        datastore.edit { preferences ->
            preferences[FAVORITE_CITY_COORDINATE_STRING_KEY] = value
        }
    }


    suspend fun setTemperatureUnit(value: String) {
        datastore.edit { preferences ->
            preferences[TEMP_UNIT_SETTING_KEY] = value
        }
    }

    suspend fun setWindSpeedUnit(value: String) {
        datastore.edit { preferences ->
            preferences[WIND_SPEED_UNIT_SETTING_KEY] = value
        }
    }
}

object DataStoreKeys {
    const val EMPTY_STRING = ""
    val FAVORITE_CITY_COORDINATE_STRING_KEY = stringPreferencesKey("favoriteCityCoordinate")
    val TEMP_UNIT_SETTING_KEY = stringPreferencesKey("settingsTempUnit")
    val WIND_SPEED_UNIT_SETTING_KEY = stringPreferencesKey("settingsWindSpeed")
}
